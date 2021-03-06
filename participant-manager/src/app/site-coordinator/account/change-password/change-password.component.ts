import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Validators, FormBuilder, FormGroup} from '@angular/forms';
import {ToastrService} from 'ngx-toastr';
import {AccountService} from '../shared/account.service';
import {ApiResponse} from 'src/app/entity/api.response.model';
import {getMessage} from 'src/app/shared/success.codes.enum';
import {ChangePassword} from '../shared/profile.model';
import {mustMatch, passwordValidator} from 'src/app/_helper/validator';
import {UnsubscribeOnDestroyAdapter} from 'src/app/unsubscribe-on-destroy-adapter';
import {HeaderDisplayService} from 'src/app/service/header-display.service';
@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss'],
})
export class ChangePasswordComponent
  extends UnsubscribeOnDestroyAdapter
  implements OnInit {
  resetPasswordForm: FormGroup;
  changePasswordTitle = 'Change password';
  currentPasswordValidationMessage = 'Enter your current password';
  currentPasswordPlaceholder = 'Enter current password';
  currentPasswordlabel = 'Current password';
  hideClickable = true;
  constructor(
    private readonly fb: FormBuilder,
    private readonly accountService: AccountService,
    private readonly route: ActivatedRoute,
    private readonly router: Router,
    private readonly toastr: ToastrService,
    private readonly displayHeader: HeaderDisplayService,
  ) {
    super();
    this.resetPasswordForm = this.fb.group(
      {
        // eslint-disable-next-line @typescript-eslint/unbound-method
        currentPassword: ['', [Validators.required]],
        // eslint-disable-next-line @typescript-eslint/unbound-method
        newPassword: ['', [Validators.required, passwordValidator()]],
        // eslint-disable-next-line @typescript-eslint/unbound-method
        confirmPassword: ['', [Validators.required]],
      },
      {
        validator: [mustMatch('newPassword', 'confirmPassword')],
      },
    );
  }
  passCriteria = '';
  get ressetPassword() {
    return this.resetPasswordForm.controls;
  }
  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      if (params.action && params.action === 'passwordsetup') {
        this.changePasswordTitle = 'Set up password';
        this.currentPasswordValidationMessage = 'Enter your temporary password';
        this.currentPasswordPlaceholder = 'Enter temporary password';
        this.currentPasswordlabel = 'Temporary password';
      }
    }),
      (this.passCriteria = `Your password must be at least 8 characters long    
and contain lower case, upper case, numeric and
special characters.`);
    this.displayHeader.showHeaders$.subscribe((visible) => {
      this.hideClickable = visible;
    });
  }
  changePassword(): void {
    if (!this.resetPasswordForm.valid) return;
    const changePassword: ChangePassword = {
      currentPassword: String(
        this.resetPasswordForm.controls['currentPassword'].value,
      ),
      newPassword: String(this.resetPasswordForm.controls['newPassword'].value),
    };
    this.accountService
      .changePassword(changePassword)
      .subscribe((successResponse: ApiResponse) => {
        this.displayHeader.setDisplayHeaderStatus(true);
        if (getMessage(successResponse.code)) {
          this.toastr.success(getMessage(successResponse.code));
        }
        void this.router.navigate(['/coordinator/studies/sites']);
      });
  }
  cancel() {
    void this.router.navigate(['/coordinator/studies/sites']);
  }
}

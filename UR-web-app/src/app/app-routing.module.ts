import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from './auth/login/login.component';
import {SetUpAccountComponent} from './auth/set-up-account/set-up-account.component';
import {ForgotPasswordComponent} from './auth/forgot-password/forgot-password.component';
import {PageNotFoundComponent} from './page-not-found/page-not-found.component';
import {SiteCoordinatorModule} from './site-coordinator/site-coordinator.module';
import {CallbackComponent} from './auth/callback/callback.component';
import {AuthGuard} from './service/auth.guard';
import {LoginGuard} from './service/login.guard';
const routes: Routes = [
  {path: 'login', component: LoginComponent, canActivate: [LoginGuard]},
  {path: 'forgot-password', component: ForgotPasswordComponent},
  {path: 'set-up-account', component: SetUpAccountComponent},
  {path: 'callback', component: CallbackComponent},
  {
    path: 'coordinator',
    loadChildren: async (): Promise<SiteCoordinatorModule> =>
      import('./site-coordinator/site-coordinator.module').then(
        (m) => m.SiteCoordinatorModule,
      ),
    canActivate: [AuthGuard],
  },
  {path: '', redirectTo: '/login', pathMatch: 'full'},
  {path: '**', component: PageNotFoundComponent},
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

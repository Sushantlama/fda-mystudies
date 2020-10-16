/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package com.google.cloud.healthcare.fdamystudies.helper;

import static com.google.cloud.healthcare.fdamystudies.common.CommonConstants.ACTIVE_STATUS;
import static com.google.cloud.healthcare.fdamystudies.common.CommonConstants.NO;
import static com.google.cloud.healthcare.fdamystudies.common.TestConstants.CUSTOM_ID_VALUE;
import static com.google.cloud.healthcare.fdamystudies.common.TestConstants.LOCATION_DESCRIPTION_VALUE;
import static com.google.cloud.healthcare.fdamystudies.common.TestConstants.LOCATION_NAME_VALUE;
import static com.google.cloud.healthcare.fdamystudies.common.TestConstants.VALID_BEARER_TOKEN;

import com.google.cloud.healthcare.fdamystudies.common.CommonConstants;
import com.google.cloud.healthcare.fdamystudies.common.ManageLocation;
import com.google.cloud.healthcare.fdamystudies.common.Permission;
import com.google.cloud.healthcare.fdamystudies.model.AppEntity;
import com.google.cloud.healthcare.fdamystudies.model.AppPermissionEntity;
import com.google.cloud.healthcare.fdamystudies.model.LocationEntity;
import com.google.cloud.healthcare.fdamystudies.model.OrgInfoEntity;
import com.google.cloud.healthcare.fdamystudies.model.ParticipantRegistrySiteEntity;
import com.google.cloud.healthcare.fdamystudies.model.ParticipantStudyEntity;
import com.google.cloud.healthcare.fdamystudies.model.SiteEntity;
import com.google.cloud.healthcare.fdamystudies.model.SitePermissionEntity;
import com.google.cloud.healthcare.fdamystudies.model.StudyConsentEntity;
import com.google.cloud.healthcare.fdamystudies.model.StudyEntity;
import com.google.cloud.healthcare.fdamystudies.model.StudyPermissionEntity;
import com.google.cloud.healthcare.fdamystudies.model.UserDetailsEntity;
import com.google.cloud.healthcare.fdamystudies.model.UserRegAdminEntity;
import com.google.cloud.healthcare.fdamystudies.repository.AppPermissionRepository;
import com.google.cloud.healthcare.fdamystudies.repository.AppRepository;
import com.google.cloud.healthcare.fdamystudies.repository.LocationRepository;
import com.google.cloud.healthcare.fdamystudies.repository.OrgInfoRepository;
import com.google.cloud.healthcare.fdamystudies.repository.ParticipantRegistrySiteRepository;
import com.google.cloud.healthcare.fdamystudies.repository.ParticipantStudyRepository;
import com.google.cloud.healthcare.fdamystudies.repository.SitePermissionRepository;
import com.google.cloud.healthcare.fdamystudies.repository.SiteRepository;
import com.google.cloud.healthcare.fdamystudies.repository.StudyConsentRepository;
import com.google.cloud.healthcare.fdamystudies.repository.StudyPermissionRepository;
import com.google.cloud.healthcare.fdamystudies.repository.StudyRepository;
import com.google.cloud.healthcare.fdamystudies.repository.UserDetailsRepository;
import com.google.cloud.healthcare.fdamystudies.repository.UserRegAdminRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Getter
@Component
public class TestDataHelper {

  public static final String ADMIN_LAST_NAME = "mockito_last_name";

  public static final String ADMIN_FIRST_NAME = "mockito";

  public static final String ADMIN_AUTH_ID_VALUE =
      "TuKUeFdyWz4E2A1-LqQcoYKBpMsfLnl-KjiuRFuxWcM3sQg";

  public static final String EMAIL_VALUE = "mockit_email@grr.la";

  public static final String NON_SUPER_ADMIN_EMAIL_ID = "mockit_non_super_admin_email@grr.la";

  public static final String SUPER_ADMIN_EMAIL_ID = "super_admin_email@grr.la";

  @Autowired private UserRegAdminRepository userRegAdminRepository;

  @Autowired private StudyRepository studyRepository;

  @Autowired private LocationRepository locationRepository;

  @Autowired private StudyPermissionRepository studyPermissionRepository;

  @Autowired private SitePermissionRepository sitePermissionRepository;

  @Autowired private AppPermissionRepository appPermissionRepository;

  @Autowired private AppRepository appRepository;

  @Autowired private SiteRepository siteRepository;

  @Autowired private UserDetailsRepository userDetailsRepository;

  @Autowired private ParticipantRegistrySiteRepository participantRegistrySiteRepository;

  @Autowired private ParticipantStudyRepository participantStudyRepository;

  @Autowired private StudyConsentRepository studyConsentRepository;

  @Autowired private OrgInfoRepository orgInfoRepository;

  public HttpHeaders newCommonHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", VALID_BEARER_TOKEN);
    return headers;
  }

  public UserRegAdminEntity newUserRegAdminEntity() {
    UserRegAdminEntity userRegAdminEntity = new UserRegAdminEntity();
    userRegAdminEntity.setEmail(EMAIL_VALUE);
    userRegAdminEntity.setFirstName(ADMIN_FIRST_NAME);
    userRegAdminEntity.setLastName(ADMIN_LAST_NAME);
    userRegAdminEntity.setEditPermission(Permission.EDIT.value());
    userRegAdminEntity.setStatus(CommonConstants.ACTIVE_STATUS);
    userRegAdminEntity.setUrAdminAuthId(ADMIN_AUTH_ID_VALUE);
    userRegAdminEntity.setSuperAdmin(true);
    userRegAdminEntity.setSecurityCode("xnsxU1Ax1V2Xtpk-qNLeiZ-417JiqyjytC-706-km6gCq9HAXNYWd8");
    userRegAdminEntity.setSecurityCodeExpireDate(
        new Timestamp(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()));
    return userRegAdminEntity;
  }

  public UserRegAdminEntity createUserRegAdmin() {
    UserRegAdminEntity userRegAdminEntity = newUserRegAdminEntity();
    return userRegAdminRepository.saveAndFlush(userRegAdminEntity);
  }

  public UserRegAdminEntity newNonSuperAdmin() {
    UserRegAdminEntity userRegAdminEntity = new UserRegAdminEntity();
    userRegAdminEntity.setEmail(NON_SUPER_ADMIN_EMAIL_ID);
    userRegAdminEntity.setFirstName(ADMIN_FIRST_NAME);
    userRegAdminEntity.setLastName(ADMIN_LAST_NAME);
    userRegAdminEntity.setEditPermission(ManageLocation.DENY.getValue());
    userRegAdminEntity.setStatus(CommonConstants.ACTIVE_STATUS);
    userRegAdminEntity.setSuperAdmin(false);
    return userRegAdminEntity;
  }

  public UserRegAdminEntity createNonSuperAdmin() {
    UserRegAdminEntity userRegAdminEntity = newNonSuperAdmin();
    return userRegAdminRepository.saveAndFlush(userRegAdminEntity);
  }

  public UserRegAdminEntity newSuperAdminForUpdate() {
    UserRegAdminEntity userRegAdminEntity = new UserRegAdminEntity();
    userRegAdminEntity.setEmail(SUPER_ADMIN_EMAIL_ID);
    userRegAdminEntity.setFirstName("mockito_fname");
    userRegAdminEntity.setLastName("mockito__lname");
    userRegAdminEntity.setEditPermission(ManageLocation.ALLOW.getValue());
    userRegAdminEntity.setStatus(CommonConstants.ACTIVE_STATUS);
    userRegAdminEntity.setSuperAdmin(true);
    return userRegAdminEntity;
  }

  public UserRegAdminEntity createSuperAdmin() {
    UserRegAdminEntity userRegAdminEntity = newSuperAdminForUpdate();
    return userRegAdminRepository.saveAndFlush(userRegAdminEntity);
  }

  public StudyEntity newStudyEntity() {
    StudyEntity studyEntity = new StudyEntity();
    studyEntity.setCustomId("StudyID01");
    studyEntity.setCategory("Public Health");
    studyEntity.setEnrolling("Yes");
    studyEntity.setStatus("Active");
    studyEntity.setName("Covid19");
    studyEntity.setSponsor("FDA");
    return studyEntity;
  }

  public AppEntity newAppEntity() {
    AppEntity appEntity = new AppEntity();
    appEntity.setAppId("MyStudies-Id-1");
    appEntity.setAppName("MyStudies-1");
    return appEntity;
  }

  public LocationEntity createLocation() {
    LocationEntity locationEntity = newLocationEntity();
    SiteEntity siteEntity = newSiteEntity();
    locationEntity.addSiteEntity(siteEntity);
    return locationRepository.saveAndFlush(locationEntity);
  }

  public LocationEntity newLocationEntity() {
    LocationEntity locationEntity = new LocationEntity();
    locationEntity.setCustomId(CUSTOM_ID_VALUE);
    locationEntity.setDescription(LOCATION_DESCRIPTION_VALUE);
    locationEntity.setName(LOCATION_NAME_VALUE);
    locationEntity.setStatus(ACTIVE_STATUS);
    locationEntity.setIsDefault(NO);
    return locationEntity;
  }

  public UserRegAdminEntity createUserRegAdminEntity() {
    return userRegAdminRepository.saveAndFlush(newUserRegAdminEntity());
  }

  public AppEntity createAppEntity(UserRegAdminEntity userEntity) {
    AppEntity appEntity = newAppEntity();
    AppPermissionEntity appPermissionEntity = new AppPermissionEntity();
    appPermissionEntity.setEdit(Permission.EDIT);
    appPermissionEntity.setUrAdminUser(userEntity);
    appEntity.addAppPermissionEntity(appPermissionEntity);
    return appRepository.saveAndFlush(appEntity);
  }

  public StudyEntity createStudyEntity(UserRegAdminEntity userEntity, AppEntity appEntity) {
    StudyEntity studyEntity = newStudyEntity();
    studyEntity.setType("CLOSE");
    studyEntity.setName("COVID Study");
    studyEntity.setCustomId("CovidStudy");
    studyEntity.setApp(appEntity);
    StudyPermissionEntity studyPermissionEntity = new StudyPermissionEntity();
    studyPermissionEntity.setUrAdminUser(userEntity);
    studyPermissionEntity.setEdit(Permission.EDIT);
    studyPermissionEntity.setApp(appEntity);
    studyEntity.addStudyPermissionEntity(studyPermissionEntity);
    return studyRepository.saveAndFlush(studyEntity);
  }

  public SiteEntity newSiteEntity() {
    SiteEntity siteEntity = new SiteEntity();
    siteEntity.setName("siteName");
    siteEntity.setStatus(ACTIVE_STATUS);
    return siteEntity;
  }

  public SiteEntity createSiteEntity(
      StudyEntity studyEntity, UserRegAdminEntity urAdminUser, AppEntity appEntity) {
    SiteEntity siteEntity = newSiteEntity();
    siteEntity.setStudy(studyEntity);
    SitePermissionEntity sitePermissionEntity = new SitePermissionEntity();
    sitePermissionEntity.setCanEdit(Permission.EDIT);
    sitePermissionEntity.setStudy(studyEntity);
    sitePermissionEntity.setUrAdminUser(urAdminUser);
    sitePermissionEntity.setApp(appEntity);
    siteEntity.addSitePermissionEntity(sitePermissionEntity);
    return siteRepository.saveAndFlush(siteEntity);
  }

  public ParticipantRegistrySiteEntity createParticipantRegistrySite(
      SiteEntity siteEntity, StudyEntity studyEntity) {
    ParticipantRegistrySiteEntity participantRegistrySiteEntity =
        new ParticipantRegistrySiteEntity();
    participantRegistrySiteEntity.setEnrollmentToken(RandomStringUtils.randomAlphanumeric(8));
    participantRegistrySiteEntity.setInvitationCount(2L);
    participantRegistrySiteEntity.setSite(siteEntity);
    participantRegistrySiteEntity.setStudy(studyEntity);
    return participantRegistrySiteRepository.saveAndFlush(participantRegistrySiteEntity);
  }

  public ParticipantStudyEntity createParticipantStudyEntity(
      SiteEntity siteEntity,
      StudyEntity studyEntity,
      ParticipantRegistrySiteEntity participantRegistrySiteEntity) {
    ParticipantStudyEntity participantStudyEntity = new ParticipantStudyEntity();
    participantStudyEntity.setSite(siteEntity);
    participantStudyEntity.setStudy(studyEntity);
    participantStudyEntity.setParticipantRegistrySite(participantRegistrySiteEntity);
    participantStudyEntity.setSharing(true);
    return participantStudyRepository.saveAndFlush(participantStudyEntity);
  }

  public UserDetailsEntity newUserDetails() {
    UserDetailsEntity userDetailsEntity = new UserDetailsEntity();
    userDetailsEntity.setEmail(EMAIL_VALUE);
    userDetailsEntity.setStatus(1);
    userDetailsEntity.setFirstName(ADMIN_FIRST_NAME);
    userDetailsEntity.setLastName(ADMIN_LAST_NAME);
    userDetailsEntity.setLocalNotificationFlag(false);
    userDetailsEntity.setRemoteNotificationFlag(false);
    userDetailsEntity.setTouchId(false);
    userDetailsEntity.setUsePassCode(false);
    return userDetailsEntity;
  }

  public UserDetailsEntity createUserDetails(AppEntity appEntity) {
    UserDetailsEntity userDetailsEntity = newUserDetails();
    userDetailsEntity.setApp(appEntity);
    return userDetailsRepository.saveAndFlush(userDetailsEntity);
  }

  public StudyConsentEntity createStudyConsentEntity(ParticipantStudyEntity participantStudy) {
    StudyConsentEntity studyConsent = new StudyConsentEntity();
    studyConsent.setPdfPath("documents/test-document.pdf");
    studyConsent.setPdfStorage(1);
    studyConsent.setVersion("1.0");
    studyConsent.setParticipantStudy(participantStudy);
    return studyConsentRepository.saveAndFlush(studyConsent);
  }

  public OrgInfoEntity createOrgInfo() {
    OrgInfoEntity orgInfoEntity = new OrgInfoEntity();
    orgInfoEntity.setName("OrgName");
    orgInfoEntity.setOrgId("OrgName");
    return orgInfoRepository.saveAndFlush(orgInfoEntity);
  }

  public void cleanUp() {
    getStudyConsentRepository().deleteAll();
    getParticipantStudyRepository().deleteAll();
    getParticipantRegistrySiteRepository().deleteAll();
    getSiteRepository().deleteAll();
    getStudyRepository().deleteAll();
    getAppRepository().deleteAll();
    getUserRegAdminRepository().deleteAll();
    getLocationRepository().deleteAll();
    getUserDetailsRepository().deleteAll();
    getOrgInfoRepository().deleteAll();
  }
}
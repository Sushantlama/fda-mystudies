/*
 * Copyright 2020 Google LLC
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */

package com.google.cloud.healthcare.fdamystudies.controller;

import static com.google.cloud.healthcare.fdamystudies.common.CommonConstants.USER_ID_HEADER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.cloud.healthcare.fdamystudies.common.ApiEndpoint;
import com.google.cloud.healthcare.fdamystudies.common.BaseMockIT;
import com.google.cloud.healthcare.fdamystudies.common.ErrorCode;
import com.google.cloud.healthcare.fdamystudies.common.IdGenerator;
import com.google.cloud.healthcare.fdamystudies.helper.TestDataHelper;
import com.google.cloud.healthcare.fdamystudies.model.AppEntity;
import com.google.cloud.healthcare.fdamystudies.model.LocationEntity;
import com.google.cloud.healthcare.fdamystudies.model.ParticipantRegistrySiteEntity;
import com.google.cloud.healthcare.fdamystudies.model.ParticipantStudyEntity;
import com.google.cloud.healthcare.fdamystudies.model.SiteEntity;
import com.google.cloud.healthcare.fdamystudies.model.StudyEntity;
import com.google.cloud.healthcare.fdamystudies.model.UserDetailsEntity;
import com.google.cloud.healthcare.fdamystudies.model.UserRegAdminEntity;
import com.google.cloud.healthcare.fdamystudies.service.AppService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

public class AppControllerTest extends BaseMockIT {

  @Autowired private AppController controller;

  @Autowired private AppService appService;

  @Autowired private TestDataHelper testDataHelper;

  private ParticipantRegistrySiteEntity participantRegistrySiteEntity;
  private ParticipantStudyEntity participantStudyEntity;
  private UserRegAdminEntity userRegAdminEntity;
  private AppEntity appEntity;
  private StudyEntity studyEntity;
  private SiteEntity siteEntity;
  private UserDetailsEntity userDetailsEntity;
  private LocationEntity locationEntity;

  @BeforeEach
  public void setUp() {
    userRegAdminEntity = testDataHelper.createUserRegAdminEntity();
    appEntity = testDataHelper.createAppEntity(userRegAdminEntity);
    studyEntity = testDataHelper.createStudyEntity(userRegAdminEntity, appEntity);
    siteEntity = testDataHelper.createSiteEntity(studyEntity, userRegAdminEntity, appEntity);
    userDetailsEntity = testDataHelper.createUserDetails(appEntity);
    participantRegistrySiteEntity =
        testDataHelper.createParticipantRegistrySite(siteEntity, studyEntity);
    participantStudyEntity =
        testDataHelper.createParticipantStudyEntity(
            siteEntity, studyEntity, participantRegistrySiteEntity);
  }

  @Test
  public void contextLoads() {
    assertNotNull(controller);
    assertNotNull(mockMvc);
    assertNotNull(appService);
  }

  @Test
  public void shouldReturnAppsRegisteredByUser() throws Exception {
    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, userRegAdminEntity.getId());

    mockMvc
        .perform(get(ApiEndpoint.GET_APPS.getPath()).headers(headers).contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.apps").isArray())
        .andExpect(jsonPath("$.apps", hasSize(1)))
        .andExpect(jsonPath("$.apps[0].customId").value(appEntity.getAppId()))
        .andExpect(jsonPath("$.apps[0].name").value(appEntity.getAppName()))
        .andExpect(jsonPath("$.studyPermissionCount").value(1));
  }

  @Test
  public void shouldReturnBadRequestForGetApps() throws Exception {
    HttpHeaders headers = testDataHelper.newCommonHeaders();

    mockMvc
        .perform(get(ApiEndpoint.GET_APPS.getPath()).headers(headers).contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.violations").isArray())
        .andExpect(jsonPath("$.violations[0].path").value("userId"))
        .andExpect(jsonPath("$.violations[0].message").value("header is required"));
  }

  @Test
  public void shouldNotReturnApp() throws Exception {
    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, IdGenerator.id());

    mockMvc
        .perform(get(ApiEndpoint.GET_APPS.getPath()).headers(headers).contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error_description").value(ErrorCode.APP_NOT_FOUND.getDescription()));
  }

  @Test
  public void shouldReturnAppsWithOptionalStudiesAndSites() throws Exception {
    // Step 1: set app,study and location
    studyEntity.setApp(appEntity);
    siteEntity.setStudy(studyEntity);
    locationEntity = testDataHelper.createLocation();
    siteEntity.setLocation(locationEntity);
    testDataHelper.getSiteRepository().save(siteEntity);

    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, userRegAdminEntity.getId());
    String[] fields = {"studies", "sites"};

    // Step 2: Call API and expect success message
    mockMvc
        .perform(
            get(ApiEndpoint.GET_APPS.getPath())
                .headers(headers)
                .contextPath(getContextPath())
                .queryParam("fields", fields))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.apps").isArray())
        .andExpect(jsonPath("$.apps", hasSize(1)))
        .andExpect(jsonPath("$.apps[0].studies").isArray())
        .andExpect(jsonPath("$.apps[0].studies[0].sites").isArray())
        .andExpect(jsonPath("$.apps[0].customId").value(appEntity.getAppId()))
        .andExpect(jsonPath("$.apps[0].name").value(appEntity.getAppName()))
        .andExpect(jsonPath("$.apps[0].totalSitesCount").value(1))
        .andExpect(jsonPath("$.apps[0].studies[0].totalSitesCount").value(1));
  }

  @Test
  public void shouldReturnAppsWithOptionalStudies() throws Exception {
    // Step 1: set app and study
    studyEntity.setApp(appEntity);
    testDataHelper.getStudyRepository().save(studyEntity);

    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, userRegAdminEntity.getId());
    String[] fields = {"studies"};

    // Step 2: Call API and expect success message
    mockMvc
        .perform(
            get(ApiEndpoint.GET_APPS.getPath())
                .headers(headers)
                .contextPath(getContextPath())
                .queryParam("fields", fields))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.apps").isArray())
        .andExpect(jsonPath("$.apps", hasSize(1)))
        .andExpect(jsonPath("$.apps[0].studies").isArray())
        .andExpect(jsonPath("$.apps[0].studies[0].sites").isEmpty())
        .andExpect(jsonPath("$.apps[0].studies[0].studyName").value(studyEntity.getName()))
        .andExpect(jsonPath("$.apps[0].customId").value(appEntity.getAppId()))
        .andExpect(jsonPath("$.apps[0].name").value(appEntity.getAppName()));
  }

  @Test
  public void shouldReturnForbiddenForGetAppDetailsAccessDenied() throws Exception {
    // Step 1 : set SuperAdmin to false
    userRegAdminEntity.setSuperAdmin(false);
    testDataHelper.getUserRegAdminRepository().save(userRegAdminEntity);

    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, userRegAdminEntity.getId());
    String[] fields = {"studies", "sites"};

    // Step 2: Call API and expect USER_ADMIN_ACCESS_DENIED
    mockMvc
        .perform(
            get(ApiEndpoint.GET_APPS.getPath())
                .headers(headers)
                .contextPath(getContextPath())
                .queryParam("fields", fields))
        .andDo(print())
        .andExpect(status().isForbidden())
        .andExpect(
            jsonPath(
                "$.error_description", is(ErrorCode.USER_ADMIN_ACCESS_DENIED.getDescription())));
  }

  @Test
  public void shouldReturnGetAppParticipants() throws Exception {
    // Step 1 : Set studyEntity,siteEntity,locationEntity,userDetailsEntity
    studyEntity.setApp(appEntity);
    siteEntity.setStudy(studyEntity);
    locationEntity = testDataHelper.createLocation();
    siteEntity.setLocation(locationEntity);
    participantStudyEntity.setUserDetails(userDetailsEntity);
    testDataHelper.getParticipantStudyRepository().saveAndFlush(participantStudyEntity);

    // Step 2: Call API to return GET_APPS_PARTICIPANTS
    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, userRegAdminEntity.getId());

    mockMvc
        .perform(
            get(ApiEndpoint.GET_APP_PARTICIPANTS.getPath(), appEntity.getId())
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.participants").isArray())
        .andExpect(jsonPath("$.participants", hasSize(1)))
        .andExpect(jsonPath("$.participants[0].enrolledStudies").isArray())
        .andExpect(jsonPath("$.participants[0].enrolledStudies", hasSize(1)))
        .andExpect(jsonPath("$.participants[0].email").value(userDetailsEntity.getEmail()))
        .andExpect(
            jsonPath("$.participants[0].enrolledStudies[0].studyName").value(studyEntity.getName()))
        .andExpect(jsonPath("$.customId").value(appEntity.getAppId()))
        .andExpect(jsonPath("$.name").value(appEntity.getAppName()));
  }

  @Test
  public void shouldNotReturnAppsForGetAppParticipants() throws Exception {
    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, IdGenerator.id());

    mockMvc
        .perform(
            get(ApiEndpoint.GET_APP_PARTICIPANTS.getPath(), appEntity.getId())
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error_description").value(ErrorCode.APP_NOT_FOUND.getDescription()));
  }

  @Test
  public void shouldReturnBadRequestForGetAppParticipants() throws Exception {
    HttpHeaders headers = testDataHelper.newCommonHeaders();

    mockMvc
        .perform(
            get(ApiEndpoint.GET_APP_PARTICIPANTS.getPath(), appEntity.getId())
                .headers(headers)
                .contextPath(getContextPath()))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.violations").isArray())
        .andExpect(jsonPath("$.violations[0].path").value("userId"))
        .andExpect(jsonPath("$.violations[0].message").value("header is required"));
  }

  public void shouldReturnInvalidAppsFieldsValues() throws Exception {
    // Step 1: set app and study
    studyEntity.setApp(appEntity);
    testDataHelper.getStudyRepository().save(studyEntity);

    HttpHeaders headers = testDataHelper.newCommonHeaders();
    headers.set(USER_ID_HEADER, userRegAdminEntity.getId());
    String[] fields = {"apps"};

    // Step 2: Call API
    mockMvc
        .perform(
            get(ApiEndpoint.GET_APPS.getPath())
                .headers(headers)
                .contextPath(getContextPath())
                .queryParam("fields", fields))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.error_description")
                .value(ErrorCode.INVALID_APPS_FIELDS_VALUES.getDescription()));
  }

  @AfterEach
  public void clean() {
    testDataHelper.cleanUp();
  }
}
/*
 * Copyright © 2017-2018 Harvard Pilgrim Health Care Institute (HPHCI) and its Contributors.
 * Copyright 2020-2021 Google LLC
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * Funding Source: Food and Drug Administration ("Funding Agency") effective 18 September 2014 as
 * Contract no. HHSF22320140030I/HHSF22301006T (the "Prime Contract").
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.fdahpstudydesigner.service;

import com.fdahpstudydesigner.bean.StudyDetailsBean;
import com.fdahpstudydesigner.bean.StudyIdBean;
import com.fdahpstudydesigner.bean.StudyListBean;
import com.fdahpstudydesigner.bean.StudyPageBean;
import com.fdahpstudydesigner.bo.Checklist;
import com.fdahpstudydesigner.bo.ComprehensionTestQuestionBo;
import com.fdahpstudydesigner.bo.ComprehensionTestResponseBo;
import com.fdahpstudydesigner.bo.ConsentBo;
import com.fdahpstudydesigner.bo.ConsentInfoBo;
import com.fdahpstudydesigner.bo.ConsentMasterInfoBo;
import com.fdahpstudydesigner.bo.EligibilityBo;
import com.fdahpstudydesigner.bo.EligibilityTestBo;
import com.fdahpstudydesigner.bo.NotificationBO;
import com.fdahpstudydesigner.bo.ReferenceTablesBo;
import com.fdahpstudydesigner.bo.ResourceBO;
import com.fdahpstudydesigner.bo.StudyBo;
import com.fdahpstudydesigner.bo.StudyPageBo;
import com.fdahpstudydesigner.bo.StudyPermissionBO;
import com.fdahpstudydesigner.util.SessionObject;
import java.util.List;
import java.util.Map;

public interface StudyService {

  public String checkActiveTaskTypeValidation(Integer studyId);

  public int comprehensionTestQuestionOrder(Integer studyId);

  public int consentInfoOrder(Integer studyId);

  public boolean copyliveStudyByCustomStudyId(String customStudyId, SessionObject sesObj);

  public String deleteComprehensionTestQuestion(
      Integer questionId, Integer studyId, SessionObject sessionObject);

  public String deleteConsentInfo(
      Integer consentInfoId, Integer studyId, SessionObject sessionObject, String customStudyId);

  public String deleteEligibilityTestQusAnsById(
      Integer eligibilityTestId,
      Integer studyId,
      SessionObject sessionObject,
      String customStudyId);

  public String deleteOverviewStudyPageById(String studyId, String pageId);

  public String deleteResourceInfo(
      Integer resourceInfoId, SessionObject sesObj, String customStudyId, int studyId);

  public boolean deleteStudyByCustomStudyId(String customStudyId);

  public List<StudyBo> getAllStudyList();

  public Checklist getchecklistInfo(Integer studyId);

  public ComprehensionTestQuestionBo getComprehensionTestQuestionById(Integer questionId);

  public List<ComprehensionTestQuestionBo> getComprehensionTestQuestionList(Integer studyId);

  public List<ComprehensionTestResponseBo> getComprehensionTestResponseList(
      Integer comprehensionQuestionId);

  public ConsentBo getConsentDetailsByStudyId(String studyId);

  public ConsentInfoBo getConsentInfoById(Integer consentInfoId);

  public List<ConsentInfoBo> getConsentInfoDetailsListByStudyId(String studyId);

  public List<ConsentInfoBo> getConsentInfoList(Integer studyId);

  public List<ConsentMasterInfoBo> getConsentMasterInfoList();

  public StudyIdBean getLiveVersion(String customStudyId);

  public List<StudyPageBo> getOverviewStudyPagesById(String studyId, Integer userId);

  public Map<String, List<ReferenceTablesBo>> getreferenceListByCategory();

  public ResourceBO getResourceInfo(Integer resourceInfoId);

  public List<ResourceBO> getResourceList(Integer studyId);

  public List<NotificationBO> getSavedNotification(Integer studyId);

  public StudyBo getStudyById(String studyId, Integer userId);

  public EligibilityBo getStudyEligibiltyByStudyId(String studyId);

  public List<StudyListBean> getStudyList(Integer userId);

  public List<StudyListBean> getStudyListByUserId(Integer userId);

  public StudyBo getStudyLiveStatusByCustomId(String customStudyId);

  public ResourceBO getStudyProtocol(Integer studyId);

  public String markAsCompleted(
      int studyId, String markCompleted, Boolean flag, SessionObject sesObj, String customStudyId);

  public String markAsCompleted(
      int studyId, String markCompleted, SessionObject sesObj, String customStudyId);

  public String reOrderComprehensionTestQuestion(
      Integer studyId, int oldOrderNumber, int newOrderNumber);

  public String reOrderConsentInfoList(Integer studyId, int oldOrderNumber, int newOrderNumber);

  public String reorderEligibilityTestQusAns(
      Integer eligibilityId, int oldOrderNumber, int newOrderNumber, Integer studyId);

  public String reOrderResourceList(Integer studyId, int oldOrderNumber, int newOrderNumber);

  public boolean resetDraftStudyByCustomStudyId(String customStudyId);

  public int resourceOrder(Integer studyId);

  public List<ResourceBO> resourcesSaved(Integer studyId);

  public List<ResourceBO> resourcesWithAnchorDate(Integer studyId);

  public ConsentBo saveOrCompleteConsentReviewDetails(
      ConsentBo consentBo, SessionObject sesObj, String customStudyId);

  public Integer saveOrDoneChecklist(
      Checklist checklist, String actionBut, SessionObject sesObj, String customStudyId);

  public ComprehensionTestQuestionBo saveOrUpdateComprehensionTestQuestion(
      ComprehensionTestQuestionBo comprehensionTestQuestionBo);

  public ConsentInfoBo saveOrUpdateConsentInfo(
      ConsentInfoBo consentInfoBo, SessionObject sessionObject, String customStudyId);

  public Integer saveOrUpdateEligibilityTestQusAns(
      EligibilityTestBo eligibilityTestBo,
      Integer studyId,
      SessionObject sessionObject,
      String customStudyId);

  public String saveOrUpdateOverviewStudyPages(StudyPageBean studyPageBean, SessionObject sesObj);

  public Integer saveOrUpdateResource(ResourceBO resourceBO, SessionObject sesObj);

  public String saveOrUpdateStudy(StudyBo studyBo, Integer userId, SessionObject sessionObject);

  public String saveOrUpdateStudyEligibilty(
      EligibilityBo eligibilityBo, SessionObject sesObj, String customStudyId);

  public String saveOrUpdateStudySettings(StudyBo studyBo, SessionObject sesObj);

  public String updateStudyActionOnAction(String studyId, String buttonText, SessionObject sesObj);

  public String validateActivityComplete(String studyId, String action);

  public String validateEligibilityTestKey(
      Integer eligibilityTestId, String shortTitle, Integer eligibilityId);

  public String validateStudyAction(String studyId, String buttonText);

  public boolean validateStudyId(String studyId);

  public List<EligibilityTestBo> viewEligibilityTestQusAnsByEligibilityId(Integer eligibilityId);

  public EligibilityTestBo viewEligibilityTestQusAnsById(Integer eligibilityTestId);

  public Boolean isAnchorDateExistForEnrollment(Integer studyId, String customStudyId);

  public Boolean isAnchorDateExistForEnrollmentDraftStudy(Integer studyId, String customStudyId);

  public boolean validateAppId(String customStudyId, String appId, String studyType);

  public StudyPermissionBO findStudyPermissionBO(int studyId, int userId);

  public StudyDetailsBean getStudyByLatestVersion(String customStudyId);

  public boolean validateStudyActions(String studyId);

  public StudyBo getStudyInfo(String studyId);
}

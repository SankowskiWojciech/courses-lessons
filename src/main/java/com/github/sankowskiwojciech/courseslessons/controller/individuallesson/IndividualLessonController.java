package com.github.sankowskiwojciech.courseslessons.controller.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequestParams;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator.IndividualLessonRequestValidator;
import com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator.IndividualLessonsScheduleRequestValidator;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonsSchedulerService;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.validator.IndividualLessonValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.tokenvalidation.TokenValidationService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/lessons/individual")
public class IndividualLessonController {

    private final TokenValidationService tokenValidationService;
    private final IndividualLessonValidatorService individualLessonValidatorService;
    private final IndividualLessonService individualLessonService;
    private final IndividualLessonsSchedulerService individualLessonsSchedulerService;
    private final SubdomainService subdomainService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public IndividualLessonResponse createIndividualLesson(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestBody IndividualLessonRequest individualLessonRequest) {
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequest);
        tokenValidationService.validateTokenAndUser(authorizationHeaderValue, individualLessonRequest.getTutorId());
        IndividualLesson individualLesson = individualLessonValidatorService.validateCreateIndividualLessonRequest(individualLessonRequest);
        return individualLessonService.createIndividualLesson(individualLesson);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/schedule")
    public List<IndividualLessonResponse> scheduleIndividualLessons(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestBody IndividualLessonsScheduleRequest individualLessonsScheduleRequest) {
        IndividualLessonsScheduleRequestValidator.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequest);
        tokenValidationService.validateTokenAndUser(authorizationHeaderValue, individualLessonsScheduleRequest.getTutorId());
        IndividualLessonsSchedule individualLessonsSchedule = individualLessonValidatorService.validateIndividualLessonsScheduleRequest(individualLessonsScheduleRequest);
        return individualLessonsSchedulerService.scheduleIndividualLessons(individualLessonsSchedule);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<IndividualLessonResponse> readIndividualLessons(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestParam(value = "subdomainAlias", required = false) String subdomainAlias, @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate, @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        TokenEntity tokenEntity = tokenValidationService.validateToken(authorizationHeaderValue);
        Subdomain subdomain = subdomainService.validateIfUserIsAllowedToLoginToSubdomain(subdomainAlias, tokenEntity.getUserEmailAddress());
        AccountInfo accountInfo = new AccountInfo(tokenEntity.getUserEmailAddress(), tokenEntity.getAccountType());
        IndividualLessonRequestParams individualLessonRequestParams = new IndividualLessonRequestParams(subdomain, fromDate, toDate);
        return individualLessonService.readIndividualLessons(accountInfo, individualLessonRequestParams);
    }
}

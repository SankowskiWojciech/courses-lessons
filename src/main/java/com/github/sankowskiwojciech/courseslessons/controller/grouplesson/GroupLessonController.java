package com.github.sankowskiwojciech.courseslessons.controller.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonsSchedule;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonsScheduleRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator.GroupLessonRequestValidator;
import com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator.GroupLessonsScheduleRequestValidator;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.GroupLessonService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.GroupLessonsSchedulerService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator.GroupLessonValidatorService;
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
@RequestMapping("/lessons/group")
public class GroupLessonController {
    private final TokenValidationService tokenValidationService;
    private final SubdomainService subdomainService;
    private final GroupLessonValidatorService groupLessonValidatorService;
    private final GroupLessonService groupLessonService;
    private final GroupLessonsSchedulerService groupLessonsSchedulerService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public GroupLessonResponse createGroupLesson(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestBody GroupLessonRequest request) {
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(request);
        TokenEntity token = tokenValidationService.validateToken(authorizationHeaderValue);
        GroupLesson groupLesson = groupLessonValidatorService.validateCreateGroupLessonRequest(request, token.getUserEmailAddress());
        return groupLessonService.createGroupLesson(groupLesson);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<GroupLessonResponse> readGroupLessons(
            @RequestHeader(value = "Authorization") String authorizationHeaderValue,
            @RequestParam(value = "subdomainAlias", required = false) String subdomainAlias,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate) {
        TokenEntity token = tokenValidationService.validateToken(authorizationHeaderValue);
        Subdomain subdomain = subdomainService.validateIfUserHasAccessToSubdomain(subdomainAlias, token.getUserEmailAddress());
        AccountInfo accountInfo = new AccountInfo(token.getUserEmailAddress(), token.getAccountType());
        LessonRequestParams requestParams = new LessonRequestParams(subdomain, fromDate, toDate);
        return groupLessonService.readGroupLessons(accountInfo, requestParams);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/schedule")
    public List<GroupLessonResponse> scheduleGroupLessons(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestBody GroupLessonsScheduleRequest request) {
        GroupLessonsScheduleRequestValidator.validateGroupLessonsScheduleRequest(request);
        TokenEntity token = tokenValidationService.validateToken(authorizationHeaderValue);
        GroupLessonsSchedule schedule = groupLessonValidatorService.validateGroupLessonsScheduleRequest(request, token.getUserEmailAddress());
        return groupLessonsSchedulerService.scheduleGroupLessons(schedule);
    }
}

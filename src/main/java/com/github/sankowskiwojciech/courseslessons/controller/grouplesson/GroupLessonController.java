package com.github.sankowskiwojciech.courseslessons.controller.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator.GroupLessonRequestValidator;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator.GroupLessonValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.tokenvalidation.TokenValidationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/lessons/group")
public class GroupLessonController {

    private final TokenValidationService tokenValidationService;
    private final GroupLessonValidatorService groupLessonValidatorService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public GroupLessonResponse createGroupLesson(@RequestHeader(value = "Authorization") String authorizationHeaderValue, GroupLessonRequest groupLessonRequest) {
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(groupLessonRequest);
        tokenValidationService.validateTokenAndUser(authorizationHeaderValue, groupLessonRequest.getTutorId());
        GroupLesson groupLesson = groupLessonValidatorService.validateCreateGroupLessonRequest(groupLessonRequest);
//        return individualLessonService.createIndividualLesson(individualLesson);
        return null;
    }
}

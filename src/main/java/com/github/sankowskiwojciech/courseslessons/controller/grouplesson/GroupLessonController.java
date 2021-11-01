package com.github.sankowskiwojciech.courseslessons.controller.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import com.github.sankowskiwojciech.courseslessons.controller.grouplesson.validator.GroupLessonRequestValidator;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.GroupLessonService;
import com.github.sankowskiwojciech.courseslessons.service.grouplesson.validator.GroupLessonValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.tokenvalidation.TokenValidationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/lessons/group")
public class GroupLessonController {
    private final TokenValidationService tokenValidationService;
    private final GroupLessonValidatorService groupLessonValidatorService;
    private final GroupLessonService groupLessonService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public GroupLessonResponse createGroupLesson(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestBody GroupLessonRequest request) {
        GroupLessonRequestValidator.validateCreateGroupLessonRequest(request);
        TokenEntity token = tokenValidationService.validateToken(authorizationHeaderValue);
        GroupLesson groupLesson = groupLessonValidatorService.validateCreateGroupLessonRequest(request, token.getUserEmailAddress());
        return groupLessonService.createGroupLesson(groupLesson);
    }
}

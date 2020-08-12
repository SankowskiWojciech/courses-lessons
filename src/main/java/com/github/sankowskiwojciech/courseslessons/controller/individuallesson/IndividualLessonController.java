package com.github.sankowskiwojciech.courseslessons.controller.individuallesson;

import com.github.sankowskiwojciech.courseslessons.controller.individuallesson.validator.IndividualLessonRequestValidator;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonRequest;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.IndividualLessonService;
import com.github.sankowskiwojciech.courseslessons.service.individuallessonvalidator.IndividualLessonValidatorService;
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
@RequestMapping("/lessons/individual")
public class IndividualLessonController {

    private final TokenValidationService tokenValidationService;
    private final IndividualLessonValidatorService individualLessonValidatorService;
    private final IndividualLessonService individualLessonService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public IndividualLessonResponse createIndividualLesson(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestBody IndividualLessonRequest individualLessonRequest) {
        IndividualLessonRequestValidator.validateCreateIndividualLessonRequest(individualLessonRequest);
        tokenValidationService.validateTokenAndUser(authorizationHeaderValue, individualLessonRequest.getTutorId());
        IndividualLesson individualLesson = individualLessonValidatorService.validateCreateIndividualLessonRequest(individualLessonRequest);
        return individualLessonService.createIndividualLesson(individualLesson);
    }
}

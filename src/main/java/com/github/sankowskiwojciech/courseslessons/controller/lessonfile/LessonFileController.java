package com.github.sankowskiwojciech.courseslessons.controller.lessonfile;

import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.UserPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.tokenvalidation.TokenValidationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/lessons/files")
public class LessonFileController {

    private final TokenValidationService tokenValidationService;
    private final UserPermissionValidatorService userPermissionValidatorService;
    private final LessonFileValidatorService lessonFileValidatorService;
    private final LessonFileService lessonFileService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public LessonFileResponse createFile(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestParam("file") MultipartFile file) throws IOException {
        TokenEntity tokenEntity = tokenValidationService.validateToken(authorizationHeaderValue);
        userPermissionValidatorService.validateIfUserIsAllowedToCreateFile(tokenEntity.getUserEmailAddress());
        LessonFile lessonFile = lessonFileValidatorService.validateFile(file);
        return lessonFileService.createLessonFile(lessonFile, tokenEntity.getUserEmailAddress());
    }
}

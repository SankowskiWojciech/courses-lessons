package com.github.sankowskiwojciech.courseslessons.controller.lessonfile;

import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.file.LessonFileService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileToResponseEntityOfStreamingResponseBody;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.FileAccessPermissionValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.lesson.validator.LessonFileValidatorService;
import com.github.sankowskiwojciech.courseslessons.service.tokenvalidation.TokenValidationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@AllArgsConstructor
@RestController
@RequestMapping("/lessons/files")
public class LessonFileController {

    private final TokenValidationService tokenValidationService;
    private final FileAccessPermissionValidatorService fileAccessPermissionValidatorService;
    private final LessonFileValidatorService lessonFileValidatorService;
    private final LessonFileService lessonFileService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public LessonFileResponse createFile(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestParam("file") MultipartFile file) {
        TokenEntity tokenEntity = tokenValidationService.validateToken(authorizationHeaderValue);
        fileAccessPermissionValidatorService.validateIfUserIsAllowedToCreateFile(tokenEntity.getUserEmailAddress());
        LessonFile lessonFile = lessonFileValidatorService.validateUploadedFile(file);
        return lessonFileService.createLessonFile(lessonFile, tokenEntity.getUserEmailAddress());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{fileId}")
    public ResponseEntity<StreamingResponseBody> readFile(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @PathVariable long fileId) {
        TokenEntity tokenEntity = tokenValidationService.validateToken(authorizationHeaderValue);
        lessonFileValidatorService.validateIfFileExists(fileId);
        fileAccessPermissionValidatorService.validateIfUserIsAllowedToReadFile(tokenEntity.getUserEmailAddress(), fileId);
        LessonFile lessonFile = lessonFileService.readLessonFile(fileId);
        return LessonFileToResponseEntityOfStreamingResponseBody.getInstance().apply(lessonFile);
    }
}

package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import org.springframework.web.multipart.MultipartFile;

public interface LessonFileValidatorService {

    LessonFile validateUploadedFile(MultipartFile file);

    void validateIfFileExists(String fileId);
}

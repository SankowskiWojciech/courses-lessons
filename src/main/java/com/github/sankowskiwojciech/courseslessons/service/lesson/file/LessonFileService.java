package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;

public interface LessonFileService {
    LessonFileResponse createLessonFile(LessonFile lessonFile, String userId);
}

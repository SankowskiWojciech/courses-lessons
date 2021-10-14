package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;

import java.util.List;

public interface LessonFileService {
    LessonFileResponse createLessonFile(LessonFile file, String userId);

    LessonFile readLessonFile(String fileId);

    List<LessonFileResponse> readFilesInformation(String fileOwnerId);

    List<LessonFileAccessEntity> attachFilesToLesson(String lessonId, List<String> filesIds);
}

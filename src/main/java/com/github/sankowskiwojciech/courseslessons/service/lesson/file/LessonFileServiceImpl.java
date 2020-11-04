package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileAndUserIdToLessonFileEntity;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileEntityToLessonFile;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileEntityToLessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileWithoutContentToLessonFileResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonFileServiceImpl implements LessonFileService {

    private final LessonFileRepository lessonFileRepository;

    @Transactional
    @Override
    public LessonFileResponse createLessonFile(LessonFile lessonFile, String userId) {
        LessonFileEntity lessonFileEntity = LessonFileAndUserIdToLessonFileEntity.getInstance().apply(lessonFile, userId);
        LessonFileEntity savedLessonFileEntity = lessonFileRepository.save(lessonFileEntity);
        return LessonFileEntityToLessonFileResponse.getInstance().apply(savedLessonFileEntity);
    }

    @Override
    public LessonFile readLessonFile(long fileId) {
        LessonFileEntity lessonFileEntity = lessonFileRepository.findById(fileId).get();
        return LessonFileEntityToLessonFile.getInstance().apply(lessonFileEntity);
    }

    @Override
    public List<LessonFileResponse> readFilesInformation(String fileOwnerId) {
        List<LessonFileWithoutContent> lessonFilesWithoutContent = lessonFileRepository.findAllByCreatedBy(fileOwnerId);
        return lessonFilesWithoutContent.stream().map(lessonFileWithoutContent -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(lessonFileWithoutContent)).collect(Collectors.toList());
    }
}

package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonFileServiceImpl implements LessonFileService {

    private final LessonFileRepository lessonFileRepository;
    private final IndividualLessonRepository individualLessonRepository;
    private final IndividualLessonFileRepository individualLessonFileRepository;

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
    public List<LessonFileResponse> readFilesInformation(String userId) {
        List<IndividualLessonEntity> lessonEntitiesRelatedToUser = individualLessonRepository.findAllByUserId(userId);
        List<Long> idsOfLessonsRelatedToUser = lessonEntitiesRelatedToUser.stream().map(IndividualLessonEntity::getLessonId).collect(Collectors.toList());
        List<IndividualLessonFileEntity> lessonsFilesRelatedToUser = individualLessonFileRepository.findAllByLessonIdIn(idsOfLessonsRelatedToUser);
        Set<Long> idsOfLessonsFilesRelatedToUser = lessonsFilesRelatedToUser.stream().map(IndividualLessonFileEntity::getFileId).collect(Collectors.toSet());
        List<LessonFileWithoutContent> lessonFilesWithoutContent = lessonFileRepository.findAllByFileIdIn(idsOfLessonsFilesRelatedToUser);
        return lessonFilesWithoutContent.stream().map(lessonFileWithoutContent -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(lessonFileWithoutContent)).collect(Collectors.toList());
    }
}

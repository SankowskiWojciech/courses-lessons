package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonFileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileAccessPermissionValidatorServiceImpl implements FileAccessPermissionValidatorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final IndividualLessonFileRepository individualLessonFileRepository;
    private final IndividualLessonRepository individualLessonRepository;
    private final LessonFileRepository lessonFileRepository;

    @Override
    public void validateIfUserIsAllowedToCreateFile(String userId) {
        if (!tutorRepository.existsById(userId) && !studentRepository.existsById(userId)) {
            throw new UserNotAllowedToCreateFileException();
        }
    }

    @Override
    public void validateIfUserIsAllowedToAccessFile(String userId, long fileId) {
        if (!lessonFileRepository.getFileOwnerId(fileId).equals(userId)) {
            List<IndividualLessonFileEntity> lessonsWhichFileBelongsTo = individualLessonFileRepository.findAllByFileId(fileId);
            List<Long> lessonsIdsWhichFileBelongsTo = lessonsWhichFileBelongsTo.stream().map(IndividualLessonFileEntity::getLessonId).collect(Collectors.toList());
            List<IndividualLessonEntity> lessonsFoundByUserIdAndLessonsIds = individualLessonRepository.findAllByUserIdAndLessonsIds(userId, lessonsIdsWhichFileBelongsTo);
            if (lessonsFoundByUserIdAndLessonsIds.isEmpty()) {
                throw new UserNotAllowedToAccessFileException();
            }
        }
    }
}

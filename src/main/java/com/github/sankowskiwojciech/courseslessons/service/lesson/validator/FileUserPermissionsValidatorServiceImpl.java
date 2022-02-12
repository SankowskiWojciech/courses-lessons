package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.lesson.LessonFileAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FileUserPermissionsValidatorServiceImpl implements FileUserPermissionsValidatorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final LessonFileAccessRepository lessonFileAccessRepository;
    private final IndividualLessonRepository individualLessonRepository;
    private final FileRepository fileRepository;

    @Override
    public void validateIfUserIsAllowedToCreateFile(String userId) {
        if (!tutorRepository.existsById(userId) && !studentRepository.existsById(userId)) {
            throw new UserNotAllowedToCreateFileException();
        }
    }

    @Override
    public void validateIfUserIsAllowedToAccessFile(String userId, String fileId) {
        if (!fileRepository.getFileOwnerIdByFileId(fileId).equals(userId)) {
            List<LessonFileAccessEntity> lessonsWhichFileBelongsTo = lessonFileAccessRepository.findAllByFileId(fileId);
            List<String> lessonsIdsWhichFileBelongsTo = lessonsWhichFileBelongsTo.stream().map(LessonFileAccessEntity::getLessonId).collect(Collectors.toList());
            List<IndividualLessonEntity> lessonsFoundByUserIdAndLessonsIds = individualLessonRepository.findAllByUserIdAndLessonIdIn(userId, lessonsIdsWhichFileBelongsTo);
            if (lessonsFoundByUserIdAndLessonsIds.isEmpty()) {
                throw new UserNotAllowedToAccessFileException();
            }
        }
    }
}

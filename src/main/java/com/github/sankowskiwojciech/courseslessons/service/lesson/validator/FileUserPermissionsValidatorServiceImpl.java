package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileUserPermissionsRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntityId;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessFileException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class FileUserPermissionsValidatorServiceImpl implements FileUserPermissionsValidatorService {
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final FileUserPermissionsRepository fileUserPermissionsRepository;

    @Override
    public void validateIfUserIsAllowedToCreateFile(String userId) {
        if (!tutorRepository.existsById(userId) && !studentRepository.existsById(userId)) {
            throw new UserNotAllowedToCreateFileException();
        }
    }

    @Override
    public void validateIfUserIsAllowedToAccessFile(String userId, String fileId) {
        Optional<FileUserPermissionsEntity> entity = fileUserPermissionsRepository.findById(new FileUserPermissionsEntityId(fileId, userId));
        if (!entity.isPresent() || !entity.get().canRead()) {
            throw new UserNotAllowedToAccessFileException();
        }
    }
}
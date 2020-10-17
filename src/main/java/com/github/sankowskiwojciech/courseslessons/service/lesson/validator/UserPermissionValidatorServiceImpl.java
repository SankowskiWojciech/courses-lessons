package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToCreateFileException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPermissionValidatorServiceImpl implements UserPermissionValidatorService {

    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;

    @Override
    public void validateIfUserIsAllowedToCreateFile(String userId) {
        if (!tutorRepository.existsById(userId) && !studentRepository.existsById(userId)) {
            throw new UserNotAllowedToCreateFileException();
        }
    }
}

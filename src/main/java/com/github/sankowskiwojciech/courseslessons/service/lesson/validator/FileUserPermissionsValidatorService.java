package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

public interface FileUserPermissionsValidatorService {
    void validateIfUserIsAllowedToCreateFile(String userId);

    void validateIfUserIsAllowedToAccessFile(String userId, String fileId);
}

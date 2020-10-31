package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

public interface FileAccessPermissionValidatorService {
    void validateIfUserIsAllowedToCreateFile(String userId);

    void validateIfUserIsAllowedToAccessFile(String userId, long fileId);
}

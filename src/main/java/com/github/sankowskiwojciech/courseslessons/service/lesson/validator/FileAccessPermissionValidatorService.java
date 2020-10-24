package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

public interface FileAccessPermissionValidatorService {
    void validateIfUserIsAllowedToCreateFile(String userId);

    void validateIfUserIsAllowedToReadFile(String userId, long fileId);
}

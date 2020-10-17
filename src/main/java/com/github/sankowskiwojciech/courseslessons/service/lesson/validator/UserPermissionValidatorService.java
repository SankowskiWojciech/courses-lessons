package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

public interface UserPermissionValidatorService {
    void validateIfUserIsAllowedToCreateFile(String userId);
}

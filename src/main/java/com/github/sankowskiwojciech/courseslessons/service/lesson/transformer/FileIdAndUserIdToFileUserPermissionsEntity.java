package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntityId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileIdAndUserIdToFileUserPermissionsEntity implements BiFunction<String, String, FileUserPermissionsEntity> {
    private static final FileIdAndUserIdToFileUserPermissionsEntity INSTANCE = new FileIdAndUserIdToFileUserPermissionsEntity();

    @Override
    public FileUserPermissionsEntity apply(String fileId, String userId) {
        return new FileUserPermissionsEntity(new FileUserPermissionsEntityId(fileId, userId), true, true, true);
    }

    public static FileIdAndUserIdToFileUserPermissionsEntity getInstance() {
        return INSTANCE;
    }
}

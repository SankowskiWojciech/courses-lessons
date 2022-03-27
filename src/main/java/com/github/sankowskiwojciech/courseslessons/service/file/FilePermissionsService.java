package com.github.sankowskiwojciech.courseslessons.service.file;

import java.util.Collection;
import java.util.Set;

public interface FilePermissionsService {
    void addUserPermissionsToFile(String userId, String fileId);

    void addUserPermissionsToFiles(String userId, Collection<String> filesIds);

    Set<String> readIdsOfFilesToWhichUserHasAccess(String userId);
}

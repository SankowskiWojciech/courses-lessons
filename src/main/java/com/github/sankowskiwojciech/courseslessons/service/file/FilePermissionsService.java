package com.github.sankowskiwojciech.courseslessons.service.file;

import java.util.List;
import java.util.Set;

public interface FilePermissionsService {
    void addUserPermissionsToFile(String userId, String fileId);

    void addUserPermissionsToFiles(String userId, List<String> filesIds);

    Set<String> readIdsOfFilesToWhichUserHasAccess(String userId);
}

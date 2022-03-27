package com.github.sankowskiwojciech.courseslessons.service.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileUserPermissionsRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileUserPermissionsEntityId;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilePermissionsServiceImpl implements FilePermissionsService {
    private final FileUserPermissionsRepository fileUserPermissionsRepository;

    @Override
    public void addUserPermissionsToFile(String userId, String fileId) {
        FileUserPermissionsEntityId id = new FileUserPermissionsEntityId(userId, fileId);
        if (!fileUserPermissionsRepository.existsById(id)) {
            FileUserPermissionsEntity entity = new FileUserPermissionsEntity(id, true, true, true);
            fileUserPermissionsRepository.save(entity);
        }
    }

    @Override
    public void addUserPermissionsToFiles(String userId, List<String> filesIds) {
        filesIds.forEach(fileId -> addUserPermissionsToFile(userId, fileId));
    }

    @Override
    public Set<String> readIdsOfFilesToWhichUserHasAccess(String userId) {
        return fileUserPermissionsRepository.findAllByFileUserPermissionsEntityIdUserIdAndCanReadIsTrue(userId).stream()
                .map(entity -> entity.getFileUserPermissionsEntityId().getFileId())
                .collect(Collectors.toSet());
    }
}

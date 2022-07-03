package com.github.sankowskiwojciech.courseslessons.service.group;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.GroupRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.group.GroupResponse;
import com.github.sankowskiwojciech.courseslessons.service.group.transformer.GroupEntityToGroupResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;

    @Override
    public List<GroupResponse> readGroups(String subdomainAlias, String tutorEmailAddress) {
        List<GroupEntity> groupEntities = groupRepository.findAllByTutorEntityEmailAddressAndSubdomainEntitySubdomainId(tutorEmailAddress, subdomainAlias);
        return groupEntities.stream()
                .map(entity -> GroupEntityToGroupResponse.getInstance().apply(entity))
                .collect(Collectors.toList());
    }
}

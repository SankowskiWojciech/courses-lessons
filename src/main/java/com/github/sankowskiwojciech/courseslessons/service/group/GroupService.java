package com.github.sankowskiwojciech.courseslessons.service.group;

import com.github.sankowskiwojciech.coursescorelib.model.group.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> readGroups(String subdomainAlias, String tutorEmailAddress);
}

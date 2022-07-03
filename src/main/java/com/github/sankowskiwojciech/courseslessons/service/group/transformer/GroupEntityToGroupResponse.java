package com.github.sankowskiwojciech.courseslessons.service.group.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.group.GroupResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupEntityToGroupResponse implements Function<GroupEntity, GroupResponse> {
    private static final GroupEntityToGroupResponse INSTANCE = new GroupEntityToGroupResponse();

    @Override
    public GroupResponse apply(GroupEntity entity) {
        return new GroupResponse(entity.getId(), entity.getName());
    }

    public static GroupEntityToGroupResponse getInstance() {
        return INSTANCE;
    }
}

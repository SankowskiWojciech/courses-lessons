package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.GROUP_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.GROUP_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.GROUP_NAME_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentsGroupEntityStub {

    public static GroupEntity create() {
        return GroupEntity.builder()
                .id(GROUP_ID_STUB)
                .name(GROUP_NAME_STUB)
                .description(GROUP_DESCRIPTION_STUB)
                .creationDateTime(LocalDateTime.now().minusMonths(1))
                .subdomainEntity(SubdomainEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .build();
    }

    public static GroupEntity createWithTutorEntity(TutorEntity tutor) {
        return GroupEntity.builder()
                .id(GROUP_ID_STUB)
                .name(GROUP_NAME_STUB)
                .description(GROUP_DESCRIPTION_STUB)
                .creationDateTime(LocalDateTime.now().minusMonths(1))
                .subdomainEntity(SubdomainEntityStub.create())
                .tutorEntity(tutor)
                .build();
    }
}

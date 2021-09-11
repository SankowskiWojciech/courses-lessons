package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.StudentsGroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentsGroupEntityStub {

    public static StudentsGroupEntity create() {
        return StudentsGroupEntity.builder()
                .groupId(GROUP_ID_STUB)
                .name(GROUP_NAME_STUB)
                .description(GROUP_DESCRIPTION_STUB)
                .creationDateTime(LocalDateTime.now().minusMonths(1))
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(TutorEntityStub.create())
                .build();
    }

    public static StudentsGroupEntity createWithTutorEntity(TutorEntity tutorEntity) {
        return StudentsGroupEntity.builder()
                .groupId(GROUP_ID_STUB)
                .name(GROUP_NAME_STUB)
                .description(GROUP_DESCRIPTION_STUB)
                .creationDateTime(LocalDateTime.now().minusMonths(1))
                .organizationEntity(OrganizationEntityStub.create())
                .tutorEntity(tutorEntity)
                .build();
    }
}

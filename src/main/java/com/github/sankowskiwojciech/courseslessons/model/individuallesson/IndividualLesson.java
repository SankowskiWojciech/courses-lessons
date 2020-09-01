package com.github.sankowskiwojciech.courseslessons.model.individuallesson;

import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class IndividualLesson {
    private String title;
    private LocalDateTime startDateOfLesson;
    private LocalDateTime endDateOfLesson;
    private String description;
    private OrganizationEntity organizationEntity;
    private TutorEntity tutorEntity;
    private StudentEntity studentEntity;
}

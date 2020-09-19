package com.github.sankowskiwojciech.courseslessons.model.individuallesson;

import com.github.sankowskiwojciech.courseslessons.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.courseslessons.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.courseslessons.model.lesson.ScheduleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class IndividualLessonsSchedule {
    private LocalDate beginningDate;
    private LocalDate endDate;
    private ScheduleType scheduleType;
    private Long allLessonsDurationInMinutes;
    private List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes;
    private List<String> lessonTitles;
    private OrganizationEntity organizationEntity;
    private TutorEntity tutorEntity;
    private StudentEntity studentEntity;
}

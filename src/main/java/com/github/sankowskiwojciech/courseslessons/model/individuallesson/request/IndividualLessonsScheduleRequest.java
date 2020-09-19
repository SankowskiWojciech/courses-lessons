package com.github.sankowskiwojciech.courseslessons.model.individuallesson.request;

import com.github.sankowskiwojciech.courseslessons.model.lesson.DayOfWeekWithTimes;
import com.github.sankowskiwojciech.courseslessons.model.lesson.ScheduleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class IndividualLessonsScheduleRequest {
    private LocalDate beginningDate;
    private LocalDate endDate;
    private ScheduleType scheduleType;
    private Long allLessonsDurationInMinutes;
    private List<DayOfWeekWithTimes> lessonsDaysOfWeekWithTimes;
    private List<String> lessonTitles;
    private String subdomainName;
    private String tutorId;
    private String studentId;
}

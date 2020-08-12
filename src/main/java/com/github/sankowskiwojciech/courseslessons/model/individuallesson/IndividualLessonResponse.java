package com.github.sankowskiwojciech.courseslessons.model.individuallesson;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class IndividualLessonResponse {
    private String title;
    private LocalDateTime dateOfLesson;
    private String description;
    private String subdomainName;
    private String tutorId;
    private String studentId;
}

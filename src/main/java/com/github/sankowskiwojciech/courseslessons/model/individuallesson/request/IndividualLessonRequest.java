package com.github.sankowskiwojciech.courseslessons.model.individuallesson.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class IndividualLessonRequest {
    private String title;
    private LocalDateTime dateOfLesson;
    private String description;
    private String subdomainName;
    private String tutorId;
    private String studentId;
}

package com.github.sankowskiwojciech.courseslessons.model.individuallesson.request;

import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class IndividualLessonRequestParams {
    private final Subdomain subdomain;
    private final LocalDateTime fromDateTime;
    private final LocalDateTime toDateTime;
}

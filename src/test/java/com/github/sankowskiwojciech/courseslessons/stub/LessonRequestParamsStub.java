package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonRequestParamsStub {

    public static LessonRequestParams create() {
        return new LessonRequestParams(SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION), LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }
}

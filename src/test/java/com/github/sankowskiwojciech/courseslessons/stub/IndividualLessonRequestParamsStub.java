package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequestParams;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestParamsStub {

    public static IndividualLessonRequestParams create() {
        return new IndividualLessonRequestParams(SubdomainStub.createWithSubdomainType(SubdomainType.ORGANIZATION), LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(1));
    }
}

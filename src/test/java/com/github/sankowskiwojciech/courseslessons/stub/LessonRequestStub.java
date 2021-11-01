package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILES_IDS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.SUBDOMAIN_ALIAS_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonRequestStub {
    public static LessonRequest create() {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new IndividualLessonRequest(LESSON_TITLE_STUB, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, FILES_IDS_STUB, STUDENT_EMAIL_ADDRESS_STUB);
    }
}

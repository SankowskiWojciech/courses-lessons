package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestStub {

    public static IndividualLessonRequest createWithDatesOfLesson(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson) {
        return new IndividualLessonRequest(LESSON_TITLE_STUB, startDateOfLesson, endDateOfLesson, LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, TUTOR_EMAIL_ADDRESS_STUB, null, STUDENT_EMAIL_ADDRESS_STUB);
    }

    public static IndividualLessonRequest createWithTitle(String title) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new IndividualLessonRequest(title, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, TUTOR_EMAIL_ADDRESS_STUB, null, STUDENT_EMAIL_ADDRESS_STUB);
    }

    public static IndividualLessonRequest create() {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new IndividualLessonRequest(LESSON_TITLE_STUB, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, TUTOR_EMAIL_ADDRESS_STUB, FILES_IDS_STUB, STUDENT_EMAIL_ADDRESS_STUB);
    }
}

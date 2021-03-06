package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILES_IDS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.SUBDOMAIN_NAME_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestStub {

    public static IndividualLessonRequest createWithDatesOfLesson(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson) {
        return IndividualLessonRequest.builder()
                .title(LESSON_TITLE_STUB)
                .startDateOfLesson(startDateOfLesson)
                .endDateOfLesson(endDateOfLesson)
                .description(LESSON_DESCRIPTION_STUB)
                .subdomainName(SUBDOMAIN_NAME_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }

    public static IndividualLessonRequest createWithTitle(String title) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return IndividualLessonRequest.builder()
                .title(title)
                .startDateOfLesson(currentDateTime)
                .endDateOfLesson(currentDateTime.plusHours(2))
                .description(LESSON_DESCRIPTION_STUB)
                .subdomainName(SUBDOMAIN_NAME_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }

    public static IndividualLessonRequest create() {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return IndividualLessonRequest.builder()
                .title(LESSON_TITLE_STUB)
                .startDateOfLesson(currentDateTime)
                .endDateOfLesson(currentDateTime.plusHours(2))
                .description(LESSON_DESCRIPTION_STUB)
                .subdomainName(SUBDOMAIN_NAME_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .filesIds(FILES_IDS_STUB)
                .build();
    }
}

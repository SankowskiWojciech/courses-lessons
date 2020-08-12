package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.SUBDOMAIN_NAME_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IndividualLessonRequestStub {

    public static IndividualLessonRequest createWithDateOfLesson(LocalDateTime dateOfLesson) {
        return IndividualLessonRequest.builder()
                .title(LESSON_TITLE_STUB)
                .dateOfLesson(dateOfLesson)
                .description(LESSON_DESCRIPTION_STUB)
                .subdomainName(SUBDOMAIN_NAME_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }

    public static IndividualLessonRequest createWithTitle(String title) {
        return IndividualLessonRequest.builder()
                .title(title)
                .dateOfLesson(LocalDateTime.now())
                .description(LESSON_DESCRIPTION_STUB)
                .subdomainName(SUBDOMAIN_NAME_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }

    public static IndividualLessonRequest create() {
        return IndividualLessonRequest.builder()
                .title(LESSON_TITLE_STUB)
                .dateOfLesson(LocalDateTime.now())
                .description(LESSON_DESCRIPTION_STUB)
                .subdomainName(SUBDOMAIN_NAME_STUB)
                .tutorId(TUTOR_EMAIL_ADDRESS_STUB)
                .studentId(STUDENT_EMAIL_ADDRESS_STUB)
                .build();
    }
}

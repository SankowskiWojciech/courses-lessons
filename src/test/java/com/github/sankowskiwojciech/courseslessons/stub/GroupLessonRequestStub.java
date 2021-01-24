package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonRequestStub {

    public static GroupLessonRequest createWithDatesOfLesson(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson) {
        return new GroupLessonRequest(LESSON_TITLE_STUB, startDateOfLesson, endDateOfLesson, LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, TUTOR_EMAIL_ADDRESS_STUB, null, GROUP_ID_STUB);
    }

    public static GroupLessonRequest createWithTitle(String title) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLessonRequest(title, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, TUTOR_EMAIL_ADDRESS_STUB, null, GROUP_ID_STUB);
    }

    public static GroupLessonRequest create() {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLessonRequest(LESSON_TITLE_STUB, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, TUTOR_EMAIL_ADDRESS_STUB, FILES_IDS_STUB, GROUP_ID_STUB);
    }
}
package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.request.GroupLessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILES_IDS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.GROUP_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.SUBDOMAIN_ALIAS_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonRequestStub {
    public static GroupLessonRequest createWithDatesOfLesson(LocalDateTime startDateOfLesson, LocalDateTime endDateOfLesson) {
        return new GroupLessonRequest(LESSON_TITLE_STUB, startDateOfLesson, endDateOfLesson, LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, null, GROUP_ID_STUB);
    }

    public static GroupLessonRequest createWithTitle(String title) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLessonRequest(title, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, null, GROUP_ID_STUB);
    }

    public static GroupLessonRequest createWithSubdomainAlias(String subdomainAlias) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLessonRequest(LESSON_TITLE_STUB, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, subdomainAlias, FILES_IDS_STUB, GROUP_ID_STUB);
    }

    public static GroupLessonRequest createWithGroupId(String groupId) {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLessonRequest(LESSON_TITLE_STUB, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, FILES_IDS_STUB, groupId);
    }

    public static GroupLessonRequest create() {
        final LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLessonRequest(LESSON_TITLE_STUB, currentDateTime, currentDateTime.plusHours(2), LESSON_DESCRIPTION_STUB, SUBDOMAIN_ALIAS_STUB, FILES_IDS_STUB, GROUP_ID_STUB);
    }
}

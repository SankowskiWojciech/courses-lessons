package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILES_IDS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_DESCRIPTION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.LESSON_TITLE_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GroupLessonStub {
    public static GroupLesson createWithExternalEntities(SubdomainEntity subdomainEntity, TutorEntity tutorEntity, GroupEntity groupEntity) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLesson(LESSON_TITLE_STUB, currentDateTime.plusHours(1), currentDateTime.plusHours(3), LESSON_DESCRIPTION_STUB, subdomainEntity, tutorEntity, FILES_IDS_STUB, groupEntity);
    }

    public static GroupLesson createWithFilesIds(List<String> filesIds) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return new GroupLesson(LESSON_TITLE_STUB, currentDateTime.plusHours(1), currentDateTime.plusHours(3), LESSON_DESCRIPTION_STUB, SubdomainEntityStub.create(), TutorEntityStub.create(), filesIds, GroupEntityStub.create());
    }
}

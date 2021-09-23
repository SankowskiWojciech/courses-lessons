package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonRequestAndExternalEntitiesToLessonImpl {

    public static Lesson transform(LessonRequest request, OrganizationEntity organization, TutorEntity tutor) {
        return new Lesson(request.getTitle(), request.getStartDate(), request.getEndDate(), request.getDescription(), organization, tutor, request.getFilesIds());
    }
}

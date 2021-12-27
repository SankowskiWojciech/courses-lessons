package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.Lesson;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonRequestAndExternalEntitiesToLesson {
    public static Lesson transform(LessonRequest request, SubdomainEntity subdomain, TutorEntity tutor) {
        return new Lesson(request.getTitle(), request.getStartDate(), request.getEndDate(), request.getDescription(), subdomain, tutor, request.getFilesIds());
    }
}

package com.github.sankowskiwojciech.courseslessons.service.grouplesson;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;

import java.util.List;

public interface GroupLessonService {
    GroupLessonResponse createGroupLesson(GroupLesson lesson);

    List<GroupLessonResponse> readGroupLessons(AccountInfo accountInfo, LessonRequestParams requestParams);
}

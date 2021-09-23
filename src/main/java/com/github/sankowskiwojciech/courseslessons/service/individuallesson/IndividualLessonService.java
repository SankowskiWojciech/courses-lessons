package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;

import java.util.List;

public interface IndividualLessonService {

    IndividualLessonResponse createIndividualLesson(IndividualLesson lesson);

    List<IndividualLessonResponse> readIndividualLessons(AccountInfo accountInfo, LessonRequestParams requestParams);
}

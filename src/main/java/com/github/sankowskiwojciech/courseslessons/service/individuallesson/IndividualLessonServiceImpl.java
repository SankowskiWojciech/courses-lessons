package com.github.sankowskiwojciech.courseslessons.service.individuallesson;

import com.github.sankowskiwojciech.courseslessons.backend.repository.IndividualLessonRepository;
import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLesson;
import com.github.sankowskiwojciech.courseslessons.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonEntityToIndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer.IndividualLessonToIndividualLessonEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class IndividualLessonServiceImpl implements IndividualLessonService {

    private final IndividualLessonRepository individualLessonRepository;

    @Override
    public IndividualLessonResponse createIndividualLesson(IndividualLesson individualLesson) {
        IndividualLessonEntity individualLessonEntity = IndividualLessonToIndividualLessonEntity.getInstance().apply(individualLesson);
        individualLessonRepository.save(individualLessonEntity);
        return IndividualLessonEntityToIndividualLessonResponse.getInstance().apply(individualLessonEntity);
    }
}

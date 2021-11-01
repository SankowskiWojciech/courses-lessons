package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.IndividualLessonResponse;
import com.github.sankowskiwojciech.courseslessons.stub.IndividualLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileWithoutContentStub;
import com.github.sankowskiwojciech.courseslessons.stub.StudentEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.TutorEntityStub;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponseTest {
    private final IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse testee = IndividualLessonEntityAndLessonFilesWithoutContentToIndividualLessonResponse.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        IndividualLessonEntity individualLessonEntityStub = IndividualLessonEntityStub.createWithExternalEntities(SubdomainEntityStub.create(), TutorEntityStub.create(), StudentEntityStub.create());
        List<FileWithoutContent> lessonFilesWithoutContent = Lists.newArrayList(
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString()),
                LessonFileWithoutContentStub.createWithFileId(UUID.randomUUID().toString())
        );

        //when
        IndividualLessonResponse individualLessonResponse = testee.apply(individualLessonEntityStub, lessonFilesWithoutContent);

        //then
        assertNotNull(individualLessonResponse);
        assertEquals(individualLessonEntityStub.getId(), individualLessonResponse.getId());
        assertEquals(individualLessonEntityStub.getTitle(), individualLessonResponse.getTitle());
        assertEquals(individualLessonEntityStub.getStartDate(), individualLessonResponse.getStartDate());
        assertEquals(individualLessonEntityStub.getEndDate(), individualLessonResponse.getEndDate());
        assertEquals(individualLessonEntityStub.getDescription(), individualLessonResponse.getDescription());
        assertEquals(individualLessonEntityStub.getSubdomainEntity().getSubdomainId(), individualLessonResponse.getSubdomainAlias());
        assertEquals(individualLessonEntityStub.getTutorEntity().getEmailAddress(), individualLessonResponse.getTutorEmailAddress());
        assertNotNull(individualLessonResponse.getStudentFullName());
        assertEquals(individualLessonEntityStub.getStudentEntity().getEmailAddress(), individualLessonResponse.getStudentEmailAddress());
        assertEquals(lessonFilesWithoutContent.size(), individualLessonResponse.getFilesInformation().size());
    }
}
package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLessonResponse;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.stub.GroupLessonEntityStub;
import com.github.sankowskiwojciech.courseslessons.stub.LessonFileWithoutContentStub;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponseTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        GroupLessonEntity lessonStub = GroupLessonEntityStub.create();
        List<FileWithoutContent> filesWithoutContentStub = Collections.singletonList(LessonFileWithoutContentStub.create());

        //when
        GroupLessonResponse response = GroupLessonEntityAndLessonFilesWithoutContentToGroupLessonResponse.getInstance().apply(lessonStub, filesWithoutContentStub);

        //then
        assertNotNull(response);
        assertEquals(response.getId(), lessonStub.getId());
        assertEquals(response.getTitle(), lessonStub.getTitle());
        assertEquals(response.getStartDate(), lessonStub.getStartDate());
        assertEquals(response.getEndDate(), lessonStub.getEndDate());
        assertEquals(response.getDescription(), lessonStub.getDescription());
        assertEquals(response.getSubdomainAlias(), lessonStub.getSubdomainEntity().getSubdomainId());
        assertEquals(response.getTutorEmailAddress(), lessonStub.getTutorEntity().getEmailAddress());
        assertEquals(response.getGroupName(), lessonStub.getGroupEntity().getName());
        List<LessonFileResponse> filesResponse = response.getFilesInformation();
        assertEquals(filesWithoutContentStub.size(), filesResponse.size());
    }
}
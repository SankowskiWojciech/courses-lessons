package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.group.GroupEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.grouplesson.GroupLesson;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.GroupLessonStub;
import com.github.sankowskiwojciech.coursestestlib.stub.SubdomainEntityStub;
import com.github.sankowskiwojciech.coursestestlib.stub.TutorEntityStub;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GroupLessonToGroupLessonEntityTest {
    @Test
    public void shouldTransformCorrectly() {
        //given
        SubdomainEntity subdomainStub = SubdomainEntityStub.create();
        TutorEntity tutorStub = TutorEntityStub.create();
        GroupEntity groupStub = GroupEntityStub.create();
        GroupLesson lessonStub = GroupLessonStub.createWithExternalEntities(subdomainStub, tutorStub, groupStub);

        //when
        GroupLessonEntity entity = GroupLessonToGroupLessonEntity.getInstance().apply(lessonStub);

        //then
        assertNotNull(entity);
        assertEquals(lessonStub.getTitle(), entity.getTitle());
        assertEquals(lessonStub.getStartDate(), entity.getStartDate());
        assertEquals(lessonStub.getEndDate(), entity.getEndDate());
        assertEquals(lessonStub.getDescription(), entity.getDescription());
        assertEquals(lessonStub.getSubdomainEntity(), entity.getSubdomainEntity());
        assertEquals(lessonStub.getTutorEntity(), entity.getTutorEntity());
        assertEquals(lessonStub.getGroupEntity(), entity.getGroupEntity());
        assertNotNull(entity.getCreationDateTime());
    }
}
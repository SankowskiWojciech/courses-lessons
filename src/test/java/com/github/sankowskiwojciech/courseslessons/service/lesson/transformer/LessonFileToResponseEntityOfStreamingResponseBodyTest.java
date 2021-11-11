package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursestestlib.stub.LessonFileStub;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LessonFileToResponseEntityOfStreamingResponseBodyTest {

    private final LessonFileToResponseEntityOfStreamingResponseBody testee = LessonFileToResponseEntityOfStreamingResponseBody.getInstance();

    @Test
    public void shouldTransformCorrectly() {
        //given
        LessonFile fileStub = LessonFileStub.create();

        //when
        ResponseEntity<StreamingResponseBody> streamingResponseBodyResponseEntity = testee.apply(fileStub);

        //then
        assertNotNull(streamingResponseBodyResponseEntity);
        assertEquals(HttpStatus.OK, streamingResponseBodyResponseEntity.getStatusCode());
        assertTrue(streamingResponseBodyResponseEntity.getHeaders().containsKey(HttpHeaders.CONTENT_DISPOSITION));
        assertNotNull(streamingResponseBodyResponseEntity.getBody());
    }
}
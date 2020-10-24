package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileToResponseEntityOfStreamingResponseBody implements Function<LessonFile, ResponseEntity<StreamingResponseBody>> {

    private static final LessonFileToResponseEntityOfStreamingResponseBody INSTANCE = new LessonFileToResponseEntityOfStreamingResponseBody();

    private static final String CONTENT_DISPOSITION_VALUE_TEMPLATE = "attachment; filename=%s";

    @Override
    public ResponseEntity<StreamingResponseBody> apply(LessonFile lessonFile) {
        StreamingResponseBody streamingResponseBody = outputStream -> FileCopyUtils.copy(lessonFile.getContent(), outputStream);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_VALUE_TEMPLATE, lessonFile.getName()))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(streamingResponseBody);
    }

    public static LessonFileToResponseEntityOfStreamingResponseBody getInstance() {
        return INSTANCE;
    }
}

package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_CONTENT_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_CREATED_BY_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_CREATION_DATE_TIME_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_EXTENSION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_NAME_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileStub {
    public static LessonFile create() {
        return LessonFile.builder()
                .fileId(FILE_ID_STUB)
                .name(FILE_NAME_STUB)
                .extension(FILE_EXTENSION_STUB)
                .content(FILE_CONTENT_STUB)
                .createdBy(FILE_CREATED_BY_STUB)
                .creationDateTime(FILE_CREATION_DATE_TIME_STUB)
                .build();
    }
}

package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.lessonfile.LessonFileWithoutContent;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_CREATED_BY_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_CREATION_DATE_TIME_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_EXTENSION_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_ID_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_NAME_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LessonFileWithoutContentStub {

    public static LessonFileWithoutContent create() {
        return new LessonFileWithoutContent(FILE_ID_STUB, FILE_NAME_STUB, FILE_EXTENSION_STUB, FILE_CREATED_BY_STUB, FILE_CREATION_DATE_TIME_STUB);
    }
}

package com.github.sankowskiwojciech.courseslessons.stub;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_CONTENT_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_CONTENT_TYPE_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.FILE_NAME_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipartFileStub {
    public static MultipartFile create() {
        return new MockMultipartFile(FILE_NAME_STUB, FILE_NAME_STUB, FILE_CONTENT_TYPE_STUB, FILE_CONTENT_STUB);
    }
}

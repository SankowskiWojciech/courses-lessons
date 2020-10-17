package com.github.sankowskiwojciech.courseslessons.service.lesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.exception.file.FileCorruptedException;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipartFileToLessonFile implements Function<MultipartFile, LessonFile> {

    private static final MultipartFileToLessonFile INSTANCE = new MultipartFileToLessonFile();

    @Override
    public LessonFile apply(MultipartFile file) {
        return LessonFile.builder()
                .name(file.getOriginalFilename())
                .extension(FilenameUtils.getExtension(file.getOriginalFilename()))
                .content(getFileContent(file))
                .build();
    }

    private byte[] getFileContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new FileCorruptedException();
        }
    }

    public static MultipartFileToLessonFile getInstance() {
        return INSTANCE;
    }
}

package com.github.sankowskiwojciech.courseslessons.service.lesson.validator;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.LessonFileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.exception.file.*;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.MultipartFileToLessonFile;
import lombok.AllArgsConstructor;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@AllArgsConstructor
public class LessonFileValidatorServiceImpl implements LessonFileValidatorService {

    private final Detector detector;
    private final Set<String> validFileMIMETypes;
    private final LessonFileRepository lessonFileRepository;

    @Override
    public LessonFile validateUploadedFile(MultipartFile file) {
        if (!validFileMIMETypes.contains(getFileMediaType(file))) {
            throw new InvalidFileFormatException();
        }
        return MultipartFileToLessonFile.getInstance().apply(file);
    }

    @Override
    public void validateIfFileExists(String fileId) {
        if (!lessonFileRepository.existsById(fileId)) {
            throw new FileNotFoundException();
        }
    }

    private String getFileMediaType(MultipartFile file) {
        try {
            TikaInputStream tikaInputStream = TikaInputStream.get(file.getInputStream());
            Metadata metadata = new Metadata();
            metadata.add(Metadata.RESOURCE_NAME_KEY, file.getOriginalFilename());
            return detector.detect(tikaInputStream, metadata).toString();
        } catch (IOException e) {
            throw new FileCorruptedException();
        }
    }
}
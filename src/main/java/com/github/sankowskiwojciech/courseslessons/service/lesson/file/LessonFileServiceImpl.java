package com.github.sankowskiwojciech.courseslessons.service.lesson.file;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.FileRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.file.FileWithoutContent;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFile;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.LessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileAndUserIdToLessonFileEntity;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileEntityToLessonFile;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileEntityToLessonFileResponse;
import com.github.sankowskiwojciech.courseslessons.service.lesson.transformer.LessonFileWithoutContentToLessonFileResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonFileServiceImpl implements LessonFileService {

    private final FileRepository fileRepository;

    @Transactional
    @Override
    public LessonFileResponse createLessonFile(LessonFile lessonFile, String userId) {
        FileEntity fileEntity = LessonFileAndUserIdToLessonFileEntity.getInstance().apply(lessonFile, userId);
        FileEntity savedFileEntity = fileRepository.save(fileEntity);
        return LessonFileEntityToLessonFileResponse.getInstance().apply(savedFileEntity);
    }

    @Override
    public LessonFile readLessonFile(String fileId) {
        FileEntity fileEntity = fileRepository.findById(fileId).get();
        return LessonFileEntityToLessonFile.getInstance().apply(fileEntity);
    }

    @Override
    public List<LessonFileResponse> readFilesInformation(String fileOwnerId) {
        List<FileWithoutContent> lessonFilesWithoutContent = fileRepository.findAllByCreatedBy(fileOwnerId);
        return lessonFilesWithoutContent.stream().map(lessonFileWithoutContent -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(lessonFileWithoutContent)).collect(Collectors.toList());
    }
}

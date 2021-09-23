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
    public LessonFileResponse createLessonFile(LessonFile file, String userId) {
        FileEntity entity = LessonFileAndUserIdToLessonFileEntity.getInstance().apply(file, userId);
        FileEntity savedEntity = fileRepository.save(entity);
        return LessonFileEntityToLessonFileResponse.getInstance().apply(savedEntity);
    }

    @Override
    public LessonFile readLessonFile(String fileId) {
        FileEntity entity = fileRepository.findById(fileId).get();
        return LessonFileEntityToLessonFile.getInstance().apply(entity);
    }

    @Override
    public List<LessonFileResponse> readFilesInformation(String fileOwnerId) {
        List<FileWithoutContent> filesWithoutContent = fileRepository.findAllByCreatedBy(fileOwnerId);
        return filesWithoutContent.stream().map(fileWithoutContent -> LessonFileWithoutContentToLessonFileResponse.getInstance().apply(fileWithoutContent)).collect(Collectors.toList());
    }
}

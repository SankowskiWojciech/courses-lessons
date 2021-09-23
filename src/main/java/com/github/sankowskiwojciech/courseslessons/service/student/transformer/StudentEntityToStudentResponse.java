package com.github.sankowskiwojciech.courseslessons.service.student.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.student.StudentResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentEntityToStudentResponse implements Function<StudentEntity, StudentResponse> {

    private static final StudentEntityToStudentResponse INSTANCE = new StudentEntityToStudentResponse();

    @Override
    public StudentResponse apply(StudentEntity entity) {
        return new StudentResponse(entity.getFullName(), entity.getEmailAddress());
    }

    public static StudentEntityToStudentResponse getInstance() {
        return INSTANCE;
    }
}

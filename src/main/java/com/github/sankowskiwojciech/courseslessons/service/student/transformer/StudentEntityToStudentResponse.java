package com.github.sankowskiwojciech.courseslessons.service.student.transformer;

import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.courseslessons.model.student.StudentResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentEntityToStudentResponse implements Function<StudentEntity, StudentResponse> {

    private static final StudentEntityToStudentResponse INSTANCE = new StudentEntityToStudentResponse();

    @Override
    public StudentResponse apply(StudentEntity studentEntity) {
        return new StudentResponse(studentEntity.getFullName(), studentEntity.getEmailAddress());
    }

    public static StudentEntityToStudentResponse getInstance() {
        return INSTANCE;
    }
}

package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.courseslessons.model.db.student.StudentEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_EMAIL_ADDRESS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_FIRST_NAME_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.STUDENT_LAST_NAME_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StudentEntityStub {

    public static StudentEntity create() {
        return StudentEntity.builder()
                .emailAddress(STUDENT_EMAIL_ADDRESS_STUB)
                .firstName(STUDENT_FIRST_NAME_STUB)
                .lastName(STUDENT_LAST_NAME_STUB)
                .build();
    }
}

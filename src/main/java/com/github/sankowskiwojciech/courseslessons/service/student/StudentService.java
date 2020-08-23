package com.github.sankowskiwojciech.courseslessons.service.student;

import com.github.sankowskiwojciech.courseslessons.model.student.StudentResponse;

import java.util.List;

public interface StudentService {

    List<StudentResponse> readStudents(String subdomainEmailAddress, String tutorEmailAddress);
}

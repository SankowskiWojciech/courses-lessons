package com.github.sankowskiwojciech.courseslessons.model.student;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StudentResponse {
    private String fullName;
    private String emailAddress;
}

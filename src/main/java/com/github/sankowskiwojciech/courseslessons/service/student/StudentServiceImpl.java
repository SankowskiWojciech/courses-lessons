package com.github.sankowskiwojciech.courseslessons.service.student;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.StudentRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.SubdomainUserAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.StudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomainuseraccess.SubdomainUserAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.student.StudentResponse;
import com.github.sankowskiwojciech.courseslessons.service.student.transformer.StudentEntityToStudentResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final SubdomainUserAccessRepository subdomainUserAccessRepository;
    private final StudentRepository studentRepository;

    @Override
    public List<StudentResponse> readStudents(String subdomainEmailAddress, String tutorEmailAddress) {
        List<SubdomainUserAccessEntity> subdomainUserAccessEntities = subdomainUserAccessRepository.findAllBySubdomainEmailAddressAndUserEmailAddressIsNot(subdomainEmailAddress, tutorEmailAddress);
        List<String> usersEmailAddresses = subdomainUserAccessEntities.stream()
                .map(SubdomainUserAccessEntity::getUserEmailAddress)
                .collect(Collectors.toList());
        List<StudentEntity> studentEntities = studentRepository.findAllById(usersEmailAddresses);
        return studentEntities.stream()
                .map(studentEntity -> StudentEntityToStudentResponse.getInstance().apply(studentEntity))
                .collect(Collectors.toList());
    }
}

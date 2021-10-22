package com.github.sankowskiwojciech.courseslessons.controller.student;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountType;
import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessStudentsInformationException;
import com.github.sankowskiwojciech.coursescorelib.model.student.StudentResponse;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.service.student.StudentService;
import com.github.sankowskiwojciech.courseslessons.service.tokenvalidation.TokenValidationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/students")
public class StudentController {
    private final TokenValidationService tokenValidationService;
    private final SubdomainService subdomainService;
    private final StudentService studentService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<StudentResponse> readStudents(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestParam(value = "subdomainAlias") String subdomainAlias) {
        TokenEntity token = tokenValidationService.validateToken(authorizationHeaderValue);
        validateIfUserIsTutor(token);
        Subdomain subdomain = subdomainService.validateIfUserHasAccessToSubdomain(subdomainAlias, token.getUserEmailAddress());
        return studentService.readStudents(subdomain.getAlias(), token.getUserEmailAddress());
    }

    private void validateIfUserIsTutor(TokenEntity tokenEntity) {
        if (!AccountType.TUTOR.equals(tokenEntity.getAccountType())) {
            throw new UserNotAllowedToAccessStudentsInformationException();
        }
    }
}

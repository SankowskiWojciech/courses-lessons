package com.github.sankowskiwojciech.courseslessons.controller.student;

import com.github.sankowskiwojciech.courseslessons.controller.validator.SubdomainAndUserAccessValidator;
import com.github.sankowskiwojciech.courseslessons.model.account.AccountType;
import com.github.sankowskiwojciech.courseslessons.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.courseslessons.model.exception.UserNotAllowedToReadInformationAboutStudents;
import com.github.sankowskiwojciech.courseslessons.model.student.StudentResponse;
import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
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
    private final SubdomainAndUserAccessValidator subdomainAndUserAccessValidator;
    private final StudentService studentService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<StudentResponse> readIndividualLessons(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestParam(value = "subdomainName") String subdomainName) {
        TokenEntity tokenEntity = tokenValidationService.validateToken(authorizationHeaderValue);
        validateIfUserIsTutor(tokenEntity);
        Subdomain subdomain = subdomainAndUserAccessValidator.apply(subdomainName, tokenEntity.getUserEmailAddress());
        return studentService.readStudents(subdomain.getEmailAddress(), tokenEntity.getUserEmailAddress());
    }

    private void validateIfUserIsTutor(TokenEntity tokenEntity) {
        if (!AccountType.TUTOR.equals(tokenEntity.getAccountType())) {
            throw new UserNotAllowedToReadInformationAboutStudents();
        }
    }
}

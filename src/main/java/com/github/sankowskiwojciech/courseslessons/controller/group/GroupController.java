package com.github.sankowskiwojciech.courseslessons.controller.group;

import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessGroupsInformationException;
import com.github.sankowskiwojciech.coursescorelib.model.group.GroupResponse;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.service.subdomain.SubdomainService;
import com.github.sankowskiwojciech.courseslessons.controller.validator.AccountValidator;
import com.github.sankowskiwojciech.courseslessons.service.group.GroupService;
import com.github.sankowskiwojciech.courseslessons.service.tokenvalidation.TokenValidationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final TokenValidationService tokenValidationService;
    private final SubdomainService subdomainService;
    private final GroupService groupService;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public List<GroupResponse> readGroups(@RequestHeader(value = "Authorization") String authorizationHeaderValue, @RequestParam(value = "subdomainAlias") String subdomainAlias) {
        TokenEntity token = tokenValidationService.validateToken(authorizationHeaderValue);
        AccountValidator.validateIfUserIsTutor(token, new UserNotAllowedToAccessGroupsInformationException());
        Subdomain subdomain = subdomainService.validateIfUserHasAccessToSubdomain(subdomainAlias, token.getUserEmailAddress());
        return groupService.readGroups(subdomain.getAlias(), token.getUserEmailAddress());
    }
}

package com.github.sankowskiwojciech.courseslessons.controller.validator;

import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.SubdomainService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;

@AllArgsConstructor
public class SubdomainAndUserAccessValidator implements BiFunction<String, String, Subdomain> {

    private final SubdomainService subdomainService;

    @Override
    public Subdomain apply(String subdomainAlias, String userEmailAddress) {
        if (StringUtils.isNotBlank(subdomainAlias)) {
            Subdomain subdomain = subdomainService.readSubdomainInformationIfSubdomainExists(subdomainAlias);
            subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), userEmailAddress);
            return subdomain;
        }
        return null;
    }
}

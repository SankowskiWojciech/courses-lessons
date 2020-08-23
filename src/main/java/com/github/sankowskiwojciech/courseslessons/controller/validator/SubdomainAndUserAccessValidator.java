package com.github.sankowskiwojciech.courseslessons.controller.validator;

import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.SubdomainService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;

@AllArgsConstructor
public class SubdomainAndUserAccessValidator implements BiFunction<String, String, Subdomain> {

    private final SubdomainService subdomainService;

    @Override
    public Subdomain apply(String subdomainName, String userEmailAddress) {
        if (StringUtils.isNotBlank(subdomainName)) {
            Subdomain subdomain = subdomainService.readSubdomainInformationIfSubdomainExists(subdomainName);
            subdomainService.validateIfUserIsAllowedToAccessSubdomain(subdomain.getEmailAddress(), userEmailAddress);
            return subdomain;
        }
        return null;
    }
}

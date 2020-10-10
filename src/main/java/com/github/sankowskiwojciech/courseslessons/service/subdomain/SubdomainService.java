package com.github.sankowskiwojciech.courseslessons.service.subdomain;

import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;

public interface SubdomainService {

    Subdomain readSubdomainInformationIfSubdomainExists(String subdomainAlias);

    void validateIfUserIsAllowedToAccessSubdomain(String subdomainEmailAddress, String userEmailAddress);
}

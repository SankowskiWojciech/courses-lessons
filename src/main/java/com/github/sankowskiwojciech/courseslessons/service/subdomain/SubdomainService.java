package com.github.sankowskiwojciech.courseslessons.service.subdomain;

import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;

public interface SubdomainService {

    Subdomain readSubdomainInformationIfSubdomainExists(String subdomainName);

    void validateIfUserIsAllowedToAccessSubdomain(String subdomainEmailAddress, String userEmailAddress);
}

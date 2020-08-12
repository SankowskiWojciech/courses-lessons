package com.github.sankowskiwojciech.courseslessons.service.subdomain;

import com.github.sankowskiwojciech.courseslessons.model.subdomain.Subdomain;

public interface SubdomainService {

    Subdomain readSubdomainInformationIfSubdomainExists(String subdomainName);

    void validateIfUserIsAllowedToAccessSubdomain(String subdomainEmailAddress, String userEmailAddress);
}

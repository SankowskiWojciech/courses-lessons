package com.github.sankowskiwojciech.courseslessons.service.subdomain;

import com.github.sankowskiwojciech.coursescorelib.backend.repository.OrganizationRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.SubdomainUserAccessRepository;
import com.github.sankowskiwojciech.coursescorelib.backend.repository.TutorRepository;
import com.github.sankowskiwojciech.coursescorelib.model.db.organization.OrganizationEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomainuseraccess.SubdomainUserAccessEntityId;
import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.SubdomainNotFoundException;
import com.github.sankowskiwojciech.coursescorelib.model.exception.permission.UserNotAllowedToAccessSubdomainException;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.transformer.OrganizationEntityToSubdomain;
import com.github.sankowskiwojciech.courseslessons.service.subdomain.transformer.TutorEntityToSubdomain;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class SubdomainServiceImpl implements SubdomainService {

    private final OrganizationRepository organizationRepository;
    private final TutorRepository tutorRepository;
    private final SubdomainUserAccessRepository subdomainUserAccessRepository;

    @Override
    public Subdomain readSubdomainInformationIfSubdomainExists(String subdomainName) {
        if (StringUtils.isBlank(subdomainName)) {
            throw new SubdomainNotFoundException();
        }
        Optional<OrganizationEntity> organizationEntity = organizationRepository.findByAlias(subdomainName);
        if (organizationEntity.isPresent()) {
            return OrganizationEntityToSubdomain.getInstance().apply(organizationEntity.get());
        }
        Optional<TutorEntity> tutorEntity = tutorRepository.findByAlias(subdomainName);
        if (tutorEntity.isPresent()) {
            return TutorEntityToSubdomain.getInstance().apply(tutorEntity.get());
        }
        throw new SubdomainNotFoundException();
    }

    @Override
    public void validateIfUserIsAllowedToAccessSubdomain(String subdomainEmailAddress, String userEmailAddress) {
        if (!subdomainUserAccessRepository.existsById(new SubdomainUserAccessEntityId(subdomainEmailAddress, userEmailAddress))) {
            throw new UserNotAllowedToAccessSubdomainException();
        }
    }
}

package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.subdomainuseraccess.SubdomainUserAccessEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.subdomainuseraccess.SubdomainUserAccessEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubdomainUserAccessRepository extends JpaRepository<SubdomainUserAccessEntity, SubdomainUserAccessEntityId> {

    List<SubdomainUserAccessEntity> findAllBySubdomainEmailAddressAndUserEmailAddressIsNot(String subdomainEmailAddress, String tutorEmailAddress);
}

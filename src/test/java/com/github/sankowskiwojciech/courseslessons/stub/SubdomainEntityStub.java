package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.subdomain.SubdomainUserAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.SUBDOMAIN_ALIAS_STUB;
import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.TUTOR_EMAIL_ADDRESS_STUB;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubdomainEntityStub {
    public static SubdomainEntity create() {
        Set<SubdomainUserAccessEntity> subdomainUserAccessEntities = new HashSet<>();
        subdomainUserAccessEntities.add(SubdomainUserAccessEntityStub.create(SUBDOMAIN_ALIAS_STUB, TUTOR_EMAIL_ADDRESS_STUB));
        return new SubdomainEntity(SUBDOMAIN_ALIAS_STUB, SubdomainType.ORGANIZATION, subdomainUserAccessEntities);
    }
}

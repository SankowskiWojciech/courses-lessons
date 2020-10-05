package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.subdomainuseraccess.SubdomainUserAccessEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubdomainUserAccessEntityStub {

    public static SubdomainUserAccessEntity create(String subdomainEmailAddress, String userEmailAddress) {
        return new SubdomainUserAccessEntity(subdomainEmailAddress, userEmailAddress);
    }
}

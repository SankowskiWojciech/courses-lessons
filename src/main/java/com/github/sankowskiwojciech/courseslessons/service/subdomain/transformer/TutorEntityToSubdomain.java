package com.github.sankowskiwojciech.courseslessons.service.subdomain.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TutorEntityToSubdomain implements Function<TutorEntity, Subdomain> {

    private final static TutorEntityToSubdomain INSTANCE = new TutorEntityToSubdomain();

    private final static String TUTOR_FIRST_NAME_LAST_NAME_DELIMITER = " ";

    @Override
    public Subdomain apply(TutorEntity tutorEntity) {
        return Subdomain.builder()
                .name(prepareSubdomainName(tutorEntity.getFirstName(), tutorEntity.getLastName()))
                .alias(tutorEntity.getAlias())
                .description(tutorEntity.getDescription())
                .emailAddress(tutorEntity.getEmailAddress())
                .phoneNumber(tutorEntity.getPhoneNumber())
                .subdomainType(SubdomainType.TUTOR)
                .build();
    }

    public static TutorEntityToSubdomain getInstance() {
        return INSTANCE;
    }

    private String prepareSubdomainName(String firstName, String lastName) {
        return String.join(TUTOR_FIRST_NAME_LAST_NAME_DELIMITER, firstName, lastName);
    }
}

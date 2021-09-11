package com.github.sankowskiwojciech.courseslessons.stub;

import com.github.sankowskiwojciech.coursescorelib.model.db.tutor.TutorEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.github.sankowskiwojciech.courseslessons.DefaultTestValues.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TutorEntityStub {

    public static TutorEntity create() {
        return createWithTutorId(TUTOR_EMAIL_ADDRESS_STUB);
    }

    public static TutorEntity createWithTutorId(String tutorId) {
        return TutorEntity.builder()
                .emailAddress(tutorId)
                .alias(TUTOR_ALIAS_STUB)
                .firstName(TUTOR_FIRST_NAME_STUB)
                .lastName(TUTOR_LAST_NAME_STUB)
                .description(TUTOR_DESCRIPTION_STUB)
                .phoneNumber(TUTOR_PHONE_NUMBER_STUB)
                .build();
    }
}

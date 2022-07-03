package com.github.sankowskiwojciech.courseslessons.controller.validator;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountType;
import com.github.sankowskiwojciech.coursescorelib.model.db.token.TokenEntity;
import com.github.sankowskiwojciech.coursescorelib.model.exception.DetailedException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountValidator {
    public static void validateIfUserIsTutor(TokenEntity tokenEntity, DetailedException exception) {
        if (!AccountType.TUTOR.equals(tokenEntity.getAccountType())) {
            throw exception;
        }
    }
}

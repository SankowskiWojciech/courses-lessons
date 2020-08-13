package com.github.sankowskiwojciech.courseslessons.model.account;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class AccountInfo {
    private final String userEmailAddress;
    private final AccountType accountType;
}

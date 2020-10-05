package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountType;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.QIndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.individuallesson.request.IndividualLessonRequestParams;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.SubdomainType;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountInfoAndIndividualLessonRequestParamsToBooleanExpression implements BiFunction<AccountInfo, IndividualLessonRequestParams, BooleanExpression> {

    private static final AccountInfoAndIndividualLessonRequestParamsToBooleanExpression INSTANCE = new AccountInfoAndIndividualLessonRequestParamsToBooleanExpression();

    @Override
    public BooleanExpression apply(AccountInfo accountInfo, IndividualLessonRequestParams individualLessonRequestParams) {
        QIndividualLessonEntity individualLessonEntity = QIndividualLessonEntity.individualLessonEntity;
        return accountQuery(individualLessonEntity, accountInfo)
                .and(subdomainQuery(individualLessonEntity, individualLessonRequestParams.getSubdomain()))
                .and(fromDateTimeQuery(individualLessonEntity, individualLessonRequestParams.getFromDateTime()))
                .and(toDateTimeQuery(individualLessonEntity, individualLessonRequestParams.getToDateTime()));
    }

    private BooleanExpression accountQuery(QIndividualLessonEntity individualLessonEntity, AccountInfo accountInfo) {
        if (AccountType.TUTOR.equals(accountInfo.getAccountType())) {
            return individualLessonEntity.tutorEntity.emailAddress.eq(accountInfo.getUserEmailAddress());
        }
        return individualLessonEntity.studentEntity.emailAddress.eq(accountInfo.getUserEmailAddress());
    }

    private BooleanExpression subdomainQuery(QIndividualLessonEntity individualLessonEntity, Subdomain subdomain) {
        if (subdomain == null) {
            return null;
        }
        if (SubdomainType.ORGANIZATION.equals(subdomain.getSubdomainType())) {
            return individualLessonEntity.organizationEntity.emailAddress.eq(subdomain.getEmailAddress());
        }
        return individualLessonEntity.organizationEntity.isNull();
    }

    private BooleanExpression fromDateTimeQuery(QIndividualLessonEntity individualLessonEntity, LocalDateTime fromDateTime) {
        if (fromDateTime != null) {
            return individualLessonEntity.startDateOfLesson.eq(fromDateTime).or(individualLessonEntity.startDateOfLesson.after(fromDateTime));
        }
        return null;
    }

    private BooleanExpression toDateTimeQuery(QIndividualLessonEntity individualLessonEntity, LocalDateTime toDateTime) {
        if (toDateTime != null) {
            return individualLessonEntity.endDateOfLesson.before(toDateTime).or(individualLessonEntity.endDateOfLesson.eq(toDateTime));
        }
        return null;
    }

    public static AccountInfoAndIndividualLessonRequestParamsToBooleanExpression getInstance() {
        return INSTANCE;
    }
}

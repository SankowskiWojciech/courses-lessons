package com.github.sankowskiwojciech.courseslessons.service.individuallesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountType;
import com.github.sankowskiwojciech.coursescorelib.model.db.individuallesson.QIndividualLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.function.BiFunction;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountInfoAndIndividualLessonRequestParamsToBooleanExpression implements BiFunction<AccountInfo, LessonRequestParams, BooleanExpression> {

    private static final AccountInfoAndIndividualLessonRequestParamsToBooleanExpression INSTANCE = new AccountInfoAndIndividualLessonRequestParamsToBooleanExpression();

    @Override
    public BooleanExpression apply(AccountInfo accountInfo, LessonRequestParams lessonRequestParams) {
        QIndividualLessonEntity individualLessonEntity = QIndividualLessonEntity.individualLessonEntity;
        return accountQuery(individualLessonEntity, accountInfo)
                .and(subdomainQuery(individualLessonEntity, lessonRequestParams.getSubdomain()))
                .and(fromDateTimeQuery(individualLessonEntity, lessonRequestParams.getFromDateTime()))
                .and(toDateTimeQuery(individualLessonEntity, lessonRequestParams.getToDateTime()));
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
        return individualLessonEntity.subdomainEntity.subdomainId.eq(subdomain.getAlias());
    }

    private BooleanExpression fromDateTimeQuery(QIndividualLessonEntity individualLessonEntity, LocalDateTime fromDateTime) {
        if (fromDateTime != null) {
            return individualLessonEntity.startDate.eq(fromDateTime).or(individualLessonEntity.startDate.after(fromDateTime));
        }
        return null;
    }

    private BooleanExpression toDateTimeQuery(QIndividualLessonEntity individualLessonEntity, LocalDateTime toDateTime) {
        if (toDateTime != null) {
            return individualLessonEntity.endDate.before(toDateTime).or(individualLessonEntity.endDate.eq(toDateTime));
        }
        return null;
    }

    public static AccountInfoAndIndividualLessonRequestParamsToBooleanExpression getInstance() {
        return INSTANCE;
    }
}

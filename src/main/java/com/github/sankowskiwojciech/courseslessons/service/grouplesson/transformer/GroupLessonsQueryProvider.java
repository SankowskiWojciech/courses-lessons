package com.github.sankowskiwojciech.courseslessons.service.grouplesson.transformer;

import com.github.sankowskiwojciech.coursescorelib.model.account.AccountInfo;
import com.github.sankowskiwojciech.coursescorelib.model.account.AccountType;
import com.github.sankowskiwojciech.coursescorelib.model.db.group.QStudentGroupAccessEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.GroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.grouplesson.QGroupLessonEntity;
import com.github.sankowskiwojciech.coursescorelib.model.db.student.QStudentEntity;
import com.github.sankowskiwojciech.coursescorelib.model.lesson.request.LessonRequestParams;
import com.github.sankowskiwojciech.coursescorelib.model.subdomain.Subdomain;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.function.BiFunction;

public class GroupLessonsQueryProvider implements BiFunction<AccountInfo, LessonRequestParams, BooleanExpression> {
    private final JPAQueryFactory queryFactory;

    public GroupLessonsQueryProvider(EntityManager entityManager) {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public BooleanExpression apply(AccountInfo accountInfo, LessonRequestParams lessonRequestParams) {
        QGroupLessonEntity groupLessonEntity = QGroupLessonEntity.groupLessonEntity;
        return accountQuery(groupLessonEntity, accountInfo)
                .and(subdomainQuery(groupLessonEntity, lessonRequestParams.getSubdomain()))
                .and(fromDateTimeQuery(groupLessonEntity, lessonRequestParams.getFromDateTime()))
                .and(toDateTimeQuery(groupLessonEntity, lessonRequestParams.getToDateTime()));
    }

    private BooleanExpression accountQuery(QGroupLessonEntity groupLessonEntity, AccountInfo accountInfo) {
        if (AccountType.TUTOR.equals(accountInfo.getAccountType())) {
            return groupLessonEntity.tutorEntity.emailAddress.eq(accountInfo.getUserEmailAddress());
        }
        QStudentGroupAccessEntity studentGroupAccessEntity = QStudentGroupAccessEntity.studentGroupAccessEntity;
        QStudentEntity studentEntity = QStudentEntity.studentEntity;
        JPAQuery<GroupLessonEntity> queryWhichSelectsLessonsForStudent = queryFactory.selectFrom(groupLessonEntity)
                .join(studentGroupAccessEntity)
                .on(groupLessonEntity.groupEntity.id.eq(studentGroupAccessEntity.groupEntity.id))
                .join(studentEntity)
                .on(studentGroupAccessEntity.studentEntity.emailAddress.eq(studentEntity.emailAddress))
                .where(studentEntity.emailAddress.eq(accountInfo.getUserEmailAddress()));
        return groupLessonEntity.in(queryWhichSelectsLessonsForStudent);
    }

    private BooleanExpression subdomainQuery(QGroupLessonEntity groupLessonEntity, Subdomain subdomain) {
        if (subdomain == null) {
            return null;
        }
        return groupLessonEntity.subdomainEntity.subdomainId.eq(subdomain.getAlias());
    }

    private BooleanExpression fromDateTimeQuery(QGroupLessonEntity groupLessonEntity, LocalDateTime fromDateTime) {
        if (fromDateTime != null) {
            return groupLessonEntity.startDate.eq(fromDateTime).or(groupLessonEntity.startDate.after(fromDateTime));
        }
        return null;
    }

    private BooleanExpression toDateTimeQuery(QGroupLessonEntity groupLessonEntity, LocalDateTime toDateTime) {
        if (toDateTime != null) {
            return groupLessonEntity.endDate.before(toDateTime).or(groupLessonEntity.endDate.eq(toDateTime));
        }
        return null;
    }

}

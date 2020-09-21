package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IndividualLessonRepository extends JpaRepository<IndividualLessonEntity, Long>, QuerydslPredicateExecutor {

    @Query("SELECT individualLesson FROM IndividualLessonEntity individualLesson WHERE :startDateOfLesson < individualLesson.endDateOfLesson AND :endDateOfLesson > individualLesson.startDateOfLesson AND individualLesson.tutorEntity.emailAddress = :tutorEmailAddress AND individualLesson.organizationEntity.emailAddress = :organizationEmailAddress")
    List<IndividualLessonEntity> findAllLessonsWhichCanCollideWithNewLesson(@Param("startDateOfLesson") LocalDateTime startDateOfLesson, @Param("endDateOfLesson") LocalDateTime endDateOfLesson, @Param("tutorEmailAddress") String tutorEmailAddress, @Param("organizationEmailAddress") String organizationEmailAddress);

    @Query("SELECT individualLesson FROM IndividualLessonEntity individualLesson WHERE individualLesson.startDateOfLesson >= :startDateOfLesson AND individualLesson.endDateOfLesson <= :endDateOfLesson AND individualLesson.tutorEntity.emailAddress = :tutorEmailAddress AND individualLesson.organizationEntity.emailAddress = :organizationEmailAddress")
    List<IndividualLessonEntity> findAllLessonsInRangeForTutor(@Param("startDateOfLesson") LocalDateTime startDateOfLesson, @Param("endDateOfLesson") LocalDateTime endDateOfLesson, @Param("tutorEmailAddress") String tutorEmailAddress, @Param("organizationEmailAddress") String organizationEmailAddress);
}

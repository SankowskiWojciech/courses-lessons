package com.github.sankowskiwojciech.courseslessons.model.db.student;

import com.github.sankowskiwojciech.courseslessons.model.db.individuallesson.IndividualLessonEntity;
import com.github.sankowskiwojciech.courseslessons.model.db.parent.ParentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "STUDENT")
@EqualsAndHashCode(exclude = "individualLessons")
public class StudentEntity {

    private static final String FIRST_NAME_LAST_NAME_DIVIDER = " ";

    @Id
    @Column(name = "EMAIL_ADDRESS", length = 50, unique = true, nullable = false, updatable = false)
    private String emailAddress;

    @Column(name = "FIRST_NAME", length = 15, nullable = false)
    private String firstName;

    @Column(name = "LAST_NAME", length = 30, nullable = false)
    private String lastName;

    @Column(name = "PHONE_NUMBER", length = 9)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private ParentEntity parent;

    @OneToMany(mappedBy = "studentEntity")
    private Set<IndividualLessonEntity> individualLessons;

    public String getFullName() {
        return firstName.concat(FIRST_NAME_LAST_NAME_DIVIDER).concat(lastName);
    }
}
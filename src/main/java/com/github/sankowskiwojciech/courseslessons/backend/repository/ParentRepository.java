package com.github.sankowskiwojciech.courseslessons.backend.repository;

import com.github.sankowskiwojciech.courseslessons.model.db.parent.ParentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends JpaRepository<ParentEntity, String> {
}

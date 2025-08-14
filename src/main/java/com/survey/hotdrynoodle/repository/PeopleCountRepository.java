package com.survey.hotdrynoodle.repository;

import com.survey.hotdrynoodle.entity.PeopleCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PeopleCountRepository extends JpaRepository<PeopleCount, Long> {
    List<PeopleCount> findAllByDate(LocalDate date);
}

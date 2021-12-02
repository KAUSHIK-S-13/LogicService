package com.coherent.unnamed.logic.repository;

import com.coherent.unnamed.logic.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRepository  extends JpaRepository<Attendance, Long> {

    @Query(value = "select * from attendance gets where year(gets.created_at) = :year and month(gets.created_at) = :month", nativeQuery = true)
    List<Attendance> findAllByCreatedAt(int year, int month);

    @Query(value = "select * from attendance gets where date(gets.created_at) = :date and gets.user_id_fk = :user_id_fk", nativeQuery = true)
    Attendance findByCreatedAtDate(int date, int user_id_fk);

}

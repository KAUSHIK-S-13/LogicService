package com.coherent.unnamed.logic.repository;

import com.coherent.unnamed.logic.model.TimeLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TimeLogsRepository  extends JpaRepository<TimeLogs, Long> {

    @Query(value = "select * from time_logs gets where date(gets.created_at) = :date and gets.user_id_fk = :userId", nativeQuery = true)
    List<TimeLogs> findAllByCreatedAtDate(int date, int userId);

    @Query(value = "select * from time_logs gets where date(gets.created_at) = :strDate and gets.user_id_fk = :user_id order by gets.user_id_fk asc", nativeQuery = true)
    List<TimeLogs> findByUserId(String strDate, int user_id);


}

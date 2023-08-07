package com.agendaCraft.agendaCraft.repository;


import com.agendaCraft.agendaCraft.domain.Task;
import com.agendaCraft.agendaCraft.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findAllByUserId(Long userId);

    //@Query("SELECT t FROM Task t WHERE t.customPropertyName = :userId") if i wanted like other name different from my field entity
   // List<Task> findByCustomPropertyName(@Param("userId") Long userId);

}
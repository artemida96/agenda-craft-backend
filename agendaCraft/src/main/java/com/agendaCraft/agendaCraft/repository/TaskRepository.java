package com.agendaCraft.agendaCraft.repository;


import com.agendaCraft.agendaCraft.domain.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
}
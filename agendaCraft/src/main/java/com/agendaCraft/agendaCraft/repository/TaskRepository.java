package com.agendaCraft.agendaCraft.repository;

import com.agendaCraft.agendaCraft.domain.Task;
import com.agendaCraft.agendaCraft.domain.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
}
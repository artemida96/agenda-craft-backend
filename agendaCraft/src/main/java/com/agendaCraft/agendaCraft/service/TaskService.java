package com.agendaCraft.agendaCraft.service;
import com.agendaCraft.agendaCraft.domain.Task;

import java.util.List;

public interface TaskService {
    Task create(Task task);

    Task edit(Long id, Task task);
    List<Task> getAllTasks();
}
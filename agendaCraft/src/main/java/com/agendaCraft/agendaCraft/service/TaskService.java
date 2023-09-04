package com.agendaCraft.agendaCraft.service;
import com.agendaCraft.agendaCraft.domain.Task;

import java.util.List;
import java.util.Map;

public interface TaskService {
    Task create(Task task);

    Task edit(Long id, Task task);

    boolean delete(Long id);
    List<Task> getAllTasks();

}
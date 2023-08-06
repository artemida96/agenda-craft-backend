package com.agendaCraft.agendaCraft.service;

import com.agendaCraft.agendaCraft.domain.Task;
import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.repository.TaskRepository;
import com.agendaCraft.agendaCraft.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Task create(@Valid Task  task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if(Objects.nonNull(user)){
            task.setAssignedUserId(user.getId()); //assign task to the owner by token
        }
        task.setDateCreated(new Date());
        return taskRepository.save(task);
    }

    @Override
    public Task edit(Long taskId, @Valid Task updatedTask) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);
        if (Objects.nonNull(existingTask)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (Objects.nonNull(user) && user.getId().equals(existingTask.getAssignedUserId())) {
                existingTask.setName(updatedTask.getName());
                existingTask.setDueDate(updatedTask.getDueDate());
                return taskRepository.save(existingTask);
            }
        }
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
}
package com.agendaCraft.agendaCraft.controller;

import com.agendaCraft.agendaCraft.domain.Task;

import com.agendaCraft.agendaCraft.service.TaskServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/tasks")
public class TaskController

{
    @Autowired
    private TaskServiceImpl taskServiceImpl;

    @PostMapping("/task")
    public ResponseEntity<Object> create(@Valid @RequestBody Task taskDto){
        Task task = taskServiceImpl.create(taskDto);
        if (Objects.nonNull(task)){
            return ResponseEntity.status(HttpStatus.CREATED).body(task);
        }
        return ResponseEntity.badRequest().body("Task could not be created");
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<Object> edit(@PathVariable Long taskId, @Valid @RequestBody Task updatedTask) {
        Task editedTask = taskServiceImpl.edit(taskId, updatedTask);
        if (Objects.nonNull(editedTask)) {
            return ResponseEntity.ok(editedTask);
        }
        return ResponseEntity.notFound().build();
    }

    @RequestMapping
    public ResponseEntity<List<Task>> getTasksForCurrentUser(@RequestParam Map<String, String> filters) {
        List<Task> tasks;
        if (!filters.isEmpty()) {
            tasks = taskServiceImpl.getTasksWithDynamicFilters(filters);
        } else {
            tasks = taskServiceImpl.getAllTasks();
        }
        return ResponseEntity.ok(tasks);
    }
    //implement a single method that handles both filtered and
    // unfiltered requests by checking for
    // the presence of filters in the query parameters.
    //maintain code flexibility while optimizing query execution based on the use case.
}

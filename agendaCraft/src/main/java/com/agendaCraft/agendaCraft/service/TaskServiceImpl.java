package com.agendaCraft.agendaCraft.service;

import com.agendaCraft.agendaCraft.domain.Task;
import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.enums.EnumStatus;
import com.agendaCraft.agendaCraft.repository.TaskRepository;
import com.agendaCraft.agendaCraft.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.*;

@Service
public class TaskServiceImpl implements TaskService {

    @PersistenceContext
    EntityManager entityManager;
    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    private final UserServiceImpl userServiceImpl;




    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, UserServiceImpl userServiceImpl ) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Task create(@Valid Task  task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if(Objects.nonNull(user)){
            task.setUserId(user.getId()); //assign task to the owner by token
        }
        task.setDateCreated(new Date());
        task.setStatus(EnumStatus.DRAFT);
        return taskRepository.save(task);
    }

    @Override
    public Task edit(Long taskId, @Valid Task updatedTask) {
        Task existingTask = taskRepository.findById(taskId).orElse(null);
        if (Objects.nonNull(existingTask)) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if (Objects.nonNull(user) && user.getId().equals(existingTask.getUserId())) {
                existingTask.setName(updatedTask.getName());
                existingTask.setDueDate(updatedTask.getDueDate());
                Date currentDate = new Date();
                if( currentDate.before( updatedTask.getDueDate())){
                    existingTask.setStatus(EnumStatus.EXPIRED);
                }
                return taskRepository.save(existingTask);
            }
        }
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        Optional<User> user = userServiceImpl.getCurrentUser();

        if (user.isPresent()) {
            return taskRepository.findAllByUserId(user.get().getId());
        } else {
            return Collections.emptyList();
        }
    }


    @Transactional
    public List<Task> getTasksWithDynamicFilters(Map<String, String> allFilters) {
        Optional<User> user = userServiceImpl.getCurrentUser();
        if(Objects.isNull(user)){
            return Collections.emptyList();
        }
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Task> query = cb.createQuery(Task.class);
        Root<Task> root = query.from(Task.class);

        Predicate predicate = cb.conjunction();

        if(user.isPresent()) {
            predicate = cb.and(predicate, cb.equal(root.get("userId"), user.get().getId()));


            for (Map.Entry<String, String> entry : allFilters.entrySet()) {
                String fieldName = entry.getKey();
                String filterValue = entry.getValue();

                Field field;
                try {
                    field = Task.class.getDeclaredField(fieldName);
                    field.setAccessible(true);

                    // Handle casting and filtering logic here
                    Predicate filterPredicate = createFilterPredicate(cb, root, field, filterValue);

                    predicate = cb.and(predicate, filterPredicate);
                } catch (NoSuchFieldException e) {
                    // Field doesn't exist, handle as needed
                }
            }

            query.select(root).where(predicate);

            return entityManager.createQuery(query).getResultList();
        }
        return Collections.emptyList();
    }

    private Predicate createFilterPredicate(CriteriaBuilder cb, Root<Task> root,
                                            Field field, String filterValue) {
        // Customize the logic to create a predicate based on the field and filter value
        // Handle casting, comparisons, and different data types
        if (field.getType() == Boolean.class) {
            Boolean value = Boolean.parseBoolean(filterValue);
            return cb.equal(root.get(field.getName()), value);
        } else if (field.getType() == Integer.class) {
            Integer value = Integer.parseInt(filterValue);
            return cb.equal(root.get(field.getName()), value);
        }
        // If field type is not recognized, return a default predicate
        return cb.conjunction();
    }
}
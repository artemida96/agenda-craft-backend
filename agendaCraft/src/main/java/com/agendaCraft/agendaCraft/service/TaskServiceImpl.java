package com.agendaCraft.agendaCraft.service;

import com.agendaCraft.agendaCraft.domain.Task;
import com.agendaCraft.agendaCraft.domain.User;
import com.agendaCraft.agendaCraft.enums.EnumTaskStatus;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        if(Objects.isNull(task.getStatus())) {
            task.setStatus(EnumTaskStatus.PENDING);
        }
        if(task.getDateCreated().after(task.getDueDate())){
            task.setStatus(EnumTaskStatus.CANCELED);
        }
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
                existingTask.setTitle(updatedTask.getTitle());
                existingTask.setDueDate(updatedTask.getDueDate());
                Date currentDate = new Date();
                if(Objects.isNull(updatedTask.getStatus())) {
                    updatedTask.setStatus(EnumTaskStatus.PENDING);
                }
                if(existingTask.getDateCreated().after(updatedTask.getDueDate())){
                    updatedTask.setStatus(EnumTaskStatus.EXPIRED);
                }
                return taskRepository.save(existingTask);
            }
        }
        return null;
    }

    @Override
    public boolean delete(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);
        if (optionalTask.isPresent() && Objects.nonNull(user)) {
            Task task = optionalTask.get();
            if (task.getUserId() != null && task.getUserId().equals(user.getId())) {
                taskRepository.delete(task);
                return true;
            }
        }
        return false;
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
        Class<?> fieldType = field.getType();

        if (fieldType == Boolean.class) {
            Boolean value = Boolean.parseBoolean(filterValue);
            return cb.equal(root.get(field.getName()), value);
        } else if (fieldType == Integer.class) {
            Integer value = Integer.parseInt(filterValue);
            return cb.equal(root.get(field.getName()), value);
        }
        else if (EnumTaskStatus.class.isAssignableFrom(fieldType)) {
            if (filterValue.equals("unCompleted")) {
                return cb.notEqual(root.get(field.getName()), EnumTaskStatus.COMPLETED);
            } else {
                EnumTaskStatus status = EnumTaskStatus.valueOf(filterValue); // Assuming filterValue matches enum values
                return cb.equal(root.get(field.getName()), status);
            }
        }
         else if (fieldType == Date.class) {
            try {
                Date filterDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(filterValue);

                // Calculate the start and end of the filter date
                Calendar startOfDay = Calendar.getInstance();
                startOfDay.setTime(filterDate);
                startOfDay.set(Calendar.HOUR_OF_DAY, 0);
                startOfDay.set(Calendar.MINUTE, 0);
                startOfDay.set(Calendar.SECOND, 0);
                startOfDay.set(Calendar.MILLISECOND, 0);

                Calendar endOfDay = Calendar.getInstance();
                endOfDay.setTime(filterDate);
                endOfDay.set(Calendar.HOUR_OF_DAY, 23);
                endOfDay.set(Calendar.MINUTE, 59);
                endOfDay.set(Calendar.SECOND, 59);
                endOfDay.set(Calendar.MILLISECOND, 999);

                // Create a predicate that checks for tasks created during the current day
                return cb.and(
                        cb.between(root.get("dateCreated"), startOfDay.getTime(), endOfDay.getTime())
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        // If field type is not recognized, return a default predicate
        return cb.conjunction();
    }
}
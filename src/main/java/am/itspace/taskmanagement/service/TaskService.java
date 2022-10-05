package am.itspace.taskmanagement.service;

import am.itspace.taskmanagement.entity.Role;
import am.itspace.taskmanagement.entity.Task;
import am.itspace.taskmanagement.entity.User;
import am.itspace.taskmanagement.repository.TaskRepository;
import am.itspace.taskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;


    public void saveNewTask(Task task) {
        if (task.getUser() != null && task.getUser().getId() == 0) {
            task.setUser(null);
        }
        taskRepository.save(task);
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Page<Task> findTasksByUserRole(User user, Pageable pageable) {
        return user.getRole() == Role.USER ?
                taskRepository.findAllByUser_Id(user.getId(), pageable)
                : taskRepository.findAll(pageable);
    }

    public void changeTaskUser(int userId, int taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        Optional<User> userOptional = userRepository.findById(userId);
        if (taskOptional.isPresent() && userOptional.isPresent()) {
            Task task = taskOptional.get();
            User user = userOptional.get();
            if (task.getUser() != user) {
                task.setUser(user);
                taskRepository.save(task);
            }
        } else if (taskOptional.isPresent() && userId == 0) {
            taskOptional.get().setUser(null);
            taskRepository.save(taskOptional.get());
        }
    }

}

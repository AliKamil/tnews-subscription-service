package tnews.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tnews.entity.Category;
import tnews.entity.Subscription;
import tnews.entity.User;
import tnews.entity.UserAction;
import tnews.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User create(User user) {
        return userRepository.findById(user.getId()).orElseGet(() -> userRepository.save(user));
    }

    public User update(Long id, User user) {
        user.setId(id);
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public void addCategory(Long id, String category) {
        Category categoryObj = new Category();
        categoryObj.setCategoryName(category);
        Subscription subscription = new Subscription();
        subscription.setId(id);
        subscription.setCategories(Set.of(categoryObj));
        User user = userRepository.findById(id).orElse(null);
        user.setSubscription(subscription);
        userRepository.save(user);
    }

    public User updateCurrentAction(Long id, String action) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User Not Found"));
        user.setCurrentAction(UserAction.valueOf(action));
        return userRepository.save(user);
    }

}

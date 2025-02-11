package tnews.subscription.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import subscription.entity.*;
import tnews.subscription.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import tnews.subscription.entity.Category;
import tnews.subscription.entity.KeyWord;
import tnews.subscription.entity.Subscription;
import tnews.subscription.entity.TimeInterval;
import tnews.subscription.entity.User;
import tnews.subscription.entity.UserAction;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private Long ownerId1;
    private Long ownerId2;
    private Long ownerId3;


    @BeforeEach
    public void setUp() {
        User user1 = new User(1L, "FirstName", null, LocalDateTime.now(),//TODO: хардкодить id не хорошо, нужно переделать!
                LocalDateTime.now(), UserAction.READY);
        User user2 = new User(2L, "SecondName", null, LocalDateTime.now(),
                LocalDateTime.now(), UserAction.READY);
        User user3 = new User(3L, "ThirdName", null, LocalDateTime.now(),
                LocalDateTime.now(), UserAction.READY);

        User newUser1 = userRepository.save(user1);
        User newUser2 = userRepository.save(user2);
        User newUser3 = userRepository.save(user3);

        ownerId1 = newUser1.getId();
        ownerId2 = newUser2.getId();
        ownerId3 = newUser3.getId();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteById(ownerId1);
        userRepository.deleteById(ownerId2);
        userRepository.deleteById(ownerId3);
    }

    @Test
    void create() {
        User newUser = new User(1L, "NewName", new Subscription(1L,
                Set.of(
                    new KeyWord(1L, "keyWord1", Set.of()),
                    new KeyWord(2L, "keyWord2", Set.of())),
                TimeInterval.ONE_HOUR,
                Set.of(
                        new Category(1L, "category1", Set.of()),
                        new Category(2L, "category2", Set.of())),
                LocalDateTime.now(),
                LocalDateTime.now()),
                LocalDateTime.now(),
                LocalDateTime.now(),
                UserAction.READY);

        User savedUser = userService.create(newUser);
        assertNotNull(savedUser);
        User retrieved = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(retrieved);
        assertEquals(savedUser.getId(), retrieved.getId());
        assertEquals(savedUser.getUsername(), retrieved.getUsername());
        assertEquals(savedUser.getCurrentAction(), retrieved.getCurrentAction());
        Long id = retrieved.getId();
        userService.delete(id);
        assertNull(userRepository.findById(id).orElse(null));
    }

    @Test
    void findAll() {
        List<User> users = userService.findAll();
        assertNotNull(users);
        List<User> retrievedUsers = userRepository.findAll();
        assertNotNull(retrievedUsers);
        assertEquals(users.size(), retrievedUsers.size());
        for (int i = 0; i < users.size(); i++) {
            assertEquals(users.get(i).getUsername(), retrievedUsers.get(i).getUsername());
            assertEquals(users.get(i).getCurrentAction(), retrievedUsers.get(i).getCurrentAction());
        }
    }

    @Test
    void findById() {
        User user = userService.findById(ownerId1);
        assertNotNull(user);
        User retrievedUser = userRepository.findById(ownerId1).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(user.getId(), retrievedUser.getId());
        assertEquals(user.getUsername(), retrievedUser.getUsername());
        assertEquals(user.getCurrentAction(), retrievedUser.getCurrentAction());
    }

    @Test
    void update() {
        User user = userService.findById(ownerId1);
        User replaceUser = new User(1L, "NEW_Name",null, LocalDateTime.now(),
                LocalDateTime.now(), UserAction.WAITING_FOR_KEYWORD);
        User updatedUser = userService.update(user.getId(), replaceUser);
        assertNotNull(updatedUser);
        assertEquals(user.getId(), updatedUser.getId());
        User retrievedUser = userRepository.findById(updatedUser.getId()).orElse(null);
        assertNotNull(retrievedUser);
        assertEquals(updatedUser.getId(), retrievedUser.getId());
        assertEquals(updatedUser.getUsername(), retrievedUser.getUsername());
        assertEquals(updatedUser.getCurrentAction(), retrievedUser.getCurrentAction());
    }

    @Test
    void delete() {
    }

    @Test
    void addCategory() {
    }

    @Test
    void updateCurrentAction() {
    }
}
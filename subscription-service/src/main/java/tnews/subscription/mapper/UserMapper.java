package tnews.subscription.mapper;

import org.telegram.telegrambots.meta.api.objects.Update;
import tnews.subscription.dto.UserCreateDTO;
import tnews.subscription.dto.UserResponseDTO;
import tnews.subscription.entity.Subscription;
import tnews.subscription.entity.TimeInterval;
import tnews.subscription.entity.User;


public class UserMapper {

    public static User fromDTO(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        Subscription newsSubscription = new Subscription();
        newsSubscription.setCategories(userCreateDTO.getCategories());
        newsSubscription.setKeyWords(userCreateDTO.getKeyWords());
        newsSubscription.setTimeInterval(TimeInterval.valueOf(userCreateDTO.getTimeInterval()));    //TODO: не обработано исключение IllegalArgumentException (если не совпадает с TimeInterval)
        user.setSubscription(newsSubscription);
        return user;
    }

    public static User fromDTO(UserResponseDTO responseDTO) {
        User user = new User();
        user.setUsername(responseDTO.getUsername());
        user.setId(responseDTO.getId());
        Subscription newsSubscription = new Subscription();
        newsSubscription.setId(responseDTO.getId());
        user.setSubscription(newsSubscription);
        return user;
    }
    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setId(user.getId());
        userDTO.setSubscriptionId(user.getSubscription().getId());
        return userDTO;
    }
    public static User toEntity(Update update) {
        User user = new User();
        user.setId(update.getMessage().getFrom().getId());
        user.setUsername(update.getMessage().getFrom().getFirstName());
        return user;
    }

}

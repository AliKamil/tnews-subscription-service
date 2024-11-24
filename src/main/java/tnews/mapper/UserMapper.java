package tnews.mapper;

import tnews.dto.UserCreateDTO;
import tnews.dto.UserResponseDTO;
import tnews.entity.Subscription;
import tnews.entity.User;


public class UserMapper {

    public static User fromDTO(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setUsername(userCreateDTO.getUsername());
        Subscription newsSubscription = new Subscription();
        newsSubscription.setCategories(userCreateDTO.getCategories());
        newsSubscription.setKeyWords(userCreateDTO.getKeyWords());
        newsSubscription.setTimeInterval(userCreateDTO.getTimeInterval());
        user.setSubscription(newsSubscription);
        return user;
    }

    public static User fromDTO(UserResponseDTO responseDTO) {
        User user = new User();
        user.setUsername(responseDTO.getUsername());
        user.setId(responseDTO.getId());
        user.setSubscription(responseDTO.getSubscription());
        return user;
    }
    public static UserResponseDTO toDTO(User user) {
        UserResponseDTO userDTO = new UserResponseDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setId(user.getId());
        userDTO.setSubscription(user.getSubscription());
        return userDTO;
    }

}

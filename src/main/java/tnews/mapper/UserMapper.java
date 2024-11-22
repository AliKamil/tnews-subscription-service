package tnews.mapper;

import tnews.dto.UserDTO;
import tnews.entity.User;


public class UserMapper {
    public static UserDTO toDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setCategories(user.getCategories());
        userDTO.setKeyWords(user.getKeyWords());
        userDTO.setTimeInterval(user.getTimeInterval());
        return userDTO;
    }

    public static User fromDTO(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setCategories(userDTO.getCategories());
        user.setKeyWords(userDTO.getKeyWords());
        user.setTimeInterval(userDTO.getTimeInterval());
        return user;
    }

}

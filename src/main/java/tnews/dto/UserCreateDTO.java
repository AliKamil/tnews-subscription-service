package tnews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tnews.entity.Category;
import tnews.entity.KeyWord;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String username;
    private Set<KeyWord> keyWords;
    private LocalDateTime timeInterval;
    private Set<Category> categories;
}

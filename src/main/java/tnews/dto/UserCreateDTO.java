package tnews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    private String username;
    private Set<String> keyWords;
    private LocalDateTime timeInterval;
    private Set<String> categories;
}

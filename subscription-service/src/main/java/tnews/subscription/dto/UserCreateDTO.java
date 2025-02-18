package tnews.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tnews.subscription.entity.Category;
import tnews.subscription.entity.KeyWord;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    @NotNull(message = "Username cannot be null")
    private String username;
    @NotNull(message = "At least one keyword is required for the search")
    private Set<KeyWord> keyWords;
    private String timeInterval;
    @NotNull(message = "At least one category is required for the search")
    private Set<Category> categories;
}

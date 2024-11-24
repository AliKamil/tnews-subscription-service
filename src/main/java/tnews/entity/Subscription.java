package tnews.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Subscription {
    private Set<String> keyWords;
    private User owner;
    private LocalDateTime timeInterval;
    private Set<String> categories;
}

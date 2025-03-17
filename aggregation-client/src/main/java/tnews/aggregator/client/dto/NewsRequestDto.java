package tnews.aggregator.client.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsRequestDto {
    private Set<String> categories;
    private Set<String> sendNewsIds;
    private int limit;
}

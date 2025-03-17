package tnews.aggregator.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tnews.aggregator.client.dto.NewsDto;
import tnews.aggregator.client.dto.NewsRequestDto;

@FeignClient(name = "aggregator" ,url = "${aggregator.service.url}")
public interface AggregatorClient {
    @GetMapping("/categories")
    List<String> getCategories();

    @GetMapping("{category}")
    List<NewsDto> getNewsByCategory(@PathVariable("category") String category);

    @PostMapping("/actual")
    List<NewsDto> getActualNews(@RequestBody NewsRequestDto request);
}

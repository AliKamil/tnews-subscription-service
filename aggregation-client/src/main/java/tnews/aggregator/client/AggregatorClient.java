package tnews.aggregator.client;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import tnews.aggregator.client.dto.NewsDto;

@FeignClient(name = "aggregator" ,url = "${aggregator.service.url}")
public interface AggregatorClient {
    @GetMapping("/categories")
    List<String> getCategories();

    @GetMapping("{category}")
    List<NewsDto> getNewsByCategory(@PathVariable("category") String category); //может передавать строки, а не обьекты?
}

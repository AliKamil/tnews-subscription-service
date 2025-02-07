package subscription.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import subscription.dto.NewsDto;

import java.util.List;

@FeignClient(name = "aggregator" ,url = "${aggregator.service.url}")
public interface AggregatorClient {
    @GetMapping("/categories")
    List<String> getCategories();

    @GetMapping("{category}")
    List<String> getNewsByCategory(@PathVariable("category") String category); //может передавать строки, а не обьекты?
}

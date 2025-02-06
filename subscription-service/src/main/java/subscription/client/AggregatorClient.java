package subscription.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "aggregator" ,url = "${aggregator.service.url}") //TODO: поправить ссылку под докер
public interface AggregatorClient {
    @GetMapping("/categories")
    List<String> getCategories();
}

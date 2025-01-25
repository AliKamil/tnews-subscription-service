package aggregator.service;

import aggregator.NewsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class NewsServiceTest {
   @Autowired
   private NewsService newsService;

    @Test
    public void addNews() {
        newsService.testMongo();
    }
}

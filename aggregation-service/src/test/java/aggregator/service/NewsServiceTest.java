package aggregator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsServiceTest {
   @Autowired
   private NewsService newsService;

    @Test
    void addNews() {
        newsService.testMongo();
    }
}

package tnews.aggregator.client.service;

import tnews.aggregator.client.repository.NewsRepository;
import tnews.aggregator.client.entity.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NewsServiceTest {
   @Autowired
   private NewsService newsService;

   @Autowired
   private NewsRepository newsRepository;

    @Test
    public void testMongo() {
        News news = new News();
        news.setTitle("Заголовок новости");
        news.setContent("Содержание новости");
        news = newsRepository.save(news);

        News savedNews = newsRepository.findById(news.getId()).orElse(null);
        if (savedNews != null) {
            System.out.println("Новость найдена: " + savedNews.getTitle());
            System.out.println("ID новости: " + news.getId());
        } else {
            System.out.println("Новость не найдена");
        }
    }

}

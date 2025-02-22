package aggregator.service;

import aggregator.entity.News;
import aggregator.repository.NewsRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class NewsServiceTest {


   @Autowired
   private NewsService newsService;

   @Autowired
   private NewsRepository newsRepository;

   @BeforeEach
   public void setUp() {
       newsRepository.deleteAll();

       News news1 = new News("1", "title_1", "content_1", "url_1", "date_1", "category_1");
       newsRepository.save(news1);
       News news2 = new News("2", "title_2", "content_2", "url_2", "date_2", "category_2");
       newsRepository.save(news2);
       News news3 = new News("3", "title_3", "content_3", "url_3", "date_3", "category_3");
       newsRepository.save(news3);

   }

   @AfterEach
   public void tearDown() {
        newsRepository.deleteAll();
   }

    @Test
    public void testMongo() {
        News news = new News();
        news.setTitle("Заголовок новости");
        news.setContent("Содержание новости");
        news.setUrl("Ссылка на новость");
        news.setPublishedAt("12345678");
        news.setCategory("Категория новости");
        news = newsRepository.save(news);

        News savedNews = newsRepository.findById(news.getId()).orElse(null);
        assertEquals(news.getId(), savedNews.getId());
        assertEquals(news.getTitle(), savedNews.getTitle());
        assertEquals(news.getContent(), savedNews.getContent());
        assertEquals(news.getUrl(), savedNews.getUrl());
        assertEquals(news.getPublishedAt(), savedNews.getPublishedAt());
        assertEquals(news.getCategory(), savedNews.getCategory());

    }

    @Test
    void fetchAndSaveNews() {
    }

    @Test
    void getCategories() {
       List<String> categories = newsService.getCategories();
       List<String> savedCategories = newsRepository.findDistinctCategories();
       assertNotNull(categories);
       assertNotNull(savedCategories);
       assertEquals(categories.size(), savedCategories.size());
       assertEquals(categories, savedCategories);

    }

    @Test
    void getNewsByCategory() {
       String category = newsService.getCategories().get(0);
       List<News> newsByCategory = newsService.getNewsByCategory(category);
       assertNotNull(newsByCategory);
       List<News> savedNewsByCategory = newsRepository.findByCategory(category);
       assertNotNull(savedNewsByCategory);
       assertEquals(newsByCategory.size(), savedNewsByCategory.size());
       assertEquals(newsByCategory, savedNewsByCategory);
    }



}

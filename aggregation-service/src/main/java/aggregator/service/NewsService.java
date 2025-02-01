package aggregator.service;

import aggregator.entity.News;
import aggregator.repository.NewsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final ObjectMapper objectMapper;

    private static final String DZEN_URL = "https://dzen.ru/news/rubric/chronologic?ajax=1";

    //TODO: перенести этот метод в тесты, которые нужно написать
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

    public List<News> fetchAndSaveNews() {

        List<News> newsList = new ArrayList<>();

        // получаем JSON по ссылке
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
        HttpEntity<String> request = new HttpEntity<>("", headers);
        ResponseEntity<String> response = restTemplate.exchange(DZEN_URL, HttpMethod.POST, request, String.class);
        String json = response.getBody();

        // Обработка JSON
        try {
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode stories = rootNode.path("data").path("stories");

            for (JsonNode story : stories) {
                News newNews = new News();
                newNews.setId(story.path("id").asText());
                newNews.setTitle(story.path("title").asText());
                newNews.setContent(story.path("annotation").asText());
                newNews.setUrl(story.path("url").asText());
                newNews.setPublishedAt(story.path("timestampInTop").asText());
                newsList.add(newNews);
            }
            newsRepository.saveAll(newsList);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return newsList;
    }

}
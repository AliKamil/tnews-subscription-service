package aggregator.service;

import aggregator.entity.News;
import aggregator.repository.NewsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
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



    public List<News> fetchAndSaveNews() {
        List<News> newsList = new ArrayList<>();
        // получаем JSON по ссылке
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<>("", new HttpHeaders());
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
                newNews.setCategory(story.path("bestRubricName").asText());
                newsList.add(newNews);
            }
            newsRepository.saveAll(newsList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    public List<String> getCategories() {
        return newsRepository.findDistinctCategories();
    }


}
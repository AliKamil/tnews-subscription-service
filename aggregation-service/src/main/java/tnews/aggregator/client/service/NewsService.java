package tnews.aggregator.client.service;

import lombok.extern.slf4j.Slf4j;
import tnews.aggregator.client.entity.News;
import tnews.aggregator.client.repository.CustomNewsRepository;
import tnews.aggregator.client.repository.NewsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final CustomNewsRepository customNewsRepository;
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
                News newNews = objectMapper.treeToValue(story, News.class);
                newsList.add(newNews);
            }
            newsRepository.saveAll(newsList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        log.info("News fetched and saved: " + newsList.size());
        return newsList;
    }

    public List<String> getCategories() {
        return newsRepository.findDistinctCategories();
    }

    public List<News> getNewsByCategory(String category) {
        var news = newsRepository.findByCategory(category);
        log.info(String.valueOf(news.size()));
        return news;
    }

    public List<News> findActualNews(Set<String> categories, Set<String> excludedNewsIds, int limit) {
        return customNewsRepository.findActualNews(categories, excludedNewsIds, limit);
    }


}
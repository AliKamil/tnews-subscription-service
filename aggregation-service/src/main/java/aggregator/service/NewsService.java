package aggregator.service;

import aggregator.entity.News;
import aggregator.repository.NewsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.client5.http.cookie.CookieStore;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.cookie.BasicClientCookie;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

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

        try {
            // Создаём хранилище для cookies
            CookieStore cookieStore = new BasicCookieStore();

            // Добавляем cookie вручную (если знаешь название cookie после авторизации)
            BasicClientCookie cookie = new BasicClientCookie("SessionID", "3:1738067083.5.0.1677501434255:3dBUXw:181.1.2:1|891977572.0.2.3:1676571932|64:10025624.659323.x9SF3OZsXsHtfVefBDoSC7hu698");
            cookie.setDomain("dzen.ru");
            cookie.setPath("/");
            cookieStore.addCookie(cookie);

            // Создаём HttpClient с поддержкой cookies
            HttpClientBuilder clientBuilder = HttpClients.custom();
            clientBuilder.setDefaultCookieStore(cookieStore);

            // Подключаем HttpClient к RestTemplate
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(clientBuilder.build()));

            // Делаем запрос к API
            String url = "https://dzen.ru/news/rubric/chronologic?ajax=1&neo_parent_id=1738430990444511-3514999381065768252-12.news-dzen-pginx.l7-news.dc.idzn.ru-BAL-4789-NEWS-NEWS_RUBRIC";
            String json = restTemplate.getForObject(url, String.class);

            System.out.println("Получен JSON: " + json);
            // Тут можно парсить JSON и сохранять в MongoDB

        } catch (Exception e) {
            e.printStackTrace();
        }

//          Обработка JSON
//
//            for (JsonNode news : newsNode) {
//                News newNews = new News();
//                newNews.setId(news.path("id").asText());
//                newNews.setTitle(news.path("title").asText());
//                newNews.setContent(news.path("annotation").asText());
//                newNews.setUrl(news.path("url").asText());
//                newNews.setPublishedAt(news.path("timestampInTop").asText());
//                newsList.add(newNews);
//            }
//            if (!newsList.isEmpty()) {
//                newsRepository.saveAll(newsList);
//            }
//
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
        return newsList;
    }

}
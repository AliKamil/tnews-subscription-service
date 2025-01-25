package aggregator;

import aggregator.entity.News;
import aggregator.repository.NewsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

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
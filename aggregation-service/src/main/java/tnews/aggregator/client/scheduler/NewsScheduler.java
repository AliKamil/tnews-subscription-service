package tnews.aggregator.client.scheduler;

import tnews.aggregator.client.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NewsScheduler {
    private final NewsService newsService;

    @Scheduled(fixedRate = 600000) // обновление новостей каждые 10 минут
    public void scheduleNewsFetching() {
        newsService.fetchAndSaveNews();
    }
}

package aggregator.controllers;

import aggregator.entity.News;
import aggregator.service.NewsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
@AllArgsConstructor
public class NewsController {

    private NewsService newsService;

    @GetMapping("/fetch")
    public List<News> fetchNews() {
        return newsService.fetchAndSaveNews();
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return newsService.getCategories();
    }
}

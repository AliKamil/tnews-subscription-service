package tnews.aggregator.client.controllers;

import tnews.aggregator.client.service.NewsService;
import tnews.aggregator.client.dto.NewsDto;
import tnews.aggregator.client.mapper.NewsMapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/news")
@AllArgsConstructor
public class NewsController {

    private NewsService newsService;

    @GetMapping("/fetch")
    public List<NewsDto> fetchNews() {
        return newsService.fetchAndSaveNews().stream()
                .map(NewsMapper::toDto)
                .toList();
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return newsService.getCategories();
    }

    @GetMapping("{category}")
    public List<String> getNewsByCategory(@PathVariable("category") String category) {
        return newsService.getNewsByCategory(category).stream()
                .map(NewsMapper::toDto)// может стоить написать toString просто в News?
                .map(NewsDto::toString)
                .toList();
    }



}

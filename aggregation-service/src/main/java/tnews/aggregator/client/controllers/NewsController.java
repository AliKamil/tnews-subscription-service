package tnews.aggregator.client.controllers;

import org.springframework.web.bind.annotation.*;
import tnews.aggregator.client.dto.NewsRequestDto;
import tnews.aggregator.client.service.NewsService;
import tnews.aggregator.client.dto.NewsDto;
import tnews.aggregator.client.mapper.NewsMapper;
import lombok.AllArgsConstructor;

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
    public List<NewsDto> getNewsByCategory(@PathVariable("category") String category) {
        return newsService.getNewsByCategory(category).stream()
                .map(NewsMapper::toDto)
                .toList();
    }

    @PostMapping("/actual")
    public List<NewsDto> getActualNews(@RequestBody NewsRequestDto request) {
        return newsService.findActualNews(request.getCategories(), request.getSendNewsIds(), request.getLimit()).stream()
                .map(NewsMapper::toDto)
                .toList();
    }

}

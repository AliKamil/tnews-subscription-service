package aggregator.mapper;

import aggregator.dto.NewsDto;
import aggregator.entity.News;

public class NewsMapper {
    public static NewsDto toDto(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setTitle(news.getTitle());
        newsDto.setContent(news.getContent());
        newsDto.setUrl(news.getUrl());
        return newsDto;
    }
    public static News fromDto(NewsDto newsDto) {
        News news = new News();
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setUrl(newsDto.getUrl());
        return news;
    }
}

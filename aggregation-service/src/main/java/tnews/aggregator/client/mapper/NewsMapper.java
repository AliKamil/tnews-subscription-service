package tnews.aggregator.client.mapper;

import tnews.aggregator.client.dto.NewsDto;
import tnews.aggregator.client.entity.News;

public class NewsMapper {
    public static NewsDto toDto(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(news.getId());
        newsDto.setTitle(news.getTitle());
        newsDto.setContent(news.getContent());
        newsDto.setUrl(news.getUrl());
        return newsDto;
    }
    public static News fromDto(NewsDto newsDto) {
        News news = new News();
        news.setId(newsDto.getId());
        news.setTitle(newsDto.getTitle());
        news.setContent(newsDto.getContent());
        news.setUrl(newsDto.getUrl());
        return news;
    }
}

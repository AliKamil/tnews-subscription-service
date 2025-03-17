package tnews.aggregator.client.repository;

import tnews.aggregator.client.entity.News;

import java.util.List;
import java.util.Set;

public interface CustomNewsRepository {
    List<News> findActualNews(Set<String> categories, Set<String> excludedNewsIds, int limit);
}

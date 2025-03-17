package tnews.aggregator.client.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import tnews.aggregator.client.entity.News;

import java.util.List;
import java.util.Set;

@Repository
@AllArgsConstructor
public class CustomNewsRepositoryImpl implements CustomNewsRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<News> findActualNews(Set<String> categories, Set<String> excludedNewsIds, int limit) {
        Query query = new Query();

        query.addCriteria(Criteria.where("category").in(categories));
        if (!excludedNewsIds.isEmpty()) {
            query.addCriteria(Criteria.where("_id").nin(excludedNewsIds));
        }

        // Сортировка по дате (или другой логике сортировки)
        query.with(Sort.by(Sort.Direction.DESC, "publishedAt"));
        query.limit(limit);

        return mongoTemplate.find(query, News.class);
    }
}

package tnews.aggregator.client.repository;

import tnews.aggregator.client.entity.News;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {
    public List<News> findByCategory(String category);

    @Aggregation("{ '$group': { '_id': '$category' } }")
    List<String> findDistinctCategories();
}

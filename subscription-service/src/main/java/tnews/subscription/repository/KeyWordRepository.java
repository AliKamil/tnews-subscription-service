package tnews.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnews.subscription.entity.KeyWord;

@Repository
public interface KeyWordRepository extends JpaRepository<KeyWord, Long> {
    KeyWord findBykeyword(String keyword);
}

package subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import subscription.entity.KeyWord;

@Repository
public interface KeyWordRepository extends JpaRepository<KeyWord, Long> {
    KeyWord findBykeyword(String keyword);
}

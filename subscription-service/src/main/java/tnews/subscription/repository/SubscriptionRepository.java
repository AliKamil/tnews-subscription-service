package tnews.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tnews.subscription.entity.Subscription;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long>, CustomizedSave<Subscription> {

    @Query(value = """
        SELECT *
        FROM subscription s
        WHERE
            (s.time_interval = 0 AND 
                (s.last_send IS NULL OR s.last_send <= now() - INTERVAL '1 minute'))
            OR
            (s.time_interval = 1 AND 
                (s.last_send IS NULL OR s.last_send <= now() - INTERVAL '1 day'))
            OR
            (s.time_interval = 2 AND 
                (s.last_send IS NULL OR s.last_send <= now() - INTERVAL '1 week'))
            OR
            (s.time_interval = 3 AND 
                (s.last_send IS NULL OR s.last_send <= now() - INTERVAL '1 month'))
        """, nativeQuery = true)
    List<Subscription> findValidSubscriptions();

}

package subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import subscription.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

package tnews.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnews.subscription.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

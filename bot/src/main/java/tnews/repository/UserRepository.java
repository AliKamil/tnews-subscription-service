package tnews.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tnews.entity.User;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}

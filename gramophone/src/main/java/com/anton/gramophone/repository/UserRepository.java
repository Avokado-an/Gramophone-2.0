package com.anton.gramophone.repository;

import com.anton.gramophone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    User findUserByEmail(String email);

    List<User> findAllBySubscribersContains(User user);

    List<User> findAllBySubscriptionsContains(User user);
}
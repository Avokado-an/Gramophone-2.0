package com.anton.gramophone.repository;

import com.anton.gramophone.entity.Post;
import com.anton.gramophone.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {
    List<Post> findAllByUser(User user);

    List<Post> findAllByUserId (Long id);

    int removeAllById(Long id);

    Optional<Post> findById(Long id);
}

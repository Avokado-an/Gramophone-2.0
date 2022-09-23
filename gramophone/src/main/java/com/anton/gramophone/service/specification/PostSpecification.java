package com.anton.gramophone.service.specification;

import com.anton.gramophone.entity.Post;
import com.anton.gramophone.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;

public class PostSpecification {
    private PostSpecification() {
    }

    public static Specification<Post> findFriendPosts(User user) {
        List<User> subscriptions = user.getSubscriptions();
        if(subscriptions == null) {
            subscriptions = Collections.emptyList();
        }
        subscriptions.add(user);
        List<User> finalSubscriptions = subscriptions;
        return (root, criteriaQuery, criteriaBuilder) -> {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("creationDateTime")));
            return criteriaBuilder.isTrue(root.get("user").in(finalSubscriptions));
        };
    }
}

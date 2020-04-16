package com.socialnetwork.users.repository;

import com.socialnetwork.users.domain.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, String> {
    User findByName(String name);
}

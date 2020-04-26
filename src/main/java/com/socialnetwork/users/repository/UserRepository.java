package com.socialnetwork.users.repository;

import com.socialnetwork.users.domain.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends Neo4jRepository<User, String> {


    @Query("MATCH (m:User) WHERE m.name CONTAINS $0 RETURN m")
    List<User> findUsersByName(@Param("name")String name);
}

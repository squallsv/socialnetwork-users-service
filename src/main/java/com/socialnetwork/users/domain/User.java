package com.socialnetwork.users.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.id.UuidStrategy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@NodeEntity
public class User {

    @Id
    @GeneratedValue(strategy = UuidStrategy.class)
    private String id;

    private String name;
    private String email;

    @Relationship(type = "FOLLOWS")
    @ToString.Exclude
    private Set<User> following;

    @Relationship(type = "FOLLOWS", direction = Relationship.INCOMING)
    @ToString.Exclude
    private Set<User> followers;

    public void follow(User user){
        if(following == null){
            following = new HashSet<>();
        }

        following.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

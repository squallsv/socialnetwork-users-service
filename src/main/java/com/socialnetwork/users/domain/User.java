package com.socialnetwork.users.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;
import org.springframework.data.neo4j.core.support.UUIDStringGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Node
public class User {

    @Id
    @GeneratedValue(value = UUIDStringGenerator.class)
    private String id;

    private String name;
    private String email;

    @Relationship(type = "FOLLOWS")
    @ToString.Exclude
    private Set<User> following;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
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

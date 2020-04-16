package com.socialnetwork.users.acceptance;


import com.socialnetwork.users.domain.User;
import com.socialnetwork.users.repository.UserRepository;
import com.socialnetwork.users.utils.UserBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.*;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences.PagedModelType;

import static org.hamcrest.MatcherAssert.assertThat;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.hateoas.client.Hop.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAcceptanceTests {

    public static final String USERS = "/users";
    public static final String RICK = "Rick";
    public static final String LESLIE = "Leslie";

    @LocalServerPort
    private int port;

    private URL usersFullURL;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    @BeforeAll
    public void setUp() throws Exception {
        this.usersFullURL = new URL("http://localhost:" + port + USERS);

        List<User> users = new ArrayList<>();

        users.add(new UserBuilder().name(RICK).email("rick@test.com").build());
        users.add(new UserBuilder().name(LESLIE).email("leslie@test.com").build());

        userRepository.saveAll(users);
    }

    @Test
    void givenANewUserIsDefined_WhenIPostIt_ThenANewUserIsCreated() throws Exception {
        var user = new UserBuilder().name("Pamela").email("pamela@test.com").build();

        var saveResponse = template.postForEntity(usersFullURL.toString(), new EntityModel<>(user),
                EntityModel.class);

        Optional<Link> link = saveResponse.getBody().getLink(IanaLinkRelations.SELF);
        var findByIdResponse = template.getForEntity(link.get().getHref(), User.class);

        assertThat(findByIdResponse.getStatusCodeValue(), is(200));
        assertThat(findByIdResponse.getBody(), is(user));
    }

    @Test
    void givenIHaveSomeUsers_WhenIDoAGet_ThenItReturnsAllTheUsers() throws URISyntaxException {
        Traverson client = new Traverson(usersFullURL.toURI(), MediaTypes.HAL_JSON);
        PagedModelType<User> collectionModelType =
                new PagedModelType<>() {};

        PagedModel<User> userPagedModel = client.//
                follow(rel("self")).//
                toObject(collectionModelType);

        Collection<User> users = userPagedModel.getContent();

        List<String> nameList = users.stream().map(x -> x.getName()).collect(Collectors.toList());

        assertThat(nameList.size(), is(2));
        assertThat(nameList, hasItem(RICK));
        assertThat(nameList, hasItem(LESLIE));
    }
}

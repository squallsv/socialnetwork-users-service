package com.socialnetwork.users.acceptance;


import com.socialnetwork.users.domain.User;
import com.socialnetwork.users.repository.UserRepository;
import com.socialnetwork.users.utils.UserBuilder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.*;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences.PagedModelType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.springframework.hateoas.client.Hop.rel;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAcceptanceTests {

    public static final String USERS = "users";
    public static final String BEN = "Ben Lane";
    public static final String RICK = "Rick Grant";
    public static final String LESLIE = "Leslie Grant";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private UserRepository userRepository;

    private URL usersURL;
    private List<User> users;
    private String usersUrlString;
    private User ben;
    private User rick;
    private User leslie;

    @BeforeEach
    @Transactional
    public void setUp() throws Exception {
        ben = new UserBuilder().name(BEN).email("ben@test.com").build();
        rick = new UserBuilder().name(RICK).email("rick_knope@test.com").build();
        leslie = new UserBuilder().name(LESLIE).email("leslie_knope@test.com").build();

        usersUrlString = format("http://localhost:%s/%s", port, USERS);
        usersURL = new URL(usersUrlString);

        users = Arrays.asList(ben, rick, leslie);

        userRepository.saveAll(users);
    }

    @AfterEach
    @Transactional
    public void tearUp(){
        var allUsers = userRepository.findAll();
        userRepository.deleteAll(allUsers);
    }

    @Test
    void givenANewUserIsDefined_WhenIPostIt_ThenANewUserIsCreated() throws Exception {
        var user = new UserBuilder().name("Pamela").email("pamela@test.com").build();

        var saveResponse = template.postForEntity(usersURL.toString(), new EntityModel<>(user),
                EntityModel.class);

        Optional<Link> link = saveResponse.getBody().getLink(IanaLinkRelations.SELF);
        var findByIdResponse = template.getForEntity(link.get().getHref(), User.class);

        assertThat(findByIdResponse.getStatusCodeValue(), is(200));
        assertThat(findByIdResponse.getBody().getName(), is(user.getName()));
        assertThat(findByIdResponse.getBody().getEmail(), is(user.getEmail()));
    }

    @Test
    void givenIHaveSomeUsers_WhenIDoAGet_ThenItReturnsAllTheUsers() throws URISyntaxException {
        Collection<User> users = retrieveUsers(Optional.empty());

        List<String> nameList = users.stream().map(x -> x.getName()).collect(Collectors.toList());

        assertThat(nameList.size(), is(3));
        assertThat(nameList, hasItem(BEN));
        assertThat(nameList, hasItem(RICK));
        assertThat(nameList, hasItem(LESLIE));
    }

    @Test
    void givenIHaveAUser_AndIChangeTheEmail_WhenIDoAPut_ThenTheUserGetsUpdated() {
        var rickId = users.stream()
                .filter(x -> x.getName().equals(RICK))
                .map(x -> x.getId())
                .findFirst()
                .orElse(null);

        var rickURL = format("%s/%s", usersURL.toString(), rickId);

        var expectedEmail = "rick_test@gmail.com";

        var rick = template.getForObject(rickURL, User.class);
        rick.setEmail(expectedEmail);

        template.put(rickURL, rick);

        var finalResponse = template.getForObject(rickURL, User.class);

        assertThat(finalResponse.getEmail(), is(expectedEmail));
    }

    @Test
    void givenIHaveUsers_WhenICallFindUsersByName_AndIPassAValidPartOfAName_ThenCorrespondingUsersReturn() throws MalformedURLException, URISyntaxException {
        var findUsersByNamePath = "/search/findUsersByName";
        var url = new URL(format("%s/%s", usersUrlString, findUsersByNamePath));

        var urlWithQueryParams = UriComponentsBuilder.
                fromUri(url.toURI()).//
                queryParam("name", "Gra").//
                build();

        var users = retrieveUsers(Optional.of(urlWithQueryParams.toUri()));

        List<String> nameList = users.stream().map(x -> x.getName()).collect(Collectors.toList());

        assertThat(nameList.size(), is(2));
        assertThat(nameList, hasItem(RICK));
        assertThat(nameList, hasItem(LESLIE));
    }

    private Collection<User> retrieveUsers(Optional<URI> uri) throws URISyntaxException {
        var client = new Traverson(uri.orElse(usersURL.toURI()), MediaTypes.HAL_JSON);
        PagedModelType<User> collectionModelType =
                new PagedModelType<>() {};

        PagedModel<User> userPagedModel = client.//
                follow(rel("self")).//
                toObject(collectionModelType);

        return userPagedModel.getContent();
    }
}

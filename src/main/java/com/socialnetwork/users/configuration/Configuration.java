package com.socialnetwork.users.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableNeo4jRepositories(basePackages = "com.socialnetwork.users.repository")
public class Configuration {
}

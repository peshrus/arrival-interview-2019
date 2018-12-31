package com.peshchuk.arrival.spring;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.dialect.PostgresDialect;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.function.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.function.ReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

/**
 * @author Ruslan Peshchuk (peshrus@gmail.com)
 */
@Configuration
@EnableR2dbcRepositories
public class RepositoriesConfiguration extends AbstractR2dbcConfiguration {

  @Value("${spring.datasource.url}")
  private String dbUrl;
  private String dbHost;
  private int dbPort;
  private String dbName;
  @Value("${spring.datasource.username}")
  private String username;
  @Value("${spring.datasource.password}")
  private String password;

  @PostConstruct
  public void init() {
    final String[] dbUrlParts = dbUrl.split("/");
    final String[] hostPort = dbUrlParts[dbUrlParts.length - 2].split(":");

    dbHost = hostPort[0];
    dbPort = Integer.valueOf(hostPort[1]);
    dbName = dbUrlParts[dbUrlParts.length - 1];
  }

  @Bean
  public TrackRepository repositoryTrack(R2dbcRepositoryFactory factory) {
    return factory.getRepository(TrackRepository.class);
  }

  @Bean
  public CarRepository repositoryCar(R2dbcRepositoryFactory factory) {
    return factory.getRepository(CarRepository.class);
  }

  @Bean
  public R2dbcRepositoryFactory factory(DatabaseClient client,
      ReactiveDataAccessStrategy reactiveDataAccessStrategy, RelationalMappingContext context) {
    return new R2dbcRepositoryFactory(client, context, reactiveDataAccessStrategy);
  }

  @Bean
  public DatabaseClient databaseClient(ConnectionFactory factory) {
    return DatabaseClient.builder().connectionFactory(factory).build();
  }

  @Bean
  public ReactiveDataAccessStrategy reactiveDataAccessStrategy() {
    return new DefaultReactiveDataAccessStrategy(PostgresDialect.INSTANCE);
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    final var config = PostgresqlConnectionConfiguration.builder()
        .host(dbHost)
        .port(dbPort)
        .database(dbName)
        .username(username)
        .password(password)
        .build();

    return new PostgresqlConnectionFactory(config);
  }
}

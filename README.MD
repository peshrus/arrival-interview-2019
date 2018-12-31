[![Travis CI](https://travis-ci.org/peshrus/arrival.svg?branch=master)](https://travis-ci.org/peshrus/arrival)
# [arrival.com](https://arrival.com): [vacancies](https://spb.hh.ru/employer/1751001#vacancy-list)
## [The Task](./_input/java-test.txt)

Implement a web application with the following features:

1. A web form for input of data from the JSON file (see [test.json](./_input/test.json));
2. Saving of the data to a storage by pressing a button on the form;
3. Display the saved data, the 1st step should be available again.

**Requirements**: an embedded microcontainer (Jetty, Tomcat), Postgres, Maven, Spring

## Demo
[YouTube](https://youtu.be/0_KW6WdnTyI)

## My Steps
1. Run postgres: `docker run --name arrival-postgres -e POSTGRES_DB=arrival -e POSTGRES_USER=arrival -e POSTGRES_PASSWORD=mysecretpassword -p 5432:5432 -d postgres`
    * Run queries: `docker run -it --rm --link arrival-postgres:postgres postgres psql -h postgres -U arrival`
2. Generate initial files on https://start.spring.io/ (Dependencies: Reactive Web, Lombok)
3. Create DB schema script and apply it to DB ([schema.sql](./src/main/resources/schema.sql))
4. Configure ReactiveCrudRepository for Postgres
5. Configure WebFlux
6. Add React frontend
5. Configure connection between WebFlux and React:
    * https://piotrminkowski.wordpress.com/2018/10/18/introduction-to-reactive-apis-with-postgres-r2dbc-spring-data-jdbc-and-spring-webflux/
    * https://medium.freecodecamp.org/how-to-build-a-real-time-editable-datagrid-in-react-c13a37b646ec
    * https://blog.monkey.codes/how-to-build-a-chat-app-using-webflux-websockets-react/
    * https://developer.okta.com/blog/2018/09/25/spring-webflux-websockets-react
    
## Difficulties
1. Make reactive Postgres connection
2. Make React UI
3. Add React UI to the Spring project
4. Configure connection between WebFlux and React

## TO DO
1. Proper validation of the form for cars
2. Edit & delete entities
3. Unit tests
4. Load drop-downs options from backend
5. Make getting of data from DB reactive. Right now changes in DB are not reflected in UI automatically.

## Final Remarks
1. Reactivity was not the goal of the task but we discussed reactive programming on the interview 
that's why I did it so.
2. I used Java 11 instead of Java 8.
3. I didn't use Jetty or Tomcat because I decided to make a reactive application.
4. Frontend technologies were not specified in the requirements that's why I used React.
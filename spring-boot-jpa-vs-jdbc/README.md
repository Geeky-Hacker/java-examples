# Spring Boot JPA vs JDBC

The same tiny Spring Boot app built with Spring Data JPA (`-Pjpa`) and with `JdbcClient` (`-Pjdbc`, default), plus a `StartupReport` that prints startup time and RSS.

Run `mvn verify` (JDBC) and `mvn verify -Pjpa`, then start `target/jpa-vs-jdbc-1.0.0.jar` with `java -Xmx80m -XX:+UseSerialGC -jar ...` and compare the `STARTUP_MS` and `RSS_MIB` output.

# Related articles

- [Spring Boot JPA vs JDBC: measuring memory and startup cost](https://www.geekyhacker.com/spring-boot-jpa-vs-jdbc-memory-startup/)

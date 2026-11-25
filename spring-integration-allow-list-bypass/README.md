# spring-integration-allow-list-bypass

Reproduces CVE-2026-59383 (JdbcMessageStore allow-list emptied by `setBeanClassLoader()`) with a harmless gadget class that only counts deserializations.
Run `mvn verify`. The default Spring Integration version is the patched 7.0.8; use `-Dspring.integration.version=7.0.7` to see the regression.

# Related articles

- [CVE-2026-59383: Spring Integration JdbcMessageStore allow-list bypass](https://www.geekyhacker.com/cve-2026-59383-spring-integration-jdbcmessagestore/)

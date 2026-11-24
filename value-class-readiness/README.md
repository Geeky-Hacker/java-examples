# value-class-readiness

A small reflection checker that finds classes ready to become JEP 401 value classes (all instance fields final, no synchronized instance methods). It runs on JDK 21, no preview JDK needed.
Run it with `mvn verify`.

# Related articles

- [Trying JEP 401 value classes: what changes for your Java code](https://www.geekyhacker.com/jep-401-value-classes-hands-on/)

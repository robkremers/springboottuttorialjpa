# Getting Started

## Topics

### Mocking

Terms:
- A mock is a nullified class instance that does not play a role in a test.
- A stub is a mock with additional instructions for how to behave when certain methods are called.
- A spy is just like a regular class instance with the ability to also stub method calls.

### The use of interfaces

Interfaces can be useful e.g. in situations where connectivity to the outer world needs to be mocked.

## Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.5/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.5/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.5.5/reference/htmlsingle/#using-boot-devtools)

## Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

* https://assertj.github.io/doc/#overview-what-is-assertj
* https://javadoc.io/static/org.mockito/mockito-core/3.9.0/org/mockito/Mockito.html
* https://www.baeldung.com/jackson-annotations
* https://reflectoring.io/spring-boot-data-jpa-test/
* https://javadoc.io/static/com.fasterxml.jackson.core/jackson-annotations/2.13.0/index.html
* https://stripe.com/docs/api?lang=java
* https://github.com/stripe/stripe-mock
  * Used for mocking the actual stripe functionality.
  * stripe-mock is a mock HTTP server that responds like the real Stripe API
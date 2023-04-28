[![](https://jitpack.io/v/hmcts/send-letter-client.svg)](https://jitpack.io/#hmcts/send-letter-client)
# Send Letter Client

This is the client library for the [send-letter-service](https://github.com/hmcts/send-letter-service) aka Bulk Printing microservice.

## Getting started

### Prerequisites

- [JDK 17](https://www.oracle.com/java)
- Project requires Spring Boot v2.2 to be present

## Usage

Just include the library as your dependency and you will be to use the client class. Health check for send-letter-producer is provided as well.

Components provided by this library will get automatically configured in a Spring context if `send-letter.url` configuration property is defined and does not equal `false`.
False would disable the auto configuration of the API.

### Building

The project uses [Gradle](https://gradle.org) as a build tool but you don't have install it locally since there is a
`./gradlew` wrapper script.

To build project execute the following command:

```bash
    ./gradlew build
```

## Developing

### Coding style tests

To run all checks (including unit tests) execute the following command:

```bash
    ./gradlew check
```

## Versioning

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.

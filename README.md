[![](https://jitpack.io/v/hmcts/send-letter-client.svg)](https://jitpack.io/#hmcts/send-letter-client)
# Send Letter Client
This is the client library for the [send-letter-service](https://github.com/hmcts/send-letter-service) aka Bulk Print.

## Getting started

### Prerequisites

- [JDK 21](https://www.oracle.com/java)
- Project requires Spring Boot v3.x to be present

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

## Models
This library includes the following API models:

- Document: Represents a document with a template and associated values.
- LetterStatus: Represents the status of a letter.
- LetterWithPdfsRequest: Represents a letter with PDFs request.
- LetterV3: Represents a letter with the LetterV3 request.
- Letter: Represents a letter with PDFs requests (deprecated)

## Requests

This library provides methods to make the following API requests to the send-letter-service:

Sending a Letter

Deprecated Method (Not Recommended): This method is deprecated and will be removed in future versions. Use the updated methods with specific request objects instead.

```@Deprecated(since = "15-June-2021", forRemoval = true)
SendLetterResponse sendLetter(String serviceAuthHeader, Letter letter)
```
Sending a Letter with LetterV3 Request:
```
SendLetterResponse sendLetter(String serviceAuthHeader, LetterV3 letter)
```

Sending a Letter with PDFs Request:
```
SendLetterResponse sendLetter(String serviceAuthHeader, LetterWithPdfsRequest letter)
```

Confirming Request Creation:
```
void confirmRequestIsCreated(UUID letterId)
```
This method confirms that the request is created successfully.


## Developing

### Coding style tests

To run all checks (including unit tests) execute the following command:

```bash
./gradlew check
```

## Installation

Add the following dependency to your `build.gradle` file:

```gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.hmcts:send-letter-client:1.0.0' // Replace with the latest version
}
```


## Versioning

We use [SemVer](http://semver.org/) for versioning.
For the versions available, see the tags on this repository.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE.md) file for details.


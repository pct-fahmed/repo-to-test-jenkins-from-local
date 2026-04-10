# repo-to-test-jenkins-from-local

Baseline Java project using:

- Java 21
- Spring Boot
- Vaadin
- PostgreSQL
- Liquibase
- Docker
- Maven
- Jenkins

## Local development

Start PostgreSQL:

```bash
docker compose up -d db
```

Run the application:

```bash
mvn spring-boot:run
```

Open `http://localhost:8081`.

## Full container run

```bash
docker compose up --build
```

## Maven build

```bash
mvn clean verify
```

## Production build

```bash
mvn -Pproduction clean package
```

## Jenkins

The `Jenkinsfile` expects Docker available on the Jenkins agent and a working `mvn` command on that agent.

Pipeline flow:

- starts PostgreSQL in Docker for CI-backed tests
- runs `mvn clean test`
- runs `mvn -DskipTests package`
- builds a Docker image
- optionally runs a deploy stage

Useful Jenkins parameters:

- `IMAGE_REPO` for the image name
- `IMAGE_TAG` to override the default `build-<BUILD_NUMBER>` tag
- `DEPLOY_ENABLED` to turn on deploy
- `DEPLOY_ENV` to choose the target environment

## Local Jenkins

For a local Jenkins controller running in Docker, see [LOCAL_JENKINS.md](/home/faiz/datadisk/gits/repo-to-test-jenkins-from-local/LOCAL_JENKINS.md).

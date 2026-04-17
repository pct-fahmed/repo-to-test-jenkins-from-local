# idm-plus-poc

Baseline Java project using:

- Java 21
- Spring Boot
- Vaadin
- PostgreSQL
- Liquibase
- Swagger / OpenAPI
- Testcontainers
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

## Swagger

When the application is running, open:

- Swagger UI: `http://localhost:8081/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8081/api-docs`

## Database relationships

The schema is defined in [002-create-commerce-tables.yaml](/home/faiz/datadisk/gits/idm-plus-poc/src/main/resources/db/changelog/changes/002-create-commerce-tables.yaml).

Main table relationships:

- `customer_order` to `order_item`: one-to-many
- `product` to `order_item`: one-to-many
- `inventory` to `inventory_item`: one-to-many
- `product` to `inventory_item`: one-to-many

Derived business relationships:

- `customer_order` to `product`: many-to-many through `order_item`
- `inventory` to `product`: many-to-many through `inventory_item`

Important constraints:

- `order_item.order_id` references `customer_order.id`
- `order_item.product_id` references `product.id`
- `inventory_item.inventory_id` references `inventory.id`
- `inventory_item.product_id` references `product.id`
- `inventory_item` has a unique constraint on `inventory_id, product_id`, so the same product can appear only once per inventory

## Full container run

```bash
docker compose up --build
```

## Maven build

```bash
mvn clean verify
```

## Integration tests

Integration tests use Testcontainers with PostgreSQL, not the local Docker Compose database.

Run them with:

```bash
mvn test
```

What happens during test startup:

- Testcontainers starts a PostgreSQL container
- Liquibase applies the schema and seed data to that container
- Spring Boot runs the REST integration tests against that PostgreSQL instance

The shared test container configuration lives in [AbstractPostgreSqlContainerTest.java](/home/faiz/datadisk/gits/idm-plus-poc/src/test/java/com/example/dvzdemo/support/AbstractPostgreSqlContainerTest.java).

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

For a local Jenkins controller running in Docker, see [LOCAL_JENKINS.md](/home/faiz/datadisk/gits/idm-plus-poc/LOCAL_JENKINS.md).

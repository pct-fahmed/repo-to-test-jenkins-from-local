# Local Jenkins

This repository includes a local Jenkins setup intended for development use on a single machine.

## Start Jenkins

```bash
docker compose -f docker-compose.jenkins.yml up -d --build
```

If Jenkins is already running, restart it after pipeline/networking changes:

```bash
docker compose -f docker-compose.jenkins.yml up -d --build --force-recreate
```

Jenkins UI:

- `http://localhost:8090`

Initial admin password:

```bash
docker exec idm-plus-poc-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

## Create the pipeline job

After Jenkins starts:

1. Unlock Jenkins with the initial admin password.
2. Install suggested plugins if prompted.
3. Create an admin user.
4. Create a new item of type `Pipeline`.
5. In the pipeline configuration:
   - choose `Pipeline script from SCM`
   - select `Git`
   - repository URL:
     - local quick start: `/workspace`
     - Bitbucket later: use your Bitbucket clone URL
   - script path: `Jenkinsfile`

## Run the job

Build the pipeline job. Jenkins will:

- start PostgreSQL in Docker
- run `mvn clean test`
- run `mvn -DskipTests package`
- build the Docker image

The Jenkins container reaches the CI PostgreSQL container through `host.docker.internal`, which is mapped in [docker-compose.jenkins.yml](/home/faiz/datadisk/gits/idm-plus-poc/docker-compose.jenkins.yml).

## Notes

- This setup mounts the host Docker socket into Jenkins so Jenkins can run Docker commands on your machine.
- That is convenient for local development, but it is not a hardened production setup.
- The repository is mounted into the Jenkins container at `/workspace` to make local testing easy.

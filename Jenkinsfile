pipeline {
    agent any

    options {
        timestamps()
        ansiColor('xterm')
    }

    parameters {
        string(name: 'IMAGE_REPO', defaultValue: 'repo-to-test-jenkins-from-local', description: 'Docker image repository/name')
        string(name: 'IMAGE_TAG', defaultValue: '', description: 'Optional image tag override; defaults to build-<BUILD_NUMBER>')
        choice(name: 'DEPLOY_ENV', choices: ['none', 'dev', 'qa', 'prod'], description: 'Deployment target environment')
        booleanParam(name: 'DEPLOY_ENABLED', defaultValue: false, description: 'Run the deploy stage after the image build')
    }

    environment {
        MAVEN_REPO_LOCAL = "${WORKSPACE}/.m2/repository"
        DB_CONTAINER = "repo-to-test-jenkins-from-local-ci-db-${BUILD_NUMBER}"
        CI_DB_PORT = '55432'
        CI_DB_HOST = 'host.docker.internal'
        POSTGRES_DB = 'dvz_demo'
        POSTGRES_USER = 'dvz'
        POSTGRES_PASSWORD = 'dvz'
        SPRING_DATASOURCE_URL = "jdbc:postgresql://${CI_DB_HOST}:${CI_DB_PORT}/dvz_demo"
        SPRING_DATASOURCE_USERNAME = 'dvz'
        SPRING_DATASOURCE_PASSWORD = 'dvz'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare') {
            steps {
                sh 'chmod +x scripts/deploy.sh'
                script {
                    env.EFFECTIVE_IMAGE_TAG = params.IMAGE_TAG?.trim() ? params.IMAGE_TAG.trim() : "build-${env.BUILD_NUMBER}"
                }
            }
        }

        stage('Start Database') {
            steps {
                sh '''
                    docker rm -f ${DB_CONTAINER} >/dev/null 2>&1 || true
                    docker run -d --name ${DB_CONTAINER} \
                      -e POSTGRES_DB=${POSTGRES_DB} \
                      -e POSTGRES_USER=${POSTGRES_USER} \
                      -e POSTGRES_PASSWORD=${POSTGRES_PASSWORD} \
                      -p ${CI_DB_PORT}:5432 postgres:16-alpine
                '''
                sh '''
                    for i in $(seq 1 30); do
                      docker exec ${DB_CONTAINER} pg_isready -U ${POSTGRES_USER} -d ${POSTGRES_DB} && exit 0
                      sleep 2
                    done
                    docker logs ${DB_CONTAINER} || true
                    exit 1
                '''
            }
        }

        stage('Test') {
            steps {
                sh 'mvn -B -Dmaven.repo.local=${MAVEN_REPO_LOCAL} clean test'
            }
        }

        stage('Package') {
            steps {
                sh 'mvn -B -Dmaven.repo.local=${MAVEN_REPO_LOCAL} -DskipTests package'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${IMAGE_REPO}:${EFFECTIVE_IMAGE_TAG} .'
            }
        }

        stage('Deploy') {
            when {
                expression { params.DEPLOY_ENABLED && params.DEPLOY_ENV != 'none' }
            }
            steps {
                sh './scripts/deploy.sh ${DEPLOY_ENV} ${IMAGE_REPO}:${EFFECTIVE_IMAGE_TAG}'
            }
        }
    }

    post {
        always {
            junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
            sh 'docker rm -f ${DB_CONTAINER} >/dev/null 2>&1 || true'
        }
    }
}

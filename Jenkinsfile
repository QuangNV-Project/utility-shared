pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK21'
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        MAVEN_SETTINGS_FILE = '.m2/settings.xml'

        // Job data
        JOB_NAME = "${env.JOB_NAME}"
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
              steps {
                    echo "DEBUG: Bắt đầu stage Checkout..."
                    checkout scm
              }
        }

        stage('Setup Maven Settings') {
            steps {
                script {
                    configFileProvider([configFile(fileId: 'maven-settings', variable: 'MAVEN_SETTINGS_PATH')]) {
                        sh 'mkdir -p .m2'
                        sh "cp ${MAVEN_SETTINGS_PATH} ${MAVEN_SETTINGS_FILE}"
                    }
                }
            }
        }

        stage('Deploy Snapshot') {
            when {
                branch 'dev'
            }
            steps {
                script {
                    echo 'Deploying snapshot version (từ pom.xml)...'
                    withCredentials([usernamePassword(credentialsId: 'github-credentials', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_PASSWORD')]) {
                        sh "mvn -s ${MAVEN_SETTINGS_FILE} clean deploy"
                    }
                }
            }
        }

        stage('Deploy Version') {
            when {
                branch 'main'
            }
            steps {
                script {
                    echo 'Deploying release version with build number...'

                    // 1. Đọc phiên bản hiện tại từ pom.xml
                    def currentVersion = sh(
                        script: "mvn -s ${MAVEN_SETTINGS_FILE} help:evaluate -Dexpression=project.version -q -DforceStdout",
                        returnStdout: true
                    ).trim()

                    // 2. Tạo phiên bản mới bằng cách gắn BUILD_NUMBER
                    def newVersion
                    if (currentVersion.endsWith('-SNAPSHOT')) {
                        newVersion = currentVersion.replace('-SNAPSHOT', ".${env.BUILD_NUMBER}")
                    } else {
                        newVersion = "${currentVersion}.${env.BUILD_NUMBER}"
                    }

                    echo "Current version in pom: ${currentVersion}"
                    echo "New version for deploy: ${newVersion}"

                    try {
                        // 3. Đặt phiên bản mới (chỉ trong workspace)
                        sh "mvn -s ${MAVEN_SETTINGS_FILE} versions:set -DnewVersion=${newVersion}"

                        // 4. Deploy phiên bản mới
                        withCredentials([usernamePassword(credentialsId: 'github-credentials', usernameVariable: 'GITHUB_USER', passwordVariable: 'GITHUB_PASSWORD')]) {
                            sh "mvn -s ${MAVEN_SETTINGS_FILE} clean deploy"
                        }
                    } finally {
                        echo "Reverting pom.xml to ${currentVersion}"
                        sh "mvn -s ${MAVEN_SETTINGS_FILE} versions:revert"
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                echo "Pipeline execution completed"
            }
        }
        success {
            script {
                withCredentials([
                    string(credentialsId: 'telegram-bot-token', variable: 'TELEGRAM_BOT_TOKEN'),
                    string(credentialsId: 'telegram-chat-id', variable: 'TELEGRAM_CHAT_ID')
                ]) {
                    sh """
                        curl -s -X POST https://api.telegram.org/bot$TELEGRAM_BOT_TOKEN/sendMessage \
                        -d chat_id=$TELEGRAM_CHAT_ID \
                        -d text="✅ Pipeline succeeded! Service: utility-shared Job: $JOB_NAME (#$BUILD_NUMBER)"
                    """
                }
            }
        }

        failure {
            script {
                withCredentials([
                    string(credentialsId: 'telegram-bot-token', variable: 'TELEGRAM_BOT_TOKEN'),
                    string(credentialsId: 'telegram-chat-id', variable: 'TELEGRAM_CHAT_ID')
                ]) {
                    sh """
                        curl -s -X POST https://api.telegram.org/bot$TELEGRAM_BOT_TOKEN/sendMessage \
                        -d chat_id=$TELEGRAM_CHAT_ID \
                        -d text="❌ Pipeline failed! Service: utility-shared Job: $JOB_NAME (#$BUILD_NUMBER)"
                    """
                }
            }
        }
        cleanup {
            script {
                sh 'docker logout || true'
            }
        }
    }
}
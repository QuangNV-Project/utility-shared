pipeline {
    agent any
    
    tools {
        maven 'Maven'
        jdk 'JDK21'
    }
    
    environment {
        MAVEN_OPTS = '-Xmx1024m'
        MAVEN_SETTINGS_FILE = '.m2/settings.xml'
    }

    stages {
        stage('Validate Start') {
              steps {
                   // Bạn sẽ thấy log này ngay lập tức nếu file Jenkinsfile này được chạy
                   echo "DEBUG: Pipeline đã bắt đầu!"
                   // In ra tên nhánh hiện tại để kiểm tra điều kiện 'when'
                   echo "DEBUG: Đang build nhánh: ${env.BRANCH_NAME}"
              }
        }

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
        success {
            echo 'Deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed!'
        }
        always {
            cleanWs()
            sh "rm -f ${MAVEN_SETTINGS_FILE}"
        }
    }
}
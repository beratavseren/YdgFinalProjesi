pipeline {
  agent any

  tools {
    jdk 'jdk-21' 
  }

  environment {
    // Windows'ta environment değişkenleri bazen farklı davranabilir ama bu genellikle sorun çıkarmaz
    MAVEN_OPTS = "-Dmaven.test.failure.ignore=false"
  }
  options {
    timestamps()
    // ansiColor eklentisi yüklü değilse aşağıdaki satırı kapalı tut:
    ansiColor('xterm') 
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    // --- Backend Build ---
    stage('Backend Build') {
      steps {
        dir('YdgBackend') {
          // sh -> bat olarak değişti
          bat label: 'Maven Clean Package', script: 'mvn -q -B -DskipTests clean package'
        }
      }
    }

    // --- Unit Tests ---
    stage('Unit Tests (Backend)') {
      steps {
        dir('YdgBackend') {
          // sh -> bat olarak değişti
          bat label: 'Run Unit Tests', script: 'mvn -q -B -DskipITs test'
        }
      }
      post {
        always {
          junit 'YdgBackend/target/surefire-reports/*.xml'
        }
      }
    }

    // --- Integration Tests ---
    stage('Integration Tests (Backend)') {
      steps {
        dir('YdgBackend') {
          // sh -> bat olarak değişti
          bat label: 'Run ITs', script: 'mvn -q -B verify -DskipUnitTests'
        }
      }
      post {
        always {
          junit 'YdgBackend/target/failsafe-reports/*.xml'
        }
      }
    }

    // --- Docker Build ---
    stage('Docker Build Images') {
      steps {
        script {
          // sh -> bat olarak değişti
          bat 'docker build -t ydg-backend:latest ./YdgBackend'
          bat 'docker build -t ydg-frontend:latest ./ydgfrontend'
        }
      }
    }

    // --- Sistemi Başlat ---
    stage('System Up (docker-compose)') {
      steps {
        // sh -> bat olarak değişti
        bat 'docker compose -f docker-compose.yml up -d --wait'
      }
    }

    // --- Selenium Testleri ---
    stage('Selenium E2E Tests') {
      steps {
        dir('e2e-tests') {
          // sh -> bat olarak değişti
          bat 'mvn -B test -De2e.baseUrl=http://ydg-frontend'
        }
      }
      post {
        always {
          junit 'e2e-tests/target/surefire-reports/*.xml'
        }
      }
    }
  }

  post {
    always {
      script {
        // sh -> bat olarak değişti. Hata yakalama bloğu korundu.
        try { 
            bat 'docker compose -f docker-compose.yml down -v' 
        } catch (err) { 
            echo "Compose down failed: ${err}" 
        }
      }
      archiveArtifacts artifacts: 'YdgBackend/target/*.jar', fingerprint: true, onlyIfSuccessful: true
    }
  }
}
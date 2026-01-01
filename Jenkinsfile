pipeline {
  agent any
  environment {
    MAVEN_OPTS = "-Dmaven.test.failure.ignore=false"
  }
  options {
    timestamps()
    ansiColor('xterm')
  }
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    // --- Backend Build (Klasör: YdgBackend) ---
    stage('Backend Build') {
      steps {
        // DİKKAT: Klasör ismini tam olarak 'YdgBackend' yazdık
        dir('YdgBackend') {
          sh label: 'Maven Clean Package', script: 'mvn -q -B -DskipTests clean package'
        }
      }
    }

    // --- Unit Tests (Klasör: YdgBackend) ---
    stage('Unit Tests (Backend)') {
      steps {
        dir('YdgBackend') {
          sh label: 'Run Unit Tests', script: 'mvn -q -B -DskipITs test'
        }
      }
      post {
        always {
          // Rapor yolu YdgBackend altında
          junit 'YdgBackend/target/surefire-reports/*.xml'
        }
      }
    }

    // --- Integration Tests (Klasör: YdgBackend) ---
    stage('Integration Tests (Backend)') {
      steps {
        dir('YdgBackend') {
          sh label: 'Run ITs', script: 'mvn -q -B verify -DskipUnitTests'
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
          // Backend imajı: YdgBackend klasöründen üretiliyor
          sh 'docker build -t ydg-backend:latest ./YdgBackend'
          
          // Frontend imajı: ydgfrontend klasöründen üretiliyor
          sh 'docker build -t ydg-frontend:latest ./ydgfrontend'
        }
      }
    }

    // --- Sistemi Başlat ---
    stage('System Up (docker-compose)') {
      steps {
        sh 'docker compose -f docker-compose.yml up -d --wait'
      }
    }

    // --- Selenium Testleri (e2e-tests klasörü aynı kalıyor) ---
    stage('Selenium E2E Tests') {
      steps {
        dir('e2e-tests') {
          sh 'mvn -q -B test'
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
        try { sh 'docker compose -f docker-compose.yml down -v' } catch (err) { echo "Compose down failed: ${err}" }
      }
      // Artifact arşivlerken YdgBackend yolunu kullanıyoruz
      archiveArtifacts artifacts: 'YdgBackend/target/*.jar', fingerprint: true, onlyIfSuccessful: true
    }
  }
}
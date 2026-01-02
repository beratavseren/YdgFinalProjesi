pipeline {
  agent any

  tools {
    jdk 'jdk-21' 
  }

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

    // --- 1. Testler (Raporlama için Localde Çalışır) ---
    // Not: Bu aşamalar Docker'dan bağımsızdır, Jenkins makinesinde çalışır.
    // Amaç: Hızlı feedback almak ve JUnit raporlarını kaydetmek.
    
    stage('Unit Tests (Backend)') {
      steps {
        dir('YdgBackend') {
          bat label: 'Run Unit Tests', script: 'mvn -q -B -DskipITs test'
        }
      }
      post {
        always {
          junit 'YdgBackend/target/surefire-reports/*.xml'
        }
      }
    }

    stage('Integration Tests (Backend)') {
      steps {
        dir('YdgBackend') {
          bat label: 'Run ITs', script: 'mvn -q -B verify -DskipUnitTests'
        }
      }
      post {
        always {
          junit 'YdgBackend/target/failsafe-reports/*.xml'
        }
      }
    }

    // --- 2. Docker Build (Artık Compose ile) ---
    // Burası en önemli değişiklik! 
    // Compose dosyasındaki ayarları (args) okuyarak image oluşturur.
    stage('Docker Build Images') {
      steps {
        script {
          // Eski 'docker build' komutları yerine bunu kullanıyoruz:
          bat 'docker compose -f docker-compose.yml build'
        }
      }
    }

    // --- 3. Sistemi Başlat ---
    stage('System Up') {
      steps {
        // --wait parametresi healthcheck'lerin 'healthy' olmasını bekler
        bat 'docker compose -f docker-compose.yml up -d --wait'
      }
    }

    // --- 4. Selenium Testleri ---
    stage('Selenium E2E Tests') {
      steps {
        dir('e2e-tests') {
          // Frontend konteynerine ulaşmak için baseUrl veriliyor
          bat 'mvn -B test -De2e.baseUrl=http://localhost:3000'
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
        try { 
            // İş bitince her şeyi temizle
            bat 'docker compose -f docker-compose.yml down -v' 
        } catch (err) { 
            echo "Compose down failed: ${err}" 
        }
      }
      // ARTIK JAR ARŞİVLEMEYE GEREK YOK (Çünkü jar Docker içinde kaldı)
      // archiveArtifacts satırı kaldırıldı.
    }
  }
}
# YoungT-fy

YoungT-fy는 JavaFX를 기반으로 구축된 데스크톱 애플리케이션으로, Spotify Web API와 연동하여 음악 관련 기능을 제공하며 MySQL 데이터베이스를 사용하여 데이터를 관리합니다.

## 📋 프로젝트 소개

이 프로젝트는 사용자에게 직관적인 데스크톱 인터페이스를 제공하기 위해 만들어졌습니다. JavaFX의 강력한 UI 툴킷을 활용하여 세련된 화면을 구성하고, 백엔드 로직과 원활하게 통신합니다.

### 주요 기능

*   **JavaFX UI**: `javafx-controls`, `javafx-fxml`, `javafx-media`를 사용한 풍부한 데스크톱 사용자 경험 제공.
*   **Spotify 연동**: `spotify-web-api-java` 라이브러리를 통해 Spotify의 방대한 음악 데이터에 접근하고 제어.
*   **데이터베이스 관리**: MySQL을 사용하여 사용자 정보 및 애플리케이션 데이터를 안정적으로 저장 및 관리.
*   **세련된 디자인**: `BootstrapFX`와 `Ikonli` 아이콘 팩을 사용하여 모던한 룩앤필 구현.

## 🛠 기술 스택

*   **Language**: Java 20
*   **Build Tool**: Maven
*   **UI Framework**: JavaFX 20.0.1
*   **Database**: MySQL 8.0.33
*   **External APIs**: Spotify Web API
*   **Libraries**:
    *   Lombok (코드 간소화)
    *   Logback / SLF4J (로깅)
    *   BootstrapFX (UI 스타일링)
    *   Ikonli (아이콘)

## ⚙️ 시작하기 (Getting Started)

### 사전 요구 사항 (Prerequisites)

*   **Java Development Kit (JDK) 20** 이상
*   **Maven**
*   **MySQL Server**
*   **Spotify Developer 계정** (API Key 필요)

## 📂 프로젝트 구조

```text
src
├── main
│   ├── java
│   │   └── com
│   │       └── youngtfy
│   │           ├── client      # 클라이언트 UI 및 로직 (ClientApp.java 등)
│   │           ├── server      # 서버 및 데이터 액세스 객체 (DAO)
│   │           ├── common      # 공통 유틸리티 및 모델
│   │           └── ...
│   └── resources               # FXML 파일, 설정 파일, 리소스 등
└── ...
```

## 📝 라이선스

이 프로젝트의 라이선스 정보는 문서를 참고하세요.

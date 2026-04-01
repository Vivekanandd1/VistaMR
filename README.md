# PreVista Automation Framework

Enterprise-level Test Automation Framework for Vista/Kreditz Application.

## 📋 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Test Categories](#test-categories)
- [Reporting](#reporting)
- [CI/CD](#cicd)
- [Contributing](#contributing)

---

## 🎯 Overview

This is a comprehensive test automation framework built with Selenium WebDriver, TestNG, and Java. It follows enterprise best practices including:

- **Page Object Model (POM)** design pattern
- **Dependency Injection** for test data
- **Parallel test execution**
- **Comprehensive logging** with Log4j2
- **Allure reporting** for detailed test results
- **Retry mechanism** for flaky tests
- **Screenshot on failure**
- **CI/CD integration** with GitHub Actions

---

## ✨ Features

| Feature | Description |
|---------|-------------|
| 🌐 Multi-Browser Support | Chrome, Firefox, Edge with headless mode |
| 🔐 Secure Configuration | Environment-based secrets management |
| 📊 Allure Reports | Interactive test reports with screenshots |
| 🔄 Auto-Retry | Failed tests automatically retry (configurable) |
| 📸 Screenshots | Automatic capture on test failure |
| 📝 Logging | Comprehensive logging with Log4j2 |
| 🔁 Parallel Execution | Thread-safe parallel test execution |
| 🎭 Test Data Generation | Dynamic data using JavaFaker |
| 🗄️ Database Support | MySQL database utilities included |
| 🌐 API Testing | REST Assured integration for API tests |

---

## 🛠 Technology Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Language | Java | 17 |
| Build Tool | Maven | 3.9+ |
| Browser Automation | Selenium WebDriver | 4.40.0 |
| Test Framework | TestNG | 7.10.2 |
| Reporting | Allure | 2.30.0 |
| Logging | Log4j2 | 2.24.3 |
| API Testing | REST Assured | 5.5.5 |
| Test Data | JavaFaker | 1.0.2 |
| Database | MySQL Connector | 9.2.0 |
| Excel | Apache POI | 5.4.0 |

---

## 📁 Project Structure

```
PreVista - Qwen/
├── src/
│   └── test/
│       ├── java/
│       │   ├── com/
│       │   │   └── vista/
│       │   │       ├── framework/       # Core framework components
│       │   │       │   ├── api/         # API client
│       │   │       │   ├── base/        # Base test classes
│       │   │       │   ├── config/      # Configuration management
│       │   │       │   ├── data/        # Test data factory
│       │   │       │   ├── database/    # Database utilities
│       │   │       │   ├── driver/      # WebDriver factory
│       │   │       │   ├── listeners/   # TestNG listeners
│       │   │       │   ├── utils/       # Utility classes
│       │   │       │   └── wait/        # Wait strategies
│       │   │       ├── pages/           # Page Object Models
│       │   │       └── tests/           # Test classes
│       └── resources/
│           ├── application.properties   # Default configuration
│           └── log4j2.xml              # Logging configuration
├── .env.example                        # Environment template
├── .github/workflows/main.yml          # CI/CD pipeline
├── pom.xml                             # Maven configuration
└── testng.xml                          # Test suite configuration
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd PreVista-Qwen
   ```

2. **Create environment file**
   ```bash
   cp .env.example .env
   ```

3. **Configure credentials**
   Edit `.env` file and add your test credentials:
   ```
   Email=your-test-email@example.com
   Password=your-test-password
   ```

4. **Build the project**
   ```bash
   mvn clean install -DskipTests
   ```

---

## ⚙️ Configuration

### Environment Variables (.env)

| Variable | Description | Default |
|----------|-------------|---------|
| `PROFILE` | Environment profile | `local` |
| `BROWSER` | Browser to use | `chrome` |
| `HEADLESS` | Run headless | `true` |
| `Email` | Test user email | - |
| `Password` | Test user password | - |
| `MAX_RETRY_COUNT` | Retry attempts | `2` |

### application.properties

Override default values in `.env` file:

```properties
# Timeouts (seconds)
IMPLICIT_WAIT=10
EXPLICIT_WAIT=30
PAGE_LOAD_TIMEOUT=60

# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=UserCred
DB_USER=root
DB_PASSWORD=


```

---

## ▶️ Running Tests

### Local Execution

```bash
# Run all tests
mvn clean test

# Run specific test class
mvn clean test -Dtest=LoginLogoutTest

# Run with visible browser
mvn clean test -DHEADLESS=false

# Run with different browser
mvn clean test -DBROWSER=firefox
```

### Parallel Execution

Tests run in parallel by default (configured in testng.xml):

```xml
<suite name="PreVista Automation Suite" parallel="methods" thread-count="2">
```

### Using Profiles

```bash
# Local environment
mvn clean test -DPROFILE=local

# CI environment (headless)
mvn clean test -DPROFILE=ci -Dheadless=true
```

---

## 📝 Test Categories

### UI Tests

| Test Class | Description |
|------------|-------------|
| `LoginLogoutTest` | Login and logout functionality |
| `CreateUserTest` | User creation and deletion |
| `ManualRequestTest` | End-to-end manual request flow |
| `CorporateRequestTest` | End-to-end corporate request flow |
| `DashboardFunctionsTest` | Dashboard search and filters |

### API Tests

| Test Class | Description |
|------------|-------------|
| `ApiTest` | API token generation and access |

---

## 📊 Reporting

### Allure Report

Generate and view Allure report:

```bash
# Generate report
mvn allure:report

# Serve report locally
mvn allure:serve
```

### Test Output

- **HTML Report**: `allure-report/index.html`
- **TestNG Report**: `test-output/index.html`
- **Logs**: `logs/` directory
- **Screenshots**: `screenshots/` directory

---

## 🔄 CI/CD

### GitHub Actions

The framework includes a complete CI/CD pipeline:

- **Triggers**: Push, PR, Schedule (weekdays 7 AM UTC), Manual
- **Features**:
  - Parallel test execution
  - Allure report generation
  - Artifact upload
  - Slack notifications
  - GitHub Pages deployment

### Required Secrets

Configure these in GitHub Repository Settings → Secrets:

| Secret | Description |
|--------|-------------|
| `EMAIL` | Test user email |
| `PASSWORD` | Test user password |
| `SLACK_WEBHOOK_URL` | Slack webhook for notifications |
| `GITHUB_TOKEN` | Auto-generated by GitHub |

### Manual Trigger

Go to Actions → "Java CI" → "Run workflow" to trigger manually with options:
- Select browser
- Toggle headless mode

---

## 🧪 Adding New Tests

### 1. Create Page Object

```java
public class MyPage {
    private final WebDriver driver;
    private final ElementUtils elementUtils;
    private final WaitStrategy wait;
    
    private final By myElement = By.id("my-element");
    
    public MyPage(WebDriver driver, WaitStrategy wait) {
        this.driver = driver;
        this.wait = wait;
        this.elementUtils = new ElementUtils(driver, wait);
    }
    
    @Step("Perform action")
    public void performAction() {
        elementUtils.click(myElement);
    }
}
```

### 2. Create Test Class

```java
public class MyTest extends BaseUiTest {
    
    @Test
    @Severity(SeverityLevel.CRITICAL)
    public void testMyFeature() {
        MyPage myPage = new MyPage(driver, wait);
        myPage.performAction();
        // assertions
    }
}
```

### 3. Add to testng.xml

```xml
<class name="com.vista.tests.MyTest"/>
```

---

## 📚 Framework Components

### ConfigManager
Centralized configuration with priority:
1. System Properties
2. Environment Variables
3. .env file
4. application.properties

### WebDriverFactory
Thread-safe WebDriver management with support for:
- Multiple browsers
- Headless mode
- Remote execution (Selenium Grid)

### WaitStrategy
Intelligent waiting mechanisms:
- Explicit waits
- Fluent waits
- Custom conditions
- Smart retry for stale elements

### TestDataFactory
Dynamic test data generation using JavaFaker:
- Names, emails, phone numbers
- Swedish Personnummer
- Company data
- Random text

### Listeners
- `TestListener`: Logging and screenshots
- `RetryAnalyzer`: Automatic retry on failure
- `AnnotationTransformer`: Apply retry to all tests

---

## 🐛 Troubleshooting

### Common Issues

**Tests fail with "Credentials not configured"**
- Ensure `.env` file exists with Email and Password

**Browser doesn't open**
- Check if browser is installed
- Try with `-DHEADLESS=false`

**Allure report not generating**
- Run `mvn allure:serve` after test execution

**Tests timing out**
- Increase timeout values in `.env`:
  ```
  EXPLICIT_WAIT=60
  PAGE_LOAD_TIMEOUT=120
  ```



---

## 👥 Contact

For questions,improvement or support, contact : Vivekanandd1@live.com.

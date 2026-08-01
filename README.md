[Launch details in Docker](RUN_INFO.md)
# [Book Club](https://book-club.qa.guru/) Web App — API + UI Test Automation Demo

> This is an automated test framework built in Java using Selenide for UI testing and RestAssured for API testing.
> The project is managed with Gradle, runs on JUnit 5, and uses Allure for comprehensive test reporting.

**It covers the core functionality of a web application, including:**

- Club management (create, read, update, delete)

- Review system (create, read, update, delete)

- User authentication (login, logout, registration)

- User management (create, read, update, delete)

**UI:**

- Page Object classes for web interface testing using Selenide

- Reusable UI components

**API:**

- Request/response models for API interactions

- JSON schema validation for API responses

- API clients for endpoint interactions

- RequestSpecifications for RestAssured

**General:**

- Environment-specific configurations (local, remote, docker)

- Structured test organization (API tests, UI tests)

## Contents:
____
* <a href="#tools">Technologies & Tools</a>

* <a href="#cases">Automated Test Scenarios</a>

* <a href="#project-structure">Project Structure</a>

* <a href="#jenkins">Jenkins Build</a>

* <a href="#console">Running Tests (Terminal)</a>

* <a href="#allure">Allure Report</a>

* <a href="#allure-testops">Allure TestOps Integration</a>

* <a href="#jira">Jira Integration</a>

* <a href="#tg">Telegram Alerts</a>

* <a href="#video">Video Example</a>
____
<a id="tools"></a>
## Technologies & Tools

<p align="center">  
<a href="https://www.jetbrains.com/idea/"><img src="media/logo/intellij-original.svg" width="50" height="50"  alt="IDEA"/></a>  
<a href="https://www.java.com/"><img src="media/logo/Java.svg" width="50" height="50"  alt="Java"/></a>  
<a href="https://github.com/"><img src="media/logo/GitHub.svg" width="50" height="50"  alt="Github"/></a>  
<a href="https://junit.org/junit5/"><img src="media/logo/JUnit5.svg" width="50" height="50"  alt="JUnit 5"/></a>  
<a href="https://gradle.org/"><img src="media/logo/Gradle.svg" width="50" height="50"  alt="Gradle"/></a>
<a href="https://www.selenium.dev/"><img src="media/logo/selenuim.svg" width="50" height="50"  alt="Selenium"/></a>  
<a href="https://selenide.org/"><img src="media/logo/Selenide.svg" width="50" height="50"  alt="Selenide"/></a>  
<a href="https://aerokube.com/selenoid/"><img src="media/logo/Selenoid.svg" width="50" height="50"  alt="Selenoid"/></a> 
<a href="https://rest-assured.io/"><img src="media/logo/RestAssured.svg" width="50" height="50"  alt="RestAssured"/></a>
<a href="https://github.com/allure-framework/allure2"><img src="media/logo/allure.svg" width="50" height="50"  alt="Allure"/></a> 
<a href="https://qameta.io/"><img src="media/logo/AllureTestOps.svg" width="50" height="50"  alt="Allure TestOps"/></a>
<a href="https://www.jenkins.io/"><img src="media/logo/Jenkins.svg" width="50" height="50"  alt="Jenkins"/></a>  
<a href="https://www.atlassian.com/ru/software/jira/"><img src="media/logo/Jira.svg" width="50" height="50"  alt="Jira"/></a>  
</p>


- Programming language: [Java](https://www.java.com/ru/)
- API automation: [Rest Assured](https://rest-assured.io/)
- UI automation: [Selenide](https://selenide.org/)
- Test framework: [JUnit5](https://github.com/junit-team/junit5)
- Build system: [Gradle](https://gradle.org/)
- CI/CD: [Jenkins](https://www.jenkins.io/)
- Reporting: [Allure](https://github.com/allure-framework)
- Test run notifications: Telegram-bot
- Integration with  [Allure TestOps](https://qameta.io/)
- Integration with  [Jira Software](https://www.atlassian.com/software/jira)
___

<a id="#cases"></a>
## Examples of Automated Test Scenarios
____
**API Tests:**
- Register user with valid credentials
- Login with valid credentials
- Create a club
- Update club description
- Create a review for a club you own

**UI Tests:**
- Register a new user via web interface
- Login via web interface
- Logout via web interface
- User registration[API], session setup[localStorage], and page opening[UI]

**Hybrid Tests (API + UI):**
- Register and login via API, set up the session via localStorage, create a club via API, and verify the club details via UI
- Register, login, create and update a club via API, set up the session via localStorage, and verify the updated club details via UI
- Register, login, create a club and a review via API, set up the session via localStorage, and verify the review is displayed on the UI

**Negative Scenarios:**
- Registration with an already existing user
- Registration with an invalid username format
- Registration with invalid endpoint (404)
- Login fails with case-modified username
- Login fails with wrong password
- Non-member cannot create review for a club
- Cannot update another user's review

____
<a id="project-structure"></a>
## 📁 Project Structure

```text
src/
└── test/
    └── java/
        └── mslt.qa/
            ├── allure/                     # Allure listeners and custom attachments
            ├── api/                        # API clients for endpoint interactions
            ├── config/                     # Configuration classes
            ├── helper/                     # Utilities and test data builders
            ├── models/                     # POJO models for requests/responses
            │   ├── clubs/                  # Club-related models
            │   ├── review/                 # Review-related models
            │   └── localstorage/           # Local storage auth models
            ├── pages/                      # Page Object classes for UI tests
            │   └── components/             # Reusable UI components
            ├── spec/                       # RestAssured Request/ResponseSpecifications
            │   ├── clubs/                  # Clubs API specs
            │   ├── login/                  # Login API specs
            │   ├── logout/                 # Logout API specs
            │   ├── register/               # Registration API specs
            │   ├── review/                 # Reviews API specs
            │   └── user/                   # User API specs
            └── tests/                      # Tests grouped by type (API, UI)
                ├── api/                    # API tests
                │   ├── club/               # Clubs API tests
                │   ├── login/              # Login API tests
                │   ├── logout/             # Logout API tests
                │   ├── register/           # Registration API tests
                │   ├── review/             # Reviews API tests
                │   └── user/               # User API tests
                ├── without_spec/           # Tests without specifications
                └── ui/                     # UI tests
                    ├── club/               # Clubs UI tests
                    ├── login/              # Login UI tests
                    ├── logout/             # Logout UI tests
                    ├── register/           # Registration UI tests
                    └── review/             # Reviews UI tests

resources/
├── config/                                 # Environment configurations (local, remote, docker)
└── json/
└── schemas/                                # JSON schemas for API response validation
    ├── clubs/                              # Clubs response schemas
    ├── login/                              # Login response schemas
    ├── logout/                             # Logout response schemas
    ├── register/                           # Registration response schemas
    ├── review/                             # Reviews response schemas
    └── update/                             # User update response schemas
```
----
<a id="jenkins"></a>
## <img width="25" style="vertical-align:middle" title="Jenkins" src="media/logo/Jenkins.svg"> </a> Jenkins Build <a target="_blank" href="https://jenkins.qa.guru/job/C40-ma1m2-redsoft/"></a>
To access Jenkins, registration on the resource is required [Jenkins](https://jenkins.qa.guru/).
To start the build, click the <code>Build Now</code> button.
____
<p align="center">  
<a href="https://jenkins.qa.guru/view/java-students/job/C40-ma1m2-book-club/" target="_blank" rel="noopener noreferrer"><img src="media/screen/Jenkins.png" alt="Jenkins" width="850"/></a>  
</p>

### **Build Parameters in Jenkins:**

- *browser (default is chrome)*
- *browserVersion (browser version, default is an empty string = latest version)*
- *browserSize (browser window size, default is 1366x768)*
- *env (configuration file, default is local)*


<a id="console"></a>
## Running Tests (Terminal)
___
***Local run:***
```bash  
./gradlew clean test -Denv=local
```
***Docker run:***
```bash  
./gradlew clean test -Denv=docker
```
***Remote run in Selenoid:***
```bash  
./gradlew clean test -Denv=remote
```
___
<a id="allure"></a>
## <img alt="Allure" height="20" src="media/logo/allure.svg" width="20"/></a> <a name="Allure"></a>Allure [Report](https://jenkins.qa.guru/view/java-students/job/C40-ma1m2-book-club/3/allure-report/)</a>
___

### Overview page of Allure Report

<p align="center">  
<img title="Allure Overview Dashboard" src="media/screen/Allure Overview.png" width="850">  
</p>  

### Test-cases

<p align="center">  
<img title="Allure Tests" src="media/screen/Allure Tests.png" width="850">  
</p>

### Graphs

  <p align="center">  
<img title="Allure Graphics" src="media/screen/Graphs.png" width="850">
</p>

___
<a id="allure-testops"></a>
## <img alt="Allure" height="25" src="media/logo/AllureTestOps.svg" width="25"/></a> Integration with <a target="_blank" href="https://allure.autotests.cloud/project/5304/launches">Allure TestOps</a>
Allure TestOps is a full-stack test management platform that unites automated and manual testing in one workspace.
____
### *Allure TestOps Dashboard*

<p align="center">  
<img title="Allure TestOps Dashboard" src="media/screen/DashboardTestOps.png" width="850">  
</p>  

### Auto and Manual Test-cases

<p align="center">  
<img title="Allure Tests" src="media/screen/TestOps Tests.png" width="850">  
</p>

___
<a id="jira"></a>
## <img alt="Jira" height="25" src="media/logo/Jira.svg" width="25"/></a> Integration with <a target="_blank" href="https://jira.qa.guru/browse/REF-14">Jira</a>
____
<p align="center">  
<img title="Jira" src="media/screen/jira.png" width="850">  
</p>

____
<a id="tg"></a>

## <img width="30" style="vertical-align:middle" title="Telegram" src="media/logo/Telegram.svg">Telegram notifications:

Upon completion of each test run, a <code>Telegram bot</code> automatically processes the test run results and sends a report to a dedicated chat

____  
<p align="center">  
<img title="Telegram" src="media/screen/tg.png" width="500">  
</p>

____  

<a id="video"></a>
## Video Example in Selenoid:
The Allure report attaches a screenshot from the final step and a video of the entire test run to each test case.
<p align="center">
  <img src="media/video/createReview.gif" width="700" alt="video">
</p>


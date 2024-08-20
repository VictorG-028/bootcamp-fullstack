# Backend

Aluno: Victor Gabriel Tenório Oliveira

### How to Run

1. Execute `git clone`.
2. Open the project in IntelliJ.
3. Add/link both Maven projects (`app-user-api` and `app-integration-api`).
4. Run the Maven lifecycle `install` script:
    - Open the Maven sidebar.
    - Click the dropdown of each Maven project.
    - Click the dropdown for "Lifecycle".
    - Select `install`.
    - Click the green "Run" button.
5. Open a terminal in the project folder.
6. With Docker Desktop running, execute `docker-compose -f docker-compose.yml up -d`.
7. Open Swagger on both APIs:
    - [http://localhost:8081/api/swagger-ui/index.html](http://localhost:8081/api/swagger-ui/index.html)
    - [http://localhost:8082/api/swagger-ui/index.html](http://localhost:8082/api/swagger-ui/index.html)

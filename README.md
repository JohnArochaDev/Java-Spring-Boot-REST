# SafePass Backend

This project is the backend for the SafePass password manager. It provides a secure API for managing user credentials, authentication, and other related functionalities.

## Features

1. **User Authentication**: Secure user authentication using JWT.
2. **Password Management**: CRUD operations for user credentials.
3. **Data Encryption**: Secure storage and transmission of data.
4. **Password Strength Checking**: Check the strength of passwords and detect breaches.
5. **API Documentation**: OpenAPI documentation for the API endpoints.
6. **Environment Configuration**: Configurable environment variables for different environments.

## Installation

1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/safepass-backend.git
    cd safepass-backend
    ```

2. Build the project using Maven:
    ```sh
    ./mvnw clean install
    ```

3. Create an `application.properties` file in the `src/main/resources` directory and add your environment variables:
    ```properties
    server.port=8080
    spring.datasource.url=jdbc:mysql://localhost:3306/safepass
    spring.datasource.username=your_database_username
    spring.datasource.password=your_database_password
    spring.jpa.hibernate.ddl-auto=update
    jwt.secret=your_jwt_secret
    ```

## Usage

### Development

To start the development server:
```sh
./mvnw spring-boot:run
```

### Production
To build and run the production server:
```sh
./mvnw clean package
java -jar target/safepass-backend-0.0.1-SNAPSHOT.jar
```
## API Documentation
The project uses OpenAPI for API documentation. You can access the documentation at:

http://localhost:8080/swagger-ui.html

## Project Structure

```sh
passwordManager
  .idea
  .mvn
  src/
      main/
          java/
              com/
                  example/
                      demo/
                          config/
                              OpenApiConfig.java
                          controller/
                              LoginCredentialController.java
                              Userontroller.java
                          dto/
                              AuthRequest.java
                              AuthResponse.java
                              LoginRequest.java
                          filter/
                              JwtAuthFIlter.java
                          repository/
                              UserRepository.java
                              LoginCredentialRepository.java
                          roles/
                              Role
                          security/
                              PasswordEncoderConfig.java
                              SecurityConfig.java
                          service/
                              JwtService.java
                              LoginCredentialService.java
                              UserService.java
                          util/
                              EncryptionUtil.java
                              JwtKeyGenerator.java
                          LoginCredential.java
                          PasswordManagerApplication.java
                          User.java
          resources/
              application.properties
  .gitignore
  mvnw
  mvnw.cmd
  pom.xml
  README.md
```

API Endpoints
Authentication
POST /users/register: Register a new user.
POST /users/login: Login a user.
User Management
GET /users: Get all users.
GET /users/{id}: Get user by ID.
PUT /users/{id}: Update user details.
DELETE /users/{id}: Delete user.
Credentials
GET /credentials: Get all login credentials.
GET /credentials/{id}: Get login credential by ID.
POST /credentials/{userId}: Create a new login credential.
PUT /credentials/{id}: Update login credential.
DELETE /credentials/{id}: Delete login credential.

## Additional Information
Disclaimer
While this project strives to offer secure password management, the author is not responsible for any security breaches, data loss, or financial loss that may result from the use of this software. Users are responsible for implementing their own security practices and safeguarding their data. This project was developed as part of a school assignment to demonstrate proficiency in web development and security practices.

License
This project is licensed under the Creative Commons Attribution-NonCommercial 4.0 International Public License.

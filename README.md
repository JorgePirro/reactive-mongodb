# Beer Service

## Overview
Beer Service is a reactive CRUD application built with Spring WebFlux and MongoDB. It allows you to manage a catalog of beers, leveraging the power of non-blocking, event-driven programming for high-performance applications.

---

## Features
- Reactive endpoints for managing beers (create, read, update, delete).
- Integration with MongoDB as a NoSQL database.
- Designed with Spring WebFlux for building asynchronous, non-blocking services.
- Dockerized setup for easy deployment and testing.

---

## Setup Instructions

### Prerequisites
Ensure the following tools are installed on your system:
- **Docker** and **Docker Compose**: For running MongoDB and other dependencies.
- **Java 17** or higher: Required for running the Spring application.
- **Maven**: For building the application.

---

### Steps to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/JorgePirro/reactive-mongodb.git
   ```

2. Build the application:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
   Alternatively, you can run the main class directly:
   ```bash
   ReactiveMongoApplication.main()
   ```

   **Note**: The application automatically starts MongoDB using Docker Compose during initialization. You do not need to manually start Docker Compose.

4. Import the Postman collection included in the project resources to test the endpoints.

---

## API Endpoints

The application exposes the following endpoints under `/api/beer`:

| Method   | Endpoint         | Description                |
|----------|------------------|----------------------------|
| `POST`   | `/api/beer`      | Create a new beer.         |
| `GET`    | `/api/beer/{id}` | Retrieve a beer by ID.     |
| `GET`    | `/api/beer`      | Retrieve all beers.        |
| `PUT`    | `/api/beer/{id}` | Update an existing beer.   |
| `DELETE` | `/api/beer/{id}` | Delete a beer by ID.       |

### Sample Beer Object
```json
{
    "name": "Corona Extra",
    "beerStyle": "Lager",
    "upc": "11111",
    "quantityOnHand": 122,
    "price": 5.99,
    "createdDate": "2024-12-04T13:35:25.013",
    "lastModifiedDate": "2024-12-04T13:35:25.013"
}
```

---

## Testing
### Using Postman
1. Import the provided Postman collection located in the `postman/` directory.
2. Use the preconfigured requests to interact with the Beer Service.

### Running Unit Tests
Run the unit tests to ensure the application behaves as expected:
```bash
mvn test
```

---

## Technologies Used
- **Spring WebFlux**: For reactive programming.
- **Spring Data MongoDB**: To interact with MongoDB.
- **MongoDB**: As the primary database.
- **Docker**: For containerized database setup.
- **Java 17**: For modern, robust backend development.

---

## Contributing
1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/my-new-feature`.
3. Commit your changes: `git commit -m 'Add new feature'`.
4. Push to the branch: `git push origin feature/my-new-feature`.
5. Open a pull request.

---

## License
This project is licensed under the MIT License. See the `LICENSE` file for more details.


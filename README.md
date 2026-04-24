# Smart Campus RESTful API (JAX-RS)

## Overview

This project implements a RESTful web service using JAX-RS (Jersey) for managing a Smart Campus system. The API supports operations for rooms, sensors, and sensor readings, simulating a scalable backend service for IoT-based infrastructure.

The system runs on an embedded Grizzly HTTP server and follows REST architectural principles including stateless communication, resource-based URIs, appropriate HTTP methods, and JSON data exchange.

---

## System Architecture

The application follows a layered architecture to promote modularity and maintainability.

### Resource Layer

This layer handles HTTP requests and defines API endpoints using JAX-RS annotations.

Classes:

* RoomResource
* SensorResource
* SensorReadingResource
* DiscoveryResource

---

### Model Layer

This layer defines the core entities of the system:

* Room
* Sensor
* SensorReading

Each model is implemented as a POJO with constructors, getters, and setters.

---

### Storage Layer

The application uses an in-memory DataStore implemented with thread-safe collections:

```java
ConcurrentHashMap<String, Room>
ConcurrentHashMap<String, Sensor>
```

This ensures safe concurrent access and prevents race conditions.

---

### Exception Handling Layer

Custom exceptions and ExceptionMapper classes are implemented to:

* Prevent exposure of internal server errors
* Return structured JSON responses
* Use appropriate HTTP status codes

---

## Base URL

[http://localhost:8080/api/v1](http://localhost:8080/api/v1)

---

## API Endpoints

### Discovery

GET /api/v1

---

### Room Management

GET /rooms
POST /rooms
GET /rooms/{roomId}
DELETE /rooms/{roomId}

---

### Sensor Management

GET /sensors
POST /sensors
GET /sensors?type=CO2

---

### Sensor Readings

GET /sensors/{sensorId}/readings
POST /sensors/{sensorId}/readings

---

## Example JSON

### Room

```json
{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 40,
  "sensorIds": []
}
```

### Sensor

```json
{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}
```

---

## Build and Run

Build the project:

```bash
mvn clean package
```

Run the server:

```bash
mvn exec:java
```

---

## Sample curl Commands

```bash
# Discovery
curl http://localhost:8080/api/v1

# Create room
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"LIB-301","name":"Library Quiet Study","capacity":40}'

# Get all rooms
curl http://localhost:8080/api/v1/rooms

# Register sensor
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"TEMP-001","type":"Temperature","status":"ACTIVE","roomId":"LIB-301"}'

# Filter sensors
curl "http://localhost:8080/api/v1/sensors?type=Temperature"

# Add sensor reading
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{"id":"r1","timestamp":1710320000000,"value":22.5}'

# Get readings
curl http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

---

## Error Handling

The API uses ExceptionMapper classes to return structured error responses.

Scenarios:

* Room deletion with existing sensors → 409 Conflict
* Invalid room reference → 422 Unprocessable Entity
* Sensor in maintenance → 403 Forbidden
* Unexpected errors → 500 Internal Server Error

Example response:

```json
{
  "error": "Room cannot be deleted because it contains sensors"
}
```

---

## Part 1 – Resource Lifecycle

JAX-RS follows a per-request lifecycle, meaning a new resource instance is created for each incoming request.
To manage shared data, a separate DataStore is used. Thread safety is ensured using ConcurrentHashMap to avoid race conditions in concurrent environments.

---

## Part 1 – HATEOAS

HATEOAS enables clients to dynamically navigate the API using links provided in responses.

This reduces reliance on static documentation and improves API usability and flexibility.

---

## Part 2 – IDs vs Full Objects

Returning full objects reduces the need for additional client requests but increases payload size. Returning IDs reduces bandwidth but requires more requests.

This API returns full objects to improve usability.

---

## Part 2 – DELETE Idempotency

DELETE is idempotent because repeated requests do not change the system state after the first execution, even if responses differ.

---

## Part 3 – @Consumes JSON

If a client sends a request in an unsupported format, JAX-RS returns a 415 Unsupported Media Type response.

---

## Part 3 – Query vs Path Parameter

Query parameters are preferred for filtering because they are optional and flexible. They also allow multiple filters in a single request.

---

## Part 4 – Sub-Resource Locator

The sub-resource locator pattern improves code organisation by separating nested resources into dedicated classes, making the system more scalable and maintainable.

---

## Part 5 – HTTP 422 Justification

HTTP 422 is used when the request is valid but contains incorrect data, such as referencing a non-existent room.

---

## Part 5 – Security Risks

Exposing stack traces can reveal internal system details such as class structure and file paths, which may be exploited by attackers.

A global exception mapper prevents this by returning generic error responses.

---

## Report Answers

### Part 1 

**Resource Lifecycle (Expanded):**
JAX-RS manages resources using a per-request lifecycle, meaning a new instance is created for each HTTP request. This design ensures thread safety because instances are not shared across threads. However, it also means instance variables cannot be used for persistent or shared data. Instead, shared data must be stored externally (e.g., in a DataStore using ConcurrentHashMap) to maintain consistency across requests.

**HATEOAS (Expanded):**
HATEOAS (Hypermedia as the Engine of Application State) allows APIs to include navigational links in responses. This enables clients to dynamically discover available actions without relying on external documentation. It improves usability, flexibility, and reduces tight coupling between client and server.

---

### Part 2 

**IDs vs Full Objects (Expanded):**
Returning IDs results in smaller payloads and better performance but requires additional API calls to retrieve related data. Returning full objects increases payload size but provides complete information in a single response, improving usability. This API prioritizes usability by returning full objects.

**DELETE Idempotency (Expanded):**
DELETE operations are idempotent because performing the same request multiple times results in the same system state. The first request deletes the resource, while subsequent requests do not change anything further, even if they return a different response (e.g., 404).

---

### Part 3 

**@Consumes(JSON) Behaviour:**
When @Consumes(MediaType.APPLICATION_JSON) is used, the API only accepts JSON input. If a client sends data in another format (e.g., text/plain or application/xml), JAX-RS automatically throws a NotSupportedException and returns a 415 Unsupported Media Type response.

**QueryParam vs PathParam (Expanded):**
Query parameters are better suited for filtering because they are optional and flexible, allowing multiple filtering conditions. Path parameters are used for identifying specific resources and are not suitable for dynamic filtering scenarios.

---

### Part 4 
**Sub-Resource Locator Benefits (Expanded):**
The sub-resource locator pattern improves modularity by delegating nested resource handling to separate classes. This avoids large monolithic controllers, improves readability, and enhances scalability and maintainability in large APIs.

---

### Part 5 

**HTTP 422 Justification (Expanded):**
HTTP 422 (Unprocessable Entity) is more appropriate when a request is syntactically valid but semantically incorrect, such as referencing a non-existent entity within a valid JSON payload.

**Security Risks of Stack Traces (Expanded):**
Exposing stack traces can reveal sensitive internal details such as class names, package structures, file paths, and framework information. Attackers can use this information to identify vulnerabilities and exploit the system. To prevent this, the API uses a global ExceptionMapper to return generic error messages and avoid leaking internal implementation details.

---

## Conclusion

This project demonstrates the development of a RESTful API using JAX-RS to effectively manage rooms, sensors, and sensor readings within a smart campus environment.

The system adheres to core REST principles, incorporating a clear resource hierarchy, support for nested endpoints, and robust exception handling mechanisms. The use of in-memory storage combined with concurrent data structures ensures reliable and thread-safe handling of multiple client requests.

Overall, the implementation provides a functional and extensible backend service, forming a solid foundation for future enhancements in a real-world smart campus system.

# Gilded Rose Inventory Management

A full-stack inventory management application developed as part of the SIS Full Stack Technical Assessment.

The application implements the Gilded Rose inventory rules and allows users to manage inventory items and advance the inventory by one day.

## Tech Stack

**Backend**
- Java JDK 21
- Spring Boot
- Maven
- JUnit 5
- Spring Boot Test / MockMvc

**Frontend**
- React
- JavaScript
- Vite
- Vitest
- React Testing Library

## Features

- Display inventory items with their name, SellIn and Quality.
- Add inventory items through a form with input validation.
- Advance the inventory by one day.
- Apply different Quality calculation rules depending on the item type.
- Handle expired items and Quality boundaries.
- Display loading state and errors when updating inventory.
- Automatically set Sulfuras Quality to 80.

## Prerequisites

Ensure the following are installed:

- Java JDK 21, compatible with the version configured in `pom.xml`
- Node.js and npm
- Git

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/Gelada00/sis-gilded-rose-assessment.git
cd sis-gilded-rose-assessment
```

### 2. Run the Backend

From the project root:

```bash
./mvnw spring-boot:run
```

The Spring Boot API runs at:

`http://localhost:8080`

### 3. Run the Frontend

Open a second terminal:

```bash
cd frontend
npm ci
npm run dev
```

Open the application at:

`http://localhost:5173`

The frontend uses a Vite development proxy to forward `/api` requests to the Spring Boot backend.

**Both the backend and frontend must be running to update the inventory through the user interface.**

## REST API

### Update Inventory

**Endpoint**

`POST /api/inventory/update`

**Request Body**

```json
[
  {
    "name": "Normal Item",
    "sellIn": 5,
    "quality": 10
  },
  {
    "name": "Aged Brie",
    "sellIn": 3,
    "quality": 20
  },
  {
    "name": "Sulfuras",
    "sellIn": 0,
    "quality": 80
  }
]
```

**Response**

```json
[
  {
    "name": "Normal Item",
    "sellIn": 4,
    "quality": 9
  },
  {
    "name": "Aged Brie",
    "sellIn": 2,
    "quality": 21
  },
  {
    "name": "Sulfuras",
    "sellIn": 0,
    "quality": 80
  }
]
```

The API accepts a JSON list and returns the updated inventory after one day.

The application does not require a database. Inventory is managed in frontend state and sent to the backend for calculation.

## Inventory Rules

| Item | Quality Behaviour |
|---|---|
| Normal Item | Decreases by 1 per day, or 2 after expiry |
| Aged Brie | Increases by 1 per day, or 2 after expiry |
| Backstage passes | Increases by 1, by 2 when SellIn ≤ 10, or by 3 when SellIn ≤ 5; drops to 0 after expiry |
| Sulfuras | SellIn and Quality remain unchanged; Quality is 80 |
| Conjured | Decreases by 2 per day, or 4 after expiry |

For ordinary items, Quality is limited to the range 0–50. SellIn can become negative.

**Assessment clarification:** The supplied example appears to contain a typo in the final Conjured item's SellIn value. The implementation follows the stated rule that SellIn decreases by one each day.

## Running Tests

### Backend Tests

From the project root:

```bash
./mvnw test
```

The backend tests cover item-specific inventory rules, boundary conditions, validation, REST API behaviour and the complete eight-item example provided in the assessment.

### Frontend Tests

From the `frontend` directory:

```bash
npm ci
npx vitest run
```

The frontend tests cover item creation, form validation, Sulfuras handling, inventory rendering, API success and API failure handling.

## Building the Application

### Backend

From the project root:

```bash
./mvnw clean package
```

### Frontend

From the `frontend` directory:

```bash
npm ci
npm run build
```

The frontend production build is generated in `frontend/dist`.

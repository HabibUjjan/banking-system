# 🏦 Digital Banking System

A full-stack, enterprise-level Digital Banking Application built with modern technologies. This system simulates real-world banking operations including secure transactions, account management, and fraud detection.


## 🚀 Overview

This project is designed to demonstrate a production-grade banking system architecture with a clean separation between frontend and backend.

It includes:

* Secure REST APIs
* Modern user interface
* Transaction processing system
* Fraud detection mechanisms


## 🧰 Tech Stack

### 🔹 Backend

* Java (Spring Boot)
* Spring Security
* JWT Authentication
* MySQL Database
* Maven

### 🔹 Frontend

* React (Vite)
* Axios (API Integration)
* HTML5, CSS3, JavaScript

## ✨ Features

### 👤 User Management

* User Registration & Login
* Secure Authentication (JWT-based)
* Role-based access control

### 💳 Account Management

* Create and manage bank accounts
* View account balance
* Generate account numbers

### 💸 Transactions

* Money transfer between accounts
* Transaction history tracking
* Real-time balance updates

### 🛡️ Security

* Encrypted authentication
* Secure API endpoints
* Input validation

### 🚨 Fraud Detection

* Suspicious transaction monitoring
* Rule-based detection system

### 🔔 Notifications

* Transaction alerts
* System notifications


## 📂 Project Structure

banking-system/
│
├── backend/        # Spring Boot Application
├── frontend/       # React Application
├── docs/           # Documentation & Screenshots
└── README.md


## ⚙️ Installation & Setup

### 🔧 Backend Setup
bash
cd backend
mvn clean install
mvn spring-boot:run

Backend will run on:
👉 http://localhost:8080


### 💻 Frontend Setup

bash
cd frontend
npm install
npm run dev


Frontend will run on:
👉 http://localhost:5173

## 🔗 API Integration

Frontend communicates with backend using REST APIs.

Example:
bash
POST /api/auth/login
POST /api/transactions/transfer
GET /api/accounts/balance

## 🔐 Security Implementation

* JWT-based authentication
* Password encryption
* Secure API access
* Role-based authorization


## 🧪 Testing

* Backend tested using Postman
* API endpoints validated
* Transaction flows verified


## 🚀 Future Improvements

* Two-Factor Authentication (2FA)
* Email/SMS notifications
* Docker deployment
* Cloud hosting (AWS)

## 👨‍💻 Author

Habibullah Ujjan

## 📄 License

This project is for educational and demonstration purposes.

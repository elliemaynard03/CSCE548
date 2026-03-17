# CSCE548

Site is live at https://elliemaynard03.github.io/CSCE548/

# Wellness Tracker

A full-stack wellness tracking application built with Java, Maven, Javalin, MySQL, HTML, CSS, and JavaScript.

Users can:
- create accounts
- create habits
- create daily logs
- update daily logs
- update habit complete/incomplete
- delete users
- delete habits
- delete logs
- get all a user's habits and logs
- get all a logs habits
- get list of all users

## Tech Stack

- **Back end:** Java, Maven, Javalin
- **Database:** MySQL
- **Hosting:** Render + Railway
- **Front end:** HTML, CSS, JavaScript
- **Version control:** GitHub

---

## Features

- Create and manage users
- Create habits for a selected user
- Create daily logs for a selected user
- Save habit completion status for a specific daily log
- REST API architecture with DAO, business, and service layers

---

## Project Structure

- **Data layer:** DAO classes + MySQL tables
- **Business layer:** manager/service classes
- **Service layer:** Javalin API endpoints
- **Client layer:** static HTML/JS front end

Main tables:
- `AppUser`
- `Habit`
- `DailyLog`
- `HabitEntry`

---

## Prerequisites

Before running this project, install:

- Java JDK 17+
- Maven
- Git
- MySQL
- IntelliJ IDEA or VS Code
- Render account
- Railway account

---

## Quick Start

## 1. Download the Repo

Clone:
```bash
git clone <your-repo-url>
cd <repo-folder>

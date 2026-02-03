CREATE DATABASE IF NOT EXISTS wellness_tracker;
USE wellness_tracker;

-- Drop tables in correct dependency order
DROP TABLE IF EXISTS HabitEntry;
DROP TABLE IF EXISTS DailyLog;
DROP TABLE IF EXISTS Habit;
DROP TABLE IF EXISTS AppUser;

-- =============================
-- 1) AppUser
-- =============================
CREATE TABLE AppUser (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR(120) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =============================
-- 2) Habit
-- =============================
CREATE TABLE Habit (
    habit_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(120) NOT NULL,
    unit VARCHAR(30) NOT NULL DEFAULT 'count',
    target_value DECIMAL(8,2),
    is_active TINYINT(1) NOT NULL DEFAULT 1,

    CONSTRAINT uq_user_habit UNIQUE (user_id, name),
    CONSTRAINT ck_unit CHECK (unit IN ('count','minutes','hours','steps','oz','cups','servings','pages')),
    CONSTRAINT ck_target CHECK (target_value IS NULL OR target_value >= 0),

    CONSTRAINT fk_habit_user
        FOREIGN KEY (user_id)
        REFERENCES AppUser(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =============================
-- 3) DailyLog
-- =============================
CREATE TABLE DailyLog (
    daily_log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    log_date DATE NOT NULL,

    sleep_hours DECIMAL(4,2),
    mood_rating INT,
    stress_level INT,
    energy_level INT,
    notes VARCHAR(500),

    CONSTRAINT uq_user_date UNIQUE (user_id, log_date),
    CONSTRAINT ck_sleep CHECK (sleep_hours IS NULL OR (sleep_hours >= 0 AND sleep_hours <= 24)),
    CONSTRAINT ck_mood CHECK (mood_rating IS NULL OR (mood_rating BETWEEN 1 AND 10)),
    CONSTRAINT ck_stress CHECK (stress_level IS NULL OR (stress_level BETWEEN 1 AND 10)),
    CONSTRAINT ck_energy CHECK (energy_level IS NULL OR (energy_level BETWEEN 1 AND 10)),

    CONSTRAINT fk_dailylog_user
        FOREIGN KEY (user_id)
        REFERENCES AppUser(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

-- =============================
-- 4) HabitEntry (junction table)
-- =============================
CREATE TABLE HabitEntry (
    habit_entry_id INT AUTO_INCREMENT PRIMARY KEY,
    daily_log_id INT NOT NULL,
    habit_id INT NOT NULL,

    completed TINYINT(1) NOT NULL DEFAULT 0,
    actual_value DECIMAL(8,2),
    entry_note VARCHAR(300),

    CONSTRAINT uq_dailylog_habit UNIQUE (daily_log_id, habit_id),
    CONSTRAINT ck_actual CHECK (actual_value IS NULL OR actual_value >= 0),

    CONSTRAINT fk_entry_dailylog
        FOREIGN KEY (daily_log_id)
        REFERENCES DailyLog(daily_log_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_entry_habit
        FOREIGN KEY (habit_id)
        REFERENCES Habit(habit_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

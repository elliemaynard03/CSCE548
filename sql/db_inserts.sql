USE wellness_tracker;

-- =============================
-- Users
-- =============================
INSERT INTO AppUser (email, full_name) VALUES
('ellie@example.com', 'Ellie Maynard'),
('alex@example.com', 'Alex Johnson');

-- =============================
-- Habits (5 per user)
-- =============================
INSERT INTO Habit (user_id, name, unit, target_value, is_active) VALUES
(1, 'Water Intake', 'oz', 80, 1),
(1, 'Steps', 'steps', 10000, 1),
(1, 'Pilates', 'minutes', 30, 1),
(1, 'Meditation', 'minutes', 10, 1),
(1, 'Protein Goal', 'servings', 3, 1),

(2, 'Water Intake', 'oz', 64, 1),
(2, 'Steps', 'steps', 8000, 1),
(2, 'Gym Workout', 'minutes', 45, 1),
(2, 'Read', 'pages', 15, 1),
(2, 'Sleep Routine', 'hours', 8, 1);

-- =============================
-- Daily Logs (10 per user)
-- =============================
INSERT INTO DailyLog (user_id, log_date, sleep_hours, mood_rating, stress_level, energy_level, notes) VALUES
(1, '2026-01-20', 7.5, 8, 4, 7, 'Felt good.'),
(1, '2026-01-21', 6.0, 6, 6, 5, 'Busy day.'),
(1, '2026-01-22', 8.0, 9, 3, 8, 'Great sleep.'),
(1, '2026-01-23', 7.0, 7, 5, 6, 'Normal day.'),
(1, '2026-01-24', 8.5, 9, 2, 9, 'High energy.'),
(1, '2026-01-25', 7.2, 8, 4, 7, 'Good routine.'),
(1, '2026-01-26', 6.8, 7, 5, 6, 'Slightly tired.'),
(1, '2026-01-27', 7.9, 8, 4, 7, 'Solid day.'),
(1, '2026-01-28', 6.5, 6, 7, 5, 'Stressful.'),
(1, '2026-01-29', 7.8, 8, 4, 7, 'Back on track.'),

(2, '2026-01-20', 7.0, 7, 5, 6, 'Okay day.'),
(2, '2026-01-21', 8.0, 8, 4, 7, 'More rested.'),
(2, '2026-01-22', 6.2, 6, 6, 5, 'Not enough sleep.'),
(2, '2026-01-23', 7.5, 7, 5, 6, 'Steady.'),
(2, '2026-01-24', 8.4, 9, 3, 8, 'Great mood.'),
(2, '2026-01-25', 7.1, 7, 5, 6, 'Normal.'),
(2, '2026-01-26', 6.9, 6, 7, 5, 'Work stress.'),
(2, '2026-01-27', 7.6, 8, 4, 7, 'Better.'),
(2, '2026-01-28', 8.2, 9, 3, 8, 'Productive day.'),
(2, '2026-01-29', 7.3, 8, 4, 7, 'Consistent.');

-- =============================
-- Habit Entries (80 rows)
-- =============================
INSERT INTO HabitEntry (daily_log_id, habit_id, completed, actual_value, entry_note)
SELECT dl.daily_log_id, h.habit_id,
       IF(RAND() > 0.2, 1, 0),
       ROUND(RAND()*100, 2),
       'Auto generated'
FROM DailyLog dl
JOIN Habit h ON dl.user_id = h.user_id;

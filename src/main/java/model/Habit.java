package main.java.model;

public class Habit {
    private int habitId;
    private int userId;
    private String name;
    private String unit;
    private Double targetValue; // nullable
    private boolean isActive;

    public Habit() {}

    public Habit(int habitId, int userId, String name, String unit, Double targetValue, boolean isActive) {
        this.habitId = habitId;
        this.userId = userId;
        this.name = name;
        this.unit = unit;
        this.targetValue = targetValue;
        this.isActive = isActive;
    }

    public int getHabitId() { return habitId; }
    public void setHabitId(int habitId) { this.habitId = habitId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getTargetValue() { return targetValue; }
    public void setTargetValue(Double targetValue) { this.targetValue = targetValue; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    @Override
    public String toString() {
        return "Habit{" +
                "habitId=" + habitId +
                ", userId=" + userId +
                ", name='" + name + '\'' +
                ", unit='" + unit + '\'' +
                ", targetValue=" + targetValue +
                ", isActive=" + isActive +
                '}';
    }
}


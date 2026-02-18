package main.java.model;


public class HabitEntry {
    private int habitEntryId;
    private int dailyLogId;
    private int habitId;
    private boolean completed;
    private Double actualValue; // nullable
    private String entryNote;   // nullable

    public HabitEntry() {}

    public HabitEntry(int habitEntryId, int dailyLogId, int habitId,
                      boolean completed, Double actualValue, String entryNote) {
        this.habitEntryId = habitEntryId;
        this.dailyLogId = dailyLogId;
        this.habitId = habitId;
        this.completed = completed;
        this.actualValue = actualValue;
        this.entryNote = entryNote;
    }

    public int getHabitEntryId() { return habitEntryId; }
    public void setHabitEntryId(int habitEntryId) { this.habitEntryId = habitEntryId; }

    public int getDailyLogId() { return dailyLogId; }
    public void setDailyLogId(int dailyLogId) { this.dailyLogId = dailyLogId; }

    public int getHabitId() { return habitId; }
    public void setHabitId(int habitId) { this.habitId = habitId; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public Double getActualValue() { return actualValue; }
    public void setActualValue(Double actualValue) { this.actualValue = actualValue; }

    public String getEntryNote() { return entryNote; }
    public void setEntryNote(String entryNote) { this.entryNote = entryNote; }

    @Override
    public String toString() {
        return "HabitEntry{" +
                "habitEntryId=" + habitEntryId +
                ", dailyLogId=" + dailyLogId +
                ", habitId=" + habitId +
                ", completed=" + completed +
                ", actualValue=" + actualValue +
                ", entryNote='" + entryNote + '\'' +
                '}';
    }
}


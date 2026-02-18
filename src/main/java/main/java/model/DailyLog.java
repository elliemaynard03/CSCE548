package main.java.model;

import java.sql.Date;

public class DailyLog {
    private int dailyLogId;
    private int userId;
    private Date logDate;
    private Double sleepHours;   // nullable
    private Integer moodRating;  // nullable
    private Integer stressLevel; // nullable
    private Integer energyLevel; // nullable
    private String notes;        // nullable

    public DailyLog() {}

    public DailyLog(int dailyLogId, int userId, Date logDate, Double sleepHours,
                    Integer moodRating, Integer stressLevel, Integer energyLevel, String notes) {
        this.dailyLogId = dailyLogId;
        this.userId = userId;
        this.logDate = logDate;
        this.sleepHours = sleepHours;
        this.moodRating = moodRating;
        this.stressLevel = stressLevel;
        this.energyLevel = energyLevel;
        this.notes = notes;
    }

    public int getDailyLogId() { return dailyLogId; }
    public void setDailyLogId(int dailyLogId) { this.dailyLogId = dailyLogId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public Date getLogDate() { return logDate; }
    public void setLogDate(Date logDate) { this.logDate = logDate; }

    public Double getSleepHours() { return sleepHours; }
    public void setSleepHours(Double sleepHours) { this.sleepHours = sleepHours; }

    public Integer getMoodRating() { return moodRating; }
    public void setMoodRating(Integer moodRating) { this.moodRating = moodRating; }

    public Integer getStressLevel() { return stressLevel; }
    public void setStressLevel(Integer stressLevel) { this.stressLevel = stressLevel; }

    public Integer getEnergyLevel() { return energyLevel; }
    public void setEnergyLevel(Integer energyLevel) { this.energyLevel = energyLevel; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return "DailyLog{" +
                "dailyLogId=" + dailyLogId +
                ", userId=" + userId +
                ", logDate=" + logDate +
                ", sleepHours=" + sleepHours +
                ", moodRating=" + moodRating +
                ", stressLevel=" + stressLevel +
                ", energyLevel=" + energyLevel +
                ", notes='" + notes + '\'' +
                '}';
    }
}


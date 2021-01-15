package com.flyf;

import java.time.LocalDate;

public class GanttChartActivity {
    private String title;
    private LocalDate startDate;
    private LocalDate endDate;
    private int topLevelID;
    private int middleLevelID;
    private int bottomLevelID;

    public GanttChartActivity(String title, LocalDate startDate, LocalDate endDate, int topLevelID, int middleLevelID,
            int bottomLevelID) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.topLevelID = topLevelID;
        this.middleLevelID = middleLevelID;
        this.bottomLevelID = bottomLevelID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getTopLevelID() {
        return topLevelID;
    }

    public void setTopLevelID(int topLevelID) {
        this.topLevelID = topLevelID;
    }

    public int getMiddleLevelID() {
        return middleLevelID;
    }

    public void setMiddleLevelID(int middleLevelID) {
        this.middleLevelID = middleLevelID;
    }

    public int getBottomLevelID() {
        return bottomLevelID;
    }

    public void setBottomLevelID(int bottomLevelID) {
        this.bottomLevelID = bottomLevelID;
    }

}

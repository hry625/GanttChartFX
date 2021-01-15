package com.flyf;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.util.Callback;

public class GanttChartViewController implements Initializable {
    private static int columnCount = 0;

    ArrayList<GanttChartActivity> activityList = new ArrayList<>();
    ArrayList<GanttChartActivity> topLevelList;
    ArrayList<GanttChartActivity> midLevelList;
    ArrayList<GanttChartActivity> bottomLevelList;
    GanttChartActivity projectActivity;
    private static LocalDate calenderStartDate;
    private static LocalDate calenderEndDate;
    private static int numberOfCalenderDays;

    @FXML
    private Label titleLabel;
    @FXML
    private TreeTableView<GanttChartActivity> ganttChartTreeTableView;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Create a place holder activity for the project.
        GanttChartActivity projectActivity = new GanttChartActivity("Project", LocalDate.of(2021, 01, 01),
                LocalDate.of(2021, 03, 01), 0, 0, 0);

        if (projectActivity.getStartDate().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
            calenderStartDate = projectActivity.getStartDate();
        } else {
            calenderStartDate = projectActivity.getStartDate().with(DayOfWeek.MONDAY);
        }
        if (projectActivity.getEndDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            calenderEndDate = projectActivity.getEndDate();
        } else {
            calenderEndDate = projectActivity.getEndDate().plusWeeks(1).with(DayOfWeek.SUNDAY);
        }

        numberOfCalenderDays = (int) ChronoUnit.DAYS.between(calenderStartDate, calenderEndDate) + 1;

        topLevelList = new ArrayList<>();
        midLevelList = new ArrayList<>();
        bottomLevelList = new ArrayList<>();

        TreeItem<GanttChartActivity> projectItem = new TreeItem<>(projectActivity);
        convertActivityToGanttChartActivity();

        for (GanttChartActivity topGanttChartActivity : topLevelList) {
            TreeItem<GanttChartActivity> topLevelItem = new TreeItem<>(topGanttChartActivity);
            for (GanttChartActivity middleGanttChartActivity : midLevelList) {
                if (middleGanttChartActivity.getTopLevelID() == topGanttChartActivity.getTopLevelID()) {
                    TreeItem<GanttChartActivity> middleLevelItem = new TreeItem<>(middleGanttChartActivity);
                    for (GanttChartActivity bottomGanttChartActivity : bottomLevelList) {
                        if (middleGanttChartActivity.getTopLevelID() == bottomGanttChartActivity.getTopLevelID()
                                && middleGanttChartActivity.getMiddleLevelID() == bottomGanttChartActivity
                                        .getMiddleLevelID()) {
                            TreeItem<GanttChartActivity> bottomLevelItem = new TreeItem<>(bottomGanttChartActivity);
                            middleLevelItem.getChildren().add(bottomLevelItem);
                            middleLevelItem.setExpanded(true);
                        }
                    }
                    topLevelItem.getChildren().add(middleLevelItem);
                    topLevelItem.setExpanded(true);
                }
            }
            projectItem.getChildren().add(topLevelItem);
            projectItem.setExpanded(true);

        }

        TreeTableColumn<GanttChartActivity, String> treeTableColumn1 = new TreeTableColumn<>("Name");
        treeTableColumn1.setPrefWidth(180.0);
        treeTableColumn1.setMinWidth(180.0);
        TreeTableColumn<GanttChartActivity, String> treeTableColumn2 = new TreeTableColumn<>("Start Date");
        treeTableColumn2.setPrefWidth(80);
        treeTableColumn2.setMinWidth(80.0);
        TreeTableColumn<GanttChartActivity, String> treeTableColumn3 = new TreeTableColumn<>("End Date");
        treeTableColumn3.setPrefWidth(80.0);
        treeTableColumn3.setMinWidth(80.0);

        treeTableColumn1.setCellValueFactory(new TreeItemPropertyValueFactory<>("title"));
        treeTableColumn2.setCellValueFactory(new TreeItemPropertyValueFactory<>("startDate"));
        treeTableColumn3.setCellValueFactory(new TreeItemPropertyValueFactory<>("endDate"));

        ganttChartTreeTableView.getColumns().add(treeTableColumn1);
        ganttChartTreeTableView.getColumns().add(treeTableColumn2);
        ganttChartTreeTableView.getColumns().add(treeTableColumn3);

        LocalDate currentDate = calenderStartDate;
        TreeTableColumn<GanttChartActivity, String> currentWeekColumn;
        Locale locale = new Locale("EN");
        while (currentDate.isBefore(calenderEndDate)) {

            int currentWeek = currentDate.get(WeekFields.of(locale).weekOfYear());
            int currentYear = currentDate.getYear();

            String firstDayOfWeekString = currentDate.format(DateTimeFormatter.ofPattern("LLL d"));

            currentWeekColumn = new TreeTableColumn<>(
                    currentYear + " Week " + (currentWeek) + " (" + firstDayOfWeekString + ")");
            currentWeekColumn.getStyleClass().add("gantt-chart-week-column");

            LocalDate tempDate = currentDate;

            for (int i = 0; i < 7; i++) {

                TreeTableColumn<GanttChartActivity, String> currentDateColumn = new TreeTableColumn<>(
                        tempDate.format(DateTimeFormatter.ofPattern("dd")));

                if (tempDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                    currentDateColumn.getStyleClass().add("gantt-chart-day-column-weekend");
                } else if (tempDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    currentDateColumn.getStyleClass().add("gantt-chart-day-column-weekend");
                    currentDateColumn.setStyle("-fx-border-color: transparent red transparent transparent");

                } else {
                    currentDateColumn.getStyleClass().add("gantt-chart-day-column-weekday");
                }

                currentWeekColumn.getColumns().add(currentDateColumn);
                currentDateColumn.setCellFactory(
                        new Callback<TreeTableColumn<GanttChartActivity, String>, TreeTableCell<GanttChartActivity, String>>() {
                            @Override
                            public TreeTableCell<GanttChartActivity, String> call(
                                    TreeTableColumn<GanttChartActivity, String> param) {
                                return new TreeTableCell<GanttChartActivity, String>() {

                                    @Override
                                    protected void updateItem(String item, boolean empty) {
                                        setText(null);
                                        getStyleClass().clear();
                                        super.updateItem(item, empty);
                                        GanttChartActivity activity = getTreeTableRow().getItem();
                                        int columnIndex = columnCount % numberOfCalenderDays;
                                        if (activity != null) {
                                            int daysBeforeActivityStart = (int) ChronoUnit.DAYS
                                                    .between(calenderStartDate, activity.getStartDate());
                                            int daysUntilActivityEnd = (int) ChronoUnit.DAYS.between(calenderStartDate,
                                                    activity.getEndDate());

                                            int rowIndex = getTreeTableRow().getIndex() % 7 + 1;

                                            if (columnIndex >= daysBeforeActivityStart
                                                    && columnIndex <= daysUntilActivityEnd) {
                                                int dayOffSets = (int) ChronoUnit.DAYS.between(calenderStartDate,
                                                        projectActivity.getStartDate());
                                                setText(Integer.toString(columnIndex - dayOffSets));
                                                setTextFill(Color.GREEN);
                                                getStyleClass().add("gantt-chart-cell" + rowIndex);

                                            } else if (columnIndex % 7 == 6 || columnIndex % 7 == 5) {

                                                getStyleClass().add("gantt-chart-day-column-weekend");

                                            } else {
                                                getStyleClass().add("gantt-chart-day-column-weekday");
                                            }
                                        } else if (columnIndex % 7 == 6 || columnIndex % 7 == 5) {
                                            getStyleClass().add("gantt-chart-day-column-weekend");

                                        }
                                        columnCount++;
                                        if (columnCount == numberOfCalenderDays) {
                                            columnCount = 0;
                                        }

                                    }

                                };
                            }
                        });
                tempDate = tempDate.plusDays(1);
            }

            ganttChartTreeTableView.getColumns().add(currentWeekColumn);
            currentDate = currentDate.plusDays(7);
        }

        ganttChartTreeTableView.setRoot(projectItem);

    }

    public void convertActivityToGanttChartActivity() {
        // fake data:
        GanttChartActivity activity1 = new GanttChartActivity("Test Activity", LocalDate.of(2021, 01, 02),
                LocalDate.of(2021, 02, 01), 1, 0, 0);
        GanttChartActivity activity2 = new GanttChartActivity("Test Activity", LocalDate.of(2021, 01, 02),
                LocalDate.of(2021, 01, 20), 1, 1, 0);
        GanttChartActivity activity3 = new GanttChartActivity("Test Activity", LocalDate.of(2021, 01, 02),
                LocalDate.of(2021, 01, 14), 1, 1, 1);
        GanttChartActivity activity4 = new GanttChartActivity("Test Activity", LocalDate.of(2021, 01, 15),
                LocalDate.of(2021, 02, 01), 1, 2, 0);
        GanttChartActivity activity5 = new GanttChartActivity("Test Activity", LocalDate.of(2021, 02, 02),
                LocalDate.of(2021, 02, 15), 1, 3, 0);
        GanttChartActivity activity6 = new GanttChartActivity("Test Activity", LocalDate.of(2021, 01, 02),
                LocalDate.of(2021, 02, 01), 2, 0, 0);
        GanttChartActivity activity7 = new GanttChartActivity("Test Activity", LocalDate.of(2021, 01, 02),
                LocalDate.of(2021, 02, 20), 3, 0, 0);
        activityList.add(activity1);
        activityList.add(activity2);
        activityList.add(activity3);
        activityList.add(activity4);
        activityList.add(activity5);
        activityList.add(activity6);
        activityList.add(activity7);
        // You could convert your activity class to the ganttchart activity in this
        // function
        // I use arraylist of GanttChartActivity for convinience, you can change
        // activitylist as an arraylist of your own activity class
        // I am using topLevelID, midLevelID and bottomLevelID to indicate the
        // relationship of different activities:
        // -Activity 1 1.0.0
        // --subActivity 1.1.0
        // ---grandChildActivity 1.1.1

        for (GanttChartActivity activity : activityList) {
            if (activity.getMiddleLevelID() == 0 && activity.getBottomLevelID() == 0) {
                topLevelList.add(activity);
            } else if (activity.getBottomLevelID() == 0) {
                midLevelList.add(activity);
            } else {
                bottomLevelList.add(activity);
            }
        }

    }

}

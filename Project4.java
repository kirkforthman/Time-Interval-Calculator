/**Project 4 - UMGC Intermediate Programming
 * Kirk Forthman
 * October 3, 2023
 * Program launches a GUI allowing user to enter time intervals and a single time. Buttons inform user where sub-intervals,
 * overlaps, disjoint intervals are as well as if either interval contains the time in question.
 */

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Project4 extends Application {
    public TextField tfinterval1Start = new TextField();
    public TextField tfinterval1End = new TextField();
    public TextField tfinterval2Start = new TextField();
    public TextField tfinterval2End = new TextField();
    public TextField tfTimeToCheck = new TextField();
    public TextField tfCheckTimeResult = new TextField();
    public Button btnCompareIntervals = new Button("Compare Intervals");
    public Button btnCheckTime = new Button("Check Time");
    public GridPane gridPane = new GridPane();

    public String compareIntervalText(Time int1Start, Time int1End, Time int2Start, Time int2End) throws InvalidTime {
        Interval<Time> int1 = new Interval<>(int1Start, int1End);
        Interval<Time> int2 = new Interval<>(int2Start, int2End);
        if (int1.subinterval(int2)) {
            return "Interval 1 is a sub-interval of interval 2.";
        } else if (int2.subinterval(int1)){
            return "Interval 2 is a sub-interval of interval 1.";
        } else if (int2.overlaps(int1) || int1.overlaps(int2)){
            return "The intervals overlap.";
        } else {
            return "The intervals are disjoint.";
        }
    } // end compareIntervalText method (attached to button)

    public String checkTimeText(Time int1Start, Time int1End, Time int2Start, Time int2End) throws InvalidTime {
        Interval<Time> int1 = new Interval<>(int1Start, int1End);
        Interval<Time> int2 = new Interval<>(int2Start, int2End);
        Time targetTime = new Time(tfTimeToCheck.getText());
        if (int1.within(targetTime) && int2.within(targetTime)) {
            return "Both intervals contain the time " + tfTimeToCheck.getText();
        } else if (int1.within(new Time(tfTimeToCheck.getText()))) {
            return "Only interval 1 contains the time " + tfTimeToCheck.getText();
        } else if (int2.within(new Time(tfTimeToCheck.getText()))) {
            return "Only interval 2 contains the time " + tfTimeToCheck.getText();
        } else {
            return "Neither interval contains the time " + tfTimeToCheck.getText();
        }
    } // end checkTimeText method (attached to button)

    // Define Time Object (Inner Class)
    public static class Time implements Comparable <Time>{
        private final int hours;
        private final int minutes;
        private final String meridian;


        public Time(String input) throws InvalidTime, NumberFormatException, IndexOutOfBoundsException {
            try {
                String[] hoursAndElse = input.split(":",2);
                this.hours = Integer.parseInt(hoursAndElse[0]);
                String[] minutesAndMeridian = hoursAndElse[1].split(" ", 2);
                this.minutes = Integer.parseInt(minutesAndMeridian[0]);
                this.meridian = minutesAndMeridian[1];
            } catch (NumberFormatException e) {
                throw new InvalidTime("Invalid Time Format Entered (HH:MM AM/PM)");
            } catch (IndexOutOfBoundsException iobex) {
                throw new InvalidTime("Incorrect Time Entry");
            } if (this.hours > 12 || this.hours < 1 || this.minutes > 59 || this.minutes < 0) {
                throw new InvalidTime("Invalid Numeric Entry");
            } if (!(this.meridian.equalsIgnoreCase("am") || this.meridian.equalsIgnoreCase("pm"))) {
                throw new InvalidTime("Invalid Meridian");
            }
        }

        public int getHours() {
            return this.hours;
        }

        public int getMinutes() {
            return this.minutes;
        }

        public String getMeridian() {
            return this.meridian;
        }

        @Override
        public int compareTo(Time o) {
            if (o.getMeridian().equalsIgnoreCase(this.meridian) && o.getHours() == this.hours && o.getMinutes() == this.minutes) {
                return 0;
            } else if (o.getMeridian().equalsIgnoreCase("pm") && this.meridian.equalsIgnoreCase("am")) {
                return -1;
            } else if (o.getMeridian().equalsIgnoreCase("am") && this.meridian.equalsIgnoreCase("pm")) {
                return 1;
            } else {
                if (this.hours == 12 && o.getHours() >= 1 && o.getHours() < 12) {
                    return -1;
                } else if (this.hours >= 1 && this.hours < 12 && o.getHours() == 12) {
                    return 1;
                } else if (this.hours > o.getHours()) {
                    return 1;
                } else if (this.hours < o.getHours()) {
                    return -1;
                } else {
                    if (this.minutes > o.getMinutes()) {
                        return 1;
                    } else {
                        return -1;
                    }
                }
            }
        } // end compareTo method

        @Override
        public String toString() {
            return this.hours + ":" + this.minutes + " " + this.meridian;
        }

    } // end Time Object

    // Define Interval Object (Inner Class)
    public class Interval<E extends Time> implements Comparable<E> {
        private final E start;
        private final E end;

        public Interval(E start, E end) throws InvalidTime {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(E o) { // unused method
            return 0;
        } //unused

        public boolean within(E object) {
            if (object.compareTo(this.start) > 0 && object.compareTo(this.end) < 0) {
                return true;
            } else if (object.compareTo(this.start) == 0 && object.compareTo(this.end) < 0) {
                return true;
            } else if (object.compareTo(this.start) > 0 && object.compareTo(this.end) == 0) {
                return true;
            } else return object.compareTo(this.start) == 0 && object.compareTo(this.end) == 0;
        }

        public boolean subinterval(Interval<Time> testInterval) {
            return testInterval.within(this.start) && testInterval.within(this.end);
        }

        public boolean overlaps(Interval<Time> testInterval) {
            return testInterval.within(this.start) || testInterval.within(this.end);
        }
    }

    // build GUI
    @Override
    public void start(Stage primaryStage) {
        tfCheckTimeResult.setEditable(false); // result cannot be entered
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(new Label("Start Time"), 1 , 0);
        gridPane.add(new Label("End Time"), 2, 0);
        gridPane.add(new Label("Time Interval 1"), 0, 1);
        gridPane.add(new Label("Time Interval 2"), 0, 2);
        gridPane.add(tfinterval1Start, 1, 1);
        gridPane.add(tfinterval1End, 2, 1);
        gridPane.add(tfinterval2Start, 1, 2);
        gridPane.add(tfinterval2End, 2, 2);
        gridPane.add(btnCompareIntervals, 0,3, 3, 1);
        btnCompareIntervals.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(new Label("Time to Check"), 0, 4);
        gridPane.add(tfTimeToCheck, 1, 4, 2, 1);
        tfTimeToCheck.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(btnCheckTime, 0, 5, 3, 1);
        btnCheckTime.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        gridPane.add(tfCheckTimeResult, 0, 6,3,1);
        tfCheckTimeResult.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        // Compare Intervals button action handler
        btnCompareIntervals.setOnAction(e -> {
            try {
                tfCheckTimeResult.setText(compareIntervalText(new Time(tfinterval1Start.getText()),
                        new Time(tfinterval1End.getText()), new Time(tfinterval2Start.getText()), new Time(tfinterval2End.getText())));
            } catch (InvalidTime ex) {
                tfCheckTimeResult.setText(ex.errorMessage);
            }
        });

        // Check Time button action handler
        btnCheckTime.setOnAction(e -> {
            try {
                tfCheckTimeResult.setText(checkTimeText(new Time(tfinterval1Start.getText()),
                        new Time(tfinterval1End.getText()), new Time(tfinterval2Start.getText()), new Time(tfinterval2End.getText())));
            } catch (InvalidTime ex) {
                tfCheckTimeResult.setText(ex.errorMessage);
            }
        });

        // build window
        Scene scene = new Scene(gridPane,400, 250);
        primaryStage.setTitle("Time Interval Checker");
        primaryStage.setScene(scene);
        primaryStage.show();

    } // end Project4 (GUI)


    /**
     *Main method is used for IDEs with limited JavaFX support.
     * It is not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch();
    } // end main

    // Invalid Time Exception message prints to result text field.
    public static class InvalidTime extends Exception {
        private final String errorMessage;
        public InvalidTime(String errorMessage) {
            super(errorMessage);
            this.errorMessage = errorMessage;
        }
    } // end InvalidTime (Exception Class)

} // end Project 4






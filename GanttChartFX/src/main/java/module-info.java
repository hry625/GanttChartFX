module com.flyf {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.flyf to javafx.fxml;
    exports com.flyf;
}
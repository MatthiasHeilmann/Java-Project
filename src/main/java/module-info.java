module com.example.javaproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javaproject to javafx.fxml;
    exports com.example.javaproject;

    opens com.example.javaproject.Tables to javafx.fxml;
    exports com.example.javaproject.Tables;
}
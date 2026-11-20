module com.example.sklep {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.sklep.controller.admin to javafx.fxml;
    opens com.example.sklep.controller.klient to javafx.fxml;
    opens com.example.sklep.controller.lobby to javafx.fxml;

    exports com.example.sklep;
}

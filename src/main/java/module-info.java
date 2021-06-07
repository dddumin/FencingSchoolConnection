module org.dumin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires java.rmi;
    requires com.jfoenix;

    opens org.dumin to javafx.fxml, javafx.base;
    exports org.dumin;

    opens model to com.google.gson, javafx.base;
}


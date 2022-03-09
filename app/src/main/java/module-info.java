module gui {
    requires javafx.controls;
    requires java.sql;
    exports gui;
    opens db to javafx.base;
}
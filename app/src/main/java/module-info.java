module gui {
    requires javafx.controls;
    requires java.sql;
    exports gui;
    opens entities to javafx.base;
}
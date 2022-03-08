module gui {
    requires javafx.controls;
    requires java.sql;
    exports gui;
    opens sqljdbc to javafx.base;
}
module me.wilux.dit216ux {
    requires javafx.controls;
    requires javafx.fxml;


    opens me.wilux.dit216ux to javafx.fxml;
    exports me.wilux.dit216ux;
}
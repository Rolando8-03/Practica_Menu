module ni.edu.uam.practica_menu {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.practica_menu to javafx.fxml;
    exports ni.edu.uam.practica_menu;
}
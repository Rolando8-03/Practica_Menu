package ni.edu.uam.practica_menu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class InventarioApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(
                InventarioApplication.class.getResource("inventario-view.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load(), 1000, 680);

        stage.setTitle("Distribuidora El Güegüense - Inventario");
        stage.setMinWidth(850);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
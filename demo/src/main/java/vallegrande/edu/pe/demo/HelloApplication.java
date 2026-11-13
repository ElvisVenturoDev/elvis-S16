package vallegrande.edu.pe.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("view/pedido-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1120, 740);
        stage.setTitle("Sistema de Pedidos de Imprenta - Elvis Venturo Prieto");
        stage.setMinWidth(980);
        stage.setMinHeight(660);
        stage.setScene(scene);
        stage.show();
    }
}

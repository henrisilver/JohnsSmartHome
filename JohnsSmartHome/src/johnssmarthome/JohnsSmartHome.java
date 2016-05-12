/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johnssmarthome;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author henrisilver
 */
public class JohnsSmartHome extends Application {
    public static ResourceBundle BUNDLE = ResourceBundle.getBundle("johnssmarthome.bundle",
            Locale.forLanguageTag("pt"));
    private Stage primaryStage;
    
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.primaryStage.setTitle("Jonh's Smart Home");
        this.primaryStage.setOnCloseRequest(e -> {
            e.consume();
            closeProgram();
        });
        this.primaryStage.setMinHeight(600);
        this.primaryStage.setMinWidth(800);
        this.primaryStage.setMaxHeight(600);
        this.primaryStage.setMaxWidth(800);
        this.primaryStage.setResizable(false);
        
        AnchorPane root;
        try {
            root = FXMLLoader.load(
                    getClass().getResource("view/Welcome.fxml"),
                    JohnsSmartHome.BUNDLE);
            
            root.setId("AnchorPane");

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(JohnsSmartHome.class.getName()).log(Level.SEVERE, null, ex);
            closeProgram();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private void closeProgram() {
        this.primaryStage.close();
    }
}

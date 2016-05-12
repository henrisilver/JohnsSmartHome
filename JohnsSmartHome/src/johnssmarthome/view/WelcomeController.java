/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johnssmarthome.view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import johnssmarthome.JohnsSmartHome;

/**
 * FXML Controller class
 *
 * @author marce
 */
public class WelcomeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    @FXML
    public void encryptDecryptOnClickButton(Event event){
        System.out.println("encryptDecryptOnClickButton");
        Scene currentScene = ((Node) event.getSource()).getScene();
        Stage currentStage = (Stage) currentScene.getWindow();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("EncryptDecryptView.fxml"),
                JohnsSmartHome.BUNDLE);
        Parent root;
        
        try {
            root = (Parent) loader.load();
            Scene scene = new Scene(
                    root,
                    currentStage.getScene().getWidth(),
                    currentStage.getScene().getHeight());
            
            currentStage.setScene(scene);
        } catch (IOException ex) {
            Logger.getLogger(WelcomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void simulatorOnClickButton(Event event){
        System.out.println("simulatorOnClickButton");
    }
}

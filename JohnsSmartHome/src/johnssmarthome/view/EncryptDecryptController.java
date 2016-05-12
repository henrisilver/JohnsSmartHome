/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johnssmarthome.view;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import johnssmarthome.JohnsSmartHome;

/**
 * FXML Controller class
 *
 * @author marce
 */
public class EncryptDecryptController implements Initializable {

    private FileChooser fileChooser;
    private Desktop desktop = Desktop.getDesktop();
    
    @FXML
    private TextArea message;
    
    @FXML
    private TextArea path;
    
    @FXML
    private TextArea result;
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fileChooser = new FileChooser();
    }    
    
    @FXML
    public void backButtonOnClick(Event event){
        System.out.println("backButtonOnClick");
        Scene currentScene = ((Node) event.getSource()).getScene();
        Stage currentStage = (Stage) currentScene.getWindow();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("Welcome.fxml"),
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
    public void teste (Event event){
    
        result.setText("Pepperoni");
    }
    
    @FXML
    public void openMessageFileOnClick(Event event){
        Scene currentScene = ((Node) event.getSource()).getScene();
        Stage currentStage = (Stage) currentScene.getWindow();
        
        configureFileChooser(fileChooser);
                    File file = fileChooser.showOpenDialog(currentStage);
                    if (file != null) {
                        openFile(file,path,message);
                    }
    }

    private static void configureFileChooser(final FileChooser fileChooser){                           
        fileChooser.setTitle("Abrir arquivo...");
        fileChooser.setInitialDirectory(
            new File(System.getProperty("user.home"))
        );
    }
    
    @FXML
    public void encrypt(Event event){
        System.out.println("Criptografar");
    }
    
    @FXML
    public void decrypt(Event event){
        System.out.println("Descriptografar");
    }
    
    private void openFile(File file, TextArea path, TextArea contentHolder) {
        try {  
            if (file.canRead()){
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String content = "";
                String temp;
                while ((temp = reader.readLine()) != null){
                    content = content + temp + "\n";
                }
                path.setText(file.getAbsolutePath());
                contentHolder.setText(content);
                
            }
        } catch (IOException ex) {
            Logger.getLogger(EncryptDecryptController.class.getName()).log(
                Level.SEVERE, null, ex
            );
        }
    }
}

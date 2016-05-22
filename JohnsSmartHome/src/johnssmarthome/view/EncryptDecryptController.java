/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johnssmarthome.view;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
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
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import johnssmarthome.JohnsSmartHome;
import johnssmarthome.RSAGHM;

/**
 * FXML Controller class
 *
 * @author marce
 */
public class EncryptDecryptController implements Initializable {

    private FileChooser fileChooser;
    private final Desktop desktop;
    private RSAGHM rsaghm;
    
    @FXML
    private TextArea message;
    
    @FXML
    private TextArea path;
    
    @FXML
    private TextArea result;

    public EncryptDecryptController() {
        this.desktop = Desktop.getDesktop();
    }
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fileChooser = new FileChooser();
        rsaghm = new RSAGHM();
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
    public void openMessageFile(Event event){
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
        result.setText(rsaghm.encrypt(message.getText()).toString());
        //result.setText(message.getText());
    }
    
    @FXML
    public void decrypt(Event event){
        result.setText(rsaghm.decrypt(
                new BigInteger(
                        message.getText().substring(0, message.getText().length()-1))
            )
        );
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
    
    @FXML
    private void saveFile(Event event){
        System.out.println("saveFile");
        
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("result.txt", "UTF-8");
            writer.println(result.getText());
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(EncryptDecryptController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null){
                writer.close();
            }
        }
        /*
        File file = new File("result.txt");
        // if file doesnt exists, then create it
        if (!file.exists()) {
            try {
                file.createNewFile();
                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(result.getText());
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(EncryptDecryptController.class.getName()).log(Level.SEVERE, null, ex);
            }
	}*/
    }
}

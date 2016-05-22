/*
 * John's Smart Home
 * Autores:
 * Giuliano Barbosa Prado
 * Henrique de Almeida Machado da Silveira
 * Marcello de Paula Ferreira Costa
 */
package johnssmarthome.view;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import johnssmarthome.RSAGHM;

public class EncryptDecryptController implements Initializable {
    
    // Objeto que representa o algoritmo RSA, versão GHM
    private RSAGHM rsaghm;

    // Abaixo, membros da interface gráfica
    private FileChooser fileChooser;
    private final Desktop desktop;
    
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
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fileChooser = new FileChooser();
        rsaghm = new RSAGHM();
    }

    // Método utilizado quando o usuário deseja abrir um arquivo
    @FXML
    public void openMessageFile(Event event) {
        Scene currentScene = ((Node) event.getSource()).getScene();
        Stage currentStage = (Stage) currentScene.getWindow();

        configureFileChooser(fileChooser);
        File file = fileChooser.showOpenDialog(currentStage);
        if (file != null) {
            openFile(file, path, message);
        }
    }

    // Método auxiliar de configuração do objeto que permite escolher arquivos
    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Abrir arquivo...");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    // Método utilizado para criptografar o conteúdo de uma mensagem
    @FXML
    public void encrypt(Event event) {
        try {
            result.setText(rsaghm.encrypt(message.getText()).toString());
        } catch(Exception e) {
            result.setText("Conversão inválida! Insira o texto que deseja criptografar.");
        }
        
    }

    // Método utilizado para realizar a descriptografia.
    // Caso o valor fornecido para descriptografar não seja um valor numérico,
    // uma excessão é detectada. Assim, uma mensagem apropriada é mostrada
    // ao usuário
    @FXML
    public void decrypt(Event event) {
        try {
            BigInteger bi = new BigInteger(message.getText());
            result.setText(rsaghm.decrypt(bi));
        } catch(Exception e) {
            result.setText("Conversão inválida! O texto que deseja descriptografar deve ser um número.");
        }
    }

    // Método auxiliar utilizado para abrir um arquivo e ler seu conteúdo,
    // caractere por caractere, preservando inclusive quebras de linha
    private void openFile(File file, TextArea path, TextArea contentHolder) {
        try {
            if (file.canRead()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuilder content = new StringBuilder();
                int c;
                
                // Lê o arquivo, caractere por caractere
                while ((c = reader.read()) != -1) {
                    
                    content.append((char) c);
                }
                path.setText(file.getAbsolutePath());
                contentHolder.setText(content.toString());

            }
        } catch (IOException ex) {
            Logger.getLogger(EncryptDecryptController.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        }
    }

    // Método utilizado para escrever o resultado da
    // criptografia/descriptografia em um arquivo
    @FXML
    private void saveFile(Event event) {
        System.out.println("saveFile");

        PrintWriter writer = null;
        try {
            writer = new PrintWriter("result.txt", "UTF-8");
            writer.print(result.getText());
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(EncryptDecryptController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}

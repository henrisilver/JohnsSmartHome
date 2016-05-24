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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    
    private ArrayList<Integer> sizes = new ArrayList<>();
    
    private boolean isEncryptedContent = false;

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
    public void encrypt(Event event){
        
        // It is required to split the input into batches with a maximum
        // of 128 characters. That's because a BigInteger cannot work
        // properly with strings bigger than that.
        String textToEncrypt = message.getText();
        String temp = null;
        StringBuilder encryptedStringBuilder = new StringBuilder();
        int textSize = textToEncrypt.length();
        int charsProcessedSoFar = 0;
        int size = 127;
        sizes.clear();
        
        try {
            while (charsProcessedSoFar != textSize){
                if (textSize - charsProcessedSoFar < 127){
                    size = textSize - charsProcessedSoFar;
                }
                //System.out.println("Size: " + size);
                temp = textToEncrypt.substring(charsProcessedSoFar, charsProcessedSoFar + size);
                //System.out.println(temp);
                String resultingText = rsaghm.encrypt(temp).toString();
                sizes.add(resultingText.length());
                encryptedStringBuilder.append(resultingText);
                charsProcessedSoFar = charsProcessedSoFar + size;
            }

            result.setText(encryptedStringBuilder.toString());
            isEncryptedContent = true;
        } catch (Exception e){
            result.setText("Conversão inválida! Insira o texto que deseja criptografar.");
            Alert alert = new Alert(AlertType.ERROR);
            StringBuilder contentText = new StringBuilder();
            contentText.append("Algo errado ocorreu. Se o problema persistir, reinicie a aplicação\n");
            alert.setTitle("Atenção");
            alert.setHeaderText(null);
            alert.setContentText(contentText.toString());

            alert.showAndWait();
        }
    }

    // Método utilizado para realizar a descriptografia.
    // Caso o valor fornecido para descriptografar não seja um valor numérico,
    // uma excessão é detectada. Assim, uma mensagem apropriada é mostrada
    // ao usuário
    @FXML
    public void decrypt(Event event) {
        
        // It is required to split the input into batches with a maximum
        // of 128 characters. That's because a BigInteger cannot work
        // properly with strings bigger than that.
        String textToDecrypt = message.getText();
        String tempString;
        BigInteger tempBigInteger;
        StringBuilder encryptedStringBuilder = new StringBuilder();
        int charsProcessedSoFar = 0;
        
        try {
            if (sizes.isEmpty()){
                throw new Exception();
            }
            for (Integer size : sizes){
                    tempString = textToDecrypt
                            .substring(charsProcessedSoFar,charsProcessedSoFar+size);
                    tempBigInteger = new BigInteger(tempString);
                    encryptedStringBuilder.append(rsaghm.decrypt(tempBigInteger));
                    charsProcessedSoFar = charsProcessedSoFar + size;
                }
            result.setText(encryptedStringBuilder.toString());
            isEncryptedContent = false;
        } catch(Exception e){
            Alert alert = new Alert(AlertType.ERROR);
            StringBuilder contentText = new StringBuilder();
            
            contentText.append("Ooops, parece que o conteúdo não é um formato conhecido!\n");
            contentText.append("O formato esperado é uma cadeia de números.\n");
            contentText.append("Não deve conter nenhum outro tipo de caracter");
            alert.setTitle("Atenção");
            alert.setHeaderText(null);
            alert.setContentText(contentText.toString());

            alert.showAndWait();
        }
    }

    // Método auxiliar utilizado para abrir um arquivo e ler seu conteúdo,
    // caractere por caractere, preservando inclusive quebras de linha
    private void openFile(File file, TextArea path, TextArea contentHolder) {
        try {
            if (file.canRead()) {
                String extension = file.getName();
                extension = extension.substring(extension.length()-4,extension.length());
                if (extension.equals(".enc")){
                    // If the file has a encrypted content, it is required
                    // to parse accordingly. It has a big integer followed
                    // by its string size
                    //
                    // BIGINTEGERSTRING|BIGINTEGERSTRINGSIZE|
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    StringBuilder content = new StringBuilder();
                    StringBuilder contentSize;
                    int c;

                    // Lê o arquivo, caractere por caractere
                    while ((c = reader.read()) != -1) {
                        char ch = (char) c;
                        if (ch == '|') {
                            contentSize = new StringBuilder();
                            c = reader.read();
                            ch = (char) c;
                            while(ch != '|'){
                                contentSize.append(ch);
                                c = reader.read();
                                ch = (char) c;
                            }
                            sizes.add(Integer.parseInt(contentSize.toString()));
                            c = reader.read(); // Moves file pointer
                            ch = (char) c;
                        }
                        
                        if (c != -1){
                            content.append(ch);
                        }
                    }
                    path.setText(file.getAbsolutePath());
                    contentHolder.setText(content.toString());
                } else {
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
        PrintWriter writer = null;
        File file;
        StringBuilder contentText = new StringBuilder();
        Alert alert = new Alert(AlertType.INFORMATION);
        try { 
            if(isEncryptedContent){
                writer = new PrintWriter("result.enc", "UTF-8");
                int offset = 0;
                String text = result.getText();
                
                for (Integer i : sizes){
                    writer.print(text.substring(offset, offset + i));
                    writer.print("|" + i + "|");
                    offset = offset + i;
                }
                sizes.clear();

                contentText.append("O conteúdo foi salvo para o arquivo 'result.enc'!\n");
                file = new File("result.enc");
            } else {
                writer = new PrintWriter("result.dec", "UTF-8");
                writer.print(result.getText());
                
                contentText.append("O conteúdo foi salvo para o arquivo 'result.dec'!\n");
                file = new File("result.dec");
            }
            writer.close();
            
            contentText.append("\nLocal: ").append(file.getAbsolutePath());
            alert.setTitle("Atenção");
            alert.setHeaderText(null);
            alert.setContentText(contentText.toString());
            alert.showAndWait();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(EncryptDecryptController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}

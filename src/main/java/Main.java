

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.apache.log4j.BasicConfigurator;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private Desktop desktop = Desktop.getDesktop();
    File printFile = new File("");

    @Override
    public void start(final Stage primaryStage) throws Exception{
        primaryStage.setTitle("Printing");
        final FileChooser fileChooser = new FileChooser();
        final String[] fileName = new String[2];
        final Button openButton = new Button("Open a PDF to print...");
        final Button printButton = new Button("Print File");
        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(primaryStage);
                            fileName[0] = file.getAbsolutePath();
                            fileName[1] = file.getName();
                        if (file != null) {
                            openFile(file);
                            openButton.setText(file.getAbsolutePath());
                        }
                    }
                });
        printButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent event) {
                        try {
                            whenUploadFileUsingSshj_thenSuccess(fileName[0], fileName[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );
        final GridPane inputGridPane = new GridPane();
        GridPane.setConstraints(openButton, 0, 0);
        GridPane.setConstraints(printButton, 2,3);
        inputGridPane.setHgap(6);
        inputGridPane.setVgap(6);
        inputGridPane.getChildren().addAll(openButton, printButton);

        final Pane rootGroup = new VBox(100);
        rootGroup.getChildren().addAll(inputGridPane);
        rootGroup.setPadding(new Insets(12,12,12,12));
        primaryStage.setScene(new Scene(rootGroup));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    public void openFile(File file){
        printFile = file;
    }


    public static void main(String[] args) {
        BasicConfigurator.configure();
        launch(args);
    }

    private SSHClient setupSshj() throws IOException {
        SSHClient client = new SSHClient();
        client.addHostKeyVerifier(new PromiscuousVerifier());
        client.connect("192.168.1.81");
        client.authPassword("sethsan","rqds12");
        return client;
    }


    public void whenUploadFileUsingSshj_thenSuccess(String localFile, String name)
            throws IOException {
        String remoteDir = "/home/sethsan/print/";
        SSHClient sshClient = setupSshj();
        SFTPClient sftpClient = sshClient.newSFTPClient();

        sftpClient.put(localFile, remoteDir + name);

        sftpClient.close();
        sshClient.disconnect();
    }


}

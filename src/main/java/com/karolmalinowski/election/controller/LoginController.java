package com.karolmalinowski.election.controller;

import com.karolmalinowski.election.model.Voter;
import com.karolmalinowski.election.service.interfaces.VoterService;
import com.karolmalinowski.election.service.tools.PeselTools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
public class LoginController{
    @Value("${voters.disallowed.json}")
    private String disallowedUrl;
    private final String votingWindowPath = "fxml/votingPage.fxml";
    @FXML
    private TextField nameInput;
    @FXML
    private TextField surnameInput;
    @FXML
    private TextField peselInput;
    @FXML
    private Button voteButton;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    VoterService voterService;

    public void initialize(){
        PeselTools.setDisallowedUrl(disallowedUrl);
        voteButton.setOnAction(event -> login());
    }

    private void login() {
        try {
            Voter voter = voterService.createVoterInstance(nameInput.getText(), surnameInput.getText(), peselInput.getText());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(votingWindowPath));
            fxmlLoader.setControllerFactory(appContext::getBean);
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(
                    new Scene(
                            fxmlLoader.load()
                    )
            );
            VotingController controller =
                    fxmlLoader.getController();
            controller.initData(voter);
            stage.show();
            ((Stage)voteButton.getScene().getWindow()).close();
        }catch (IllegalArgumentException e){
            new Alert(Alert.AlertType.INFORMATION, e.getMessage()).showAndWait();
        }catch(IOException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Cannot load voting page" + e.toString()).showAndWait();
        }
    }

}

package com.karolmalinowski.election.controller;

import com.itextpdf.text.FontFactory;
import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.Voter;
import com.karolmalinowski.election.service.interfaces.CandidateService;
import com.karolmalinowski.election.service.interfaces.VoterService;
import com.karolmalinowski.election.service.tools.PeselTools;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class VotingController {

    private Voter voter;
    @FXML
    GridPane gridPane;
    @FXML
    private Button voteButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button statisticsButton;

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private VoterService voterService;

    @Autowired
    private ApplicationContext context;

    private final String loginPageUrl = "fxml/loginPage.fxml";
    private final String statisticsPageUrl = "fxml/statisticsPage.fxml";

    private String confirmationMessageIncorrectVote = " Your vote is invalid, and you won't be able to vote again.\n"
            + " Are you sure, you want to proceed?";
    private String forVoterInformation = " To vote you should check a checkbox by the candidate, you are voting for. " +
            "Voting when more than one checkbox is checked, or none checkbox is checked will result in an invalid vote." +
            "You can vote only once, even when your vote is invalid." +
            "You are voting as ";


    /**
     * Passes Voter object to bind Vote to Voter and Candidate.
     *
     * @param voter
     */
    public void initData(Voter voter) {
        this.voter = voter;
        Text text = new Text(forVoterInformation + voter.getName() + " " + voter.getSurname() + ".");
        text.setWrappingWidth(900);
        gridPane.add(text, 0, 0);
    }

    public void initialize() {
        List<Candidate> allCandidates = candidateService.findAllCandidates();
        createCandidatesTableOnGrid(allCandidates);
        voteButton.setOnAction(event -> vote());
        logoutButton.setOnAction(event -> logout());
        statisticsButton.setOnAction(event -> showStatistics());
    }

    /**
     * Gets List of candidates to put on the grid.
     * Candidate id (as in database) is stored in checkbox object.
     *
     * @param allCandidates
     */
    private void createCandidatesTableOnGrid(List<Candidate> allCandidates) {
        int i = 1;
        String FONT = "fonts/Amble-Regular.ttf";
        for (Candidate candidate :
                allCandidates) {
            Text text = new Text(" Name: " + candidate.getName() + "\n Party: " + candidate.getParty());
            text.setFont(new Font("Arial",15));
            gridPane.add(text, 0, i);
            CheckBox checkBox = new CheckBox();
            checkBox.setPadding(new Insets(0, 0, 10, 10));
            checkBox.setId(candidate.getId().toString());
            gridPane.add(checkBox, 1, i);
            i++;
        }
    }

    /**
     * After bunch of checks voter is saved into the system, what means, he voted.
     * Optionally, when the vote is valid, a vote entity is also saved into the db.
     */
    private void vote() {
        Alert internalErrorMessage = new Alert(Alert.AlertType.ERROR, "Internal error. Contact staff immediately.");
        List<Candidate> votedForCandidates = new ArrayList<>();

        if (voterService.findByPesel(voter.getPesel()).isPresent()) {
            new Alert(Alert.AlertType.INFORMATION, "You have voted already!").showAndWait();
            return;
        }

        //Read all checkboxes and getting their ids (the same as candidates ids)
        for (Node child : gridPane.getChildren()) {
            if (child instanceof CheckBox) {
                if (((CheckBox) child).isSelected()) {
                    String idString = child.getId();
                    if (idString.matches("\\d*")) {
                        Optional<Candidate> byId = candidateService.findById(Long.valueOf(idString));
                        if (byId.isPresent()) {
                            votedForCandidates.add(byId.get());
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        Optional<ButtonType> buttonType;
        Candidate candidate;

        //Confirmation
        if (votedForCandidates.size() == 1) {
            //if voted for only one candidate
            candidate = votedForCandidates.get(0);
            buttonType = new Alert(Alert.AlertType.CONFIRMATION,
                    "You are voting for " + candidate.getName() + " from " + candidate.getParty() + " party." +
                            "Is that correct?").showAndWait();
        } else {
            buttonType = new Alert(Alert.AlertType.CONFIRMATION, confirmationMessageIncorrectVote).showAndWait();
            candidate = null;
        }

        if (buttonType.isPresent() && buttonType.get() == ButtonType.OK) {

            //Check for disallowed list
            if (PeselTools.disallowed(voter.getPesel())) {
                voterService.addDisallowedTry();
                new Alert(Alert.AlertType.INFORMATION, "You have been deprived voting rights.").showAndWait();
                return;
            } else {
                voter.setCandidate(candidate);
                if (voterService.voteFor(voter, candidate)) {
                    //Saving voter to database means he voted. Only after saving in db the message is viewed.
                    new Alert(Alert.AlertType.INFORMATION, "Success. Thank you for your vote").showAndWait();
                }
            }
        }
    }

    private void showStatistics() {
        try {
            loadNewWindow(statisticsPageUrl, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        try {
            loadNewWindow(loginPageUrl, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ((Stage)logoutButton.getScene().getWindow()).close();
    }

    private void loadNewWindow(String url, boolean setModality) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource(url));
            fxmlLoader.setControllerFactory(context::getBean);
            Stage stage = new Stage(StageStyle.DECORATED);
            if(setModality) {
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner(logoutButton.getScene().getWindow().getScene().getWindow());
            }
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
    }


}

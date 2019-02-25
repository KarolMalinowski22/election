package com.karolmalinowski.election.controller;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.Voter;
import com.karolmalinowski.election.service.interfaces.VotingService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class VotingController {

    @FXML
    GridPane gridPane;

    @Autowired
    private VotingService votingService;

    public void initialize(){
        List<Candidate> allCandidates = votingService.findAllCandidates();
        int i = 1;
        for (Candidate candidate :
                allCandidates) {
            gridPane.add(new Text("Name: " + candidate.getName() + "\nParty: " + candidate.getParty()), 1, i);
            CheckBox checkBox = new CheckBox();
            checkBox.setId(candidate.getId().toString());
            gridPane.add(checkBox, 2, i);
            i++;
        }
    }
    void initData(Voter voter){

    }
}

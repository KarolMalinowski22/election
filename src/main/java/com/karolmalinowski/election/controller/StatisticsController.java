package com.karolmalinowski.election.controller;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.dto.ElectionStatistics;
import com.karolmalinowski.election.service.interfaces.CandidateService;
import com.karolmalinowski.election.service.interfaces.VoterService;
import com.karolmalinowski.election.service.tools.Export;
import com.karolmalinowski.election.service.tools.Statistics;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StatisticsController {

    @Autowired
    CandidateService candidateService;
    @Autowired
    VoterService voterService;

    private ElectionStatistics electionStatistics;

    @FXML
    AnchorPane candidatesGraph;
    @FXML
    BarChart candidatesBarGraph;
    @FXML
    AnchorPane partiesGraph;
    @FXML
    BarChart partiesBarGraph;
    @FXML
    Button barGraphButton;
    @FXML
    Button numericGraphButton;
    @FXML
    Button exportCsvButton;
    @FXML
    Button exitButton;
    @FXML
    SplitPane splitPane;
    @FXML
    SplitPane splitPane2;
    @FXML
    GridPane candidatesGrid;
    @FXML
    GridPane partiesGrid;


    public void initialize(){
        electionStatistics = Statistics.generate(voterService.findAllVoters(), candidateService.findAllCandidates(), voterService.countDisallowed());
        createGraphs();
        barGraphButton.setOnAction(event -> showBarGraph());
        numericGraphButton.setOnAction(event -> showNumericGraph());
        exportCsvButton.setOnAction(event -> exportCsv((Stage)exportCsvButton.getScene().getWindow()));
        exitButton.setOnAction(event -> exit((Stage)exportCsvButton.getScene().getWindow()));
    }



    private void showBarGraph() {
        splitPane.setDividerPosition(0, 0);
        splitPane2.setDividerPosition(0, 0);
    }

    private void showNumericGraph() {
        splitPane.setDividerPosition(0, 1);
        splitPane2.setDividerPosition(0, 1);

    }

    private void createGraphs() {
        createNumericCandidateGraph();
        createNumericPartiesGraph();
        createBarCandidatesGraph();
        createBarPartiesGraph();
    }

    /**
     * This extracts some data for Map partyVotes field, and Integer invalidVotes field
     */
    private void createNumericCandidateGraph() {
        List<Candidate> allCandidates = electionStatistics.getCandidates();
        Map<String, Integer> partyVotes = electionStatistics.getPartyVotes();

        int i = 1;
        int sum = 0;
        for (Candidate candidate :
                allCandidates) {
            Text text = new Text(" Name: " + candidate.getName() + "\n Party: " + candidate.getParty());
            text.setFont(new Font(15));
            candidatesGrid.add(text, 0, i);
            int votes = candidate.getVoters().size();
            Text text2 = new Text(" " + votes + " votes");
            text2.setFont(new Font(15));
            candidatesGrid.add(text2, 1, i);
            i++;

            String party = candidate.getParty();
            partyVotes.merge(party, votes, (e1, e2) -> e1 + e2);
        }

        Text text = new Text(" Invalid");
        text.setFont(new Font(15));
        candidatesGrid.add(text, 0, i);
        Text text2 = new Text(" " + electionStatistics.getInvalidVotes() + " votes");
        text2.setFont(new Font(15));
        candidatesGrid.add(text2, 1, i);
    }

    private void createNumericPartiesGraph(){
        Map<String, Integer> partyVotes = electionStatistics.getPartyVotes();
        int i = 1;
        for(String party : partyVotes.keySet()){
            Text text = new Text("Party: " + party);
            text.setFont(new Font(15));
            partiesGrid.add(text, 0, i);
            Text text2 = new Text(" " + partyVotes.get(party) + " votes");
            text2.setFont(new Font(15));
            partiesGrid.add(text2, 1, i);
            i++;
        }

        Text text = new Text(" Invalid");
        text.setFont(new Font(15));
        partiesGrid.add(text, 0, i);
        Text text2 = new Text(" " + electionStatistics.getInvalidVotes() + " votes");
        text2.setFont(new Font(15));
        partiesGrid.add(text2, 1, i);
    }

    private void createBarCandidatesGraph(){
        CategoryAxis xAxis  = new CategoryAxis();
        xAxis.setLabel("Candidates");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Votes");

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("votes");
        List<Candidate> allCandidates = electionStatistics.getCandidates();
        for (Candidate  candidate : allCandidates) {
            dataSeries1.getData().add(new XYChart.Data(candidate.getName(), candidate.getVoters().size()));
        }

        candidatesBarGraph.getData().add(dataSeries1);
    }

    private void createBarPartiesGraph(){
        Map<String, Integer> partyVotes = electionStatistics.getPartyVotes();
        CategoryAxis xAxis  = new CategoryAxis();
        xAxis.setLabel("Parties");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Votes");

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("votes");
        for (String  party : partyVotes.keySet()) {
            dataSeries1.getData().add(new XYChart.Data(party, partyVotes.get(party)));
        }

        partiesBarGraph.getData().add(dataSeries1);
    }

    private void exportCsv(Stage stage){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf")
        );
        File selectedFile = fileChooser.showSaveDialog(stage);

        if(selectedFile == null){
            //No file selected
        }else{
            Export.statictics(selectedFile, electionStatistics);
        }
    }
    private void exit(Stage window) {
        window.close();
    }
}

package com.karolmalinowski.election.service.tools;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.dto.ElectionStatistics;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Export {
    public static void statictics(File file, ElectionStatistics electionStatistics){
        if(file.getPath().matches(".*.pdf")){
            toPdf(file, electionStatistics);
        }else if(file.getPath().matches(".*.csv")){
            toCsv(file, electionStatistics);
        }
    }

    private static void toCsv(File file, ElectionStatistics electionStatistics) {
        try {
            FileWriter fileWriter = new FileWriter(file);
            CSVWriter writer = new CSVWriter(fileWriter);

            String[] line = {"valid votes",""+electionStatistics.getValidVotes()};
            writer.writeNext(line);

            line[0] = "invalid votes";line[1] = ""+electionStatistics.getInvalidVotes();
            writer.writeNext(line);

            line = new String[]{"candidate", "", ""};
            for (Candidate candidate :
                    electionStatistics.getCandidates()) {
                line[1] = candidate.getName();line[2] = candidate.getVoters().size() + "";
                writer.writeNext(line);
            }

            line[0] = "party";
            Map<String, Integer> partyVotes = electionStatistics.getPartyVotes();
            for (String party :
                    partyVotes.keySet()) {
                line[1] = party;line[2] = partyVotes.get(party) + "";
                writer.writeNext(line);
            }

            line = new String[]{"tried to vote with no voting rights", electionStatistics.getTriesByDisallowed()+""};
            writer.writeNext(line);
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void toPdf(File file, ElectionStatistics electionStatistics) {

    }
}

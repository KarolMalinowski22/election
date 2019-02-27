package com.karolmalinowski.election.service.tools;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.dto.ElectionStatistics;
import com.opencsv.CSVWriter;

import java.io.*;
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

            line = new String[]{"tried to vote with no voting rights", electionStatistics.getTriesByDisallowed()+""};
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
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void toPdf(File file, ElectionStatistics electionStatistics) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();
            addMetaData(document);
            addContent(document, electionStatistics);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void addMetaData(Document document) {
        document.addTitle("Election data");
        //document.addSubject("");
        //document.addKeywords("");
        //document.addAuthor("");
        //document.addCreator("");
    }
    private static void addContent(Document document, ElectionStatistics electionStatistics) throws DocumentException, IOException {
        String FONT = "fonts/Amble-Regular.ttf";

        Font fontBig = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, true,24);
        Font fontMedium = FontFactory.getFont(FONT, "Cp1250", true,18);
        Font fontSmall = FontFactory.getFont(FONT, "Cp1250", true,12);
        Anchor anchor = new Anchor("Voting report", fontBig);
        anchor.setName("Voting report");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("General data", fontMedium);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Valid votes: " + electionStatistics.getValidVotes(), fontSmall));
        subCatPart.add(new Paragraph("Invalid votes: " + electionStatistics.getInvalidVotes(), fontSmall));
        subCatPart.add(new Paragraph("Tries by people with no voting rights: " + electionStatistics.getTriesByDisallowed(), fontSmall));

        subPara = new Paragraph("Votes by candidate", fontMedium);
        subCatPart = catPart.addSection(subPara);
        for (Candidate candidate :
                electionStatistics.getCandidates()) {
            subCatPart.add(new Paragraph(candidate.getName() + " received " + candidate.getVoters().size() + " votes.", fontSmall));
        }

        subPara = new Paragraph("Votes by party", fontMedium);
        subCatPart = catPart.addSection(subPara);
        Map<String, Integer> partyVotes = electionStatistics.getPartyVotes();
        for (String party :
                partyVotes.keySet()) {
            subCatPart.add(new Paragraph(party + " received " + partyVotes.get(party) + " votes.", fontSmall));
        }

        // now add all this to the document
        document.add(catPart);
    }
}

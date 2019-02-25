package com.karolmalinowski.election.service;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.xml.Candidates;
import com.karolmalinowski.election.repository.CandidateRepository;
import com.karolmalinowski.election.service.interfaces.VotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Service("votingService")
public class DefaultVotingService implements VotingService {
    @Autowired
    CandidateRepository candidateRepository;
    @Override
    public List<Candidate> findAllCandidates() {
        List<Candidate> all = candidateRepository.findAll();
        if(all.size() == 0){
            loadCandidatesToDB();
            all = candidateRepository.findAll();
        }
        return all;
    }
    private void loadCandidatesToDB(){
        try {
            JAXBContext jc = JAXBContext.newInstance("com.karolmalinowski.election.model.xml.Candidates");
            Unmarshaller u = jc.createUnmarshaller();
            URL url = new URL("http://webtask.future-processing.com:8069/candidates");
            Candidates candidates = (Candidates)u.unmarshal(url);
            for (Candidate candidate:candidates.getCandidates()) {
                candidateRepository.save(candidate);
            }
        } catch(JAXBException e){

        }catch(IOException e){

        }
    }
}

package com.karolmalinowski.election.service;

import com.google.gson.Gson;
import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.json.CandidatesBoxJson;
import com.karolmalinowski.election.repository.CandidateRepository;
import com.karolmalinowski.election.service.interfaces.CandidateService;
import com.karolmalinowski.election.service.tools.UrlTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("votingService")
public class DefaultCandidateService implements CandidateService {
    @Value("${candidates.list.json}")
    private String candidatesUrl;
    @Autowired
    CandidateRepository candidateRepository;

    @Override
    public List<Candidate> findAllCandidates() {
        List<Candidate> all = candidateRepository.findAll();
        if (all.size() == 0) {
            loadCandidatesToDB();
            all = candidateRepository.findAll();
        }
        return all;
    }

    @Override
    public Optional<Candidate> findById(Long id) {
        return candidateRepository.findById(id);
    }

    private void loadCandidatesToDB() {
        try {
            String readUrl = UrlTools.readUrl(candidatesUrl);
            Gson gson = new Gson();
            CandidatesBoxJson candidates = gson.fromJson(readUrl, CandidatesBoxJson.class);

            for (Candidate candidate : candidates.getCandidates().getCandidate()) {
                candidateRepository.save(candidate);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

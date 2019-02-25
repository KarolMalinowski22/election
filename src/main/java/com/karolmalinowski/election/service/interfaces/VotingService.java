package com.karolmalinowski.election.service.interfaces;

import com.karolmalinowski.election.model.Candidate;

import java.util.List;
public interface VotingService {
    List<Candidate> findAllCandidates();

}

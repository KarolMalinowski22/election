package com.karolmalinowski.election.service.interfaces;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.Voter;

public interface VoterService {
    /**
     * Validate Voter object passed.
     * Validate by name not empty, surname not empty, pesel of exact long, and did vote already
     */
    void valid(String name, String surname, String pesel) throws IllegalArgumentException;

    Voter createVoterInstance(String name, String surname, String pesel);

    /**
     * This saves vote event in database.
     * All validations should be done before (if voter voted already, or is adult ect.)
     * @param voter
     * @param candidate
     * @return
     */
    boolean voteFor(Voter voter, Candidate candidate);
}

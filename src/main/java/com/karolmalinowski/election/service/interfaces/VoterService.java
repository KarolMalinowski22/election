package com.karolmalinowski.election.service.interfaces;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.Voter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    Optional<Voter> findByPesel(String pesel);

    List<Voter> findAllVoters();

    void addDisallowedTry();
    Long countDisallowed();
}

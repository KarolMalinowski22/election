package com.karolmalinowski.election.service.interfaces;

import com.karolmalinowski.election.model.Voter;

public interface VoterService {
    /**
     * Validate Voter object passed.
     * Validate by name not empty, surname not empty, pesel of exact long, and did vote already
     */
    void valid(String name, String surname, String pesel) throws IllegalArgumentException;

    Voter createVoterInstance(String name, String surname, String pesel);
}

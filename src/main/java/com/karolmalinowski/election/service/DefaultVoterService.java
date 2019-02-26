package com.karolmalinowski.election.service;

import com.google.gson.Gson;
import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.Voter;
import com.karolmalinowski.election.model.json.DisallowedBoxJson;
import com.karolmalinowski.election.model.json.DisallowedPerson;
import com.karolmalinowski.election.repository.VoterRepository;
import com.karolmalinowski.election.service.interfaces.VoterService;
import com.karolmalinowski.election.service.tools.PeselTools;
import com.karolmalinowski.election.service.tools.UrlTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultVoterService implements VoterService {
    private final int peselLength = 11;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    VoterRepository voterRepository;


    @Override
    public Voter createVoterInstance(String name, String surname, String pesel) throws IllegalArgumentException {
        valid(name, surname, pesel);
        Voter voter = new Voter();
        voter.setName(name);
        voter.setSurname(surname);
        voter.setPesel(passwordEncoder.encode(pesel));
        return voter;
    }

    @Override
    public boolean voteFor(Voter voter, Candidate candidate) {
        List<Candidate> candidates = voter.getCandidate();
        if(candidates == null){
            candidates = new ArrayList<>();
        }
        candidates.add(candidate);
        voterRepository.save(voter);
        return true;
    }

    @Override
    public void valid(String name, String surname, String pesel) throws IllegalArgumentException {
        String errorMessage = "";
        if ("".equals(name)) {
            errorMessage += "-Name is empty.\n";
        }
        if ("".equals(surname)) {
            errorMessage += "-Surname is empty.\n";
        }
        try {
            PeselTools.valid(pesel);
        } catch (IllegalArgumentException e) {
            errorMessage += e.getMessage() + "\n";
        }
        if (voterRepository.findAllByPesel(passwordEncoder.encode(pesel)).isPresent()) {
            errorMessage += "-Voter of " + pesel + " have voted already.\n";
        }
        if (PeselTools.disallowed(pesel)) {
            errorMessage += "-You have no voting rights.\n";
        }
        if ("".equals(errorMessage)) {
            return;
        } else {
            if (errorMessage.matches(".*\n")) {
                errorMessage.substring(0, errorMessage.length() - 1);
            }
            throw new IllegalArgumentException(errorMessage);
        }
    }



}

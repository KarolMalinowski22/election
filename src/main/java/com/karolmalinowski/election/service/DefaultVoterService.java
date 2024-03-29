package com.karolmalinowski.election.service;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.DisallowedVotingTry;
import com.karolmalinowski.election.model.Voter;
import com.karolmalinowski.election.repository.DisallowedVotingTriesRepository;
import com.karolmalinowski.election.repository.VoterRepository;
import com.karolmalinowski.election.service.interfaces.VoterService;
import com.karolmalinowski.election.service.tools.PeselTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DefaultVoterService implements VoterService {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    VoterRepository voterRepository;
    @Autowired
    DisallowedVotingTriesRepository disallowedVotingTriesRepository;

    @Override
    public Voter createVoterInstance(String name, String surname, String pesel) throws IllegalArgumentException {
        valid(name, surname, pesel);
        Voter voter = new Voter();
        voter.setName(name);
        voter.setSurname(surname);
        voter.setPesel(String.valueOf(pesel.hashCode()));
        return voter;
    }

    @Override
    public boolean voteFor(Voter voter, Candidate candidate) {
        voterRepository.save(voter);
        return true;
    }

    @Override
    public Optional<Voter> findByPesel(String pesel) {
        return voterRepository.findAllByPesel(pesel);
    }

    @Override
    public List<Voter> findAllVoters() {
        return voterRepository.findAll();
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

    @Override
    public void addDisallowedTry() {
        disallowedVotingTriesRepository.save(new DisallowedVotingTry());
    }

    @Override
    public Long countDisallowed() {
        return disallowedVotingTriesRepository.count();
    }
}

package com.karolmalinowski.election.service.interfaces;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.Voter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateService {
    List<Candidate> findAllCandidates();
    Optional<Candidate> findById(Long id);
}

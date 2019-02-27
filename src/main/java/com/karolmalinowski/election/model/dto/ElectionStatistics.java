package com.karolmalinowski.election.model.dto;

import com.karolmalinowski.election.model.Candidate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ElectionStatistics {
    private Integer validVotes;
    private Integer invalidVotes;
    private List<Candidate> candidates;
    private Map<String, Integer> partyVotes;
    private Long triesByDisallowed;
}

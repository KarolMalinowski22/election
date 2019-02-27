package com.karolmalinowski.election.service.tools;

import com.karolmalinowski.election.model.Candidate;
import com.karolmalinowski.election.model.Voter;
import com.karolmalinowski.election.model.dto.ElectionStatistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    public static ElectionStatistics generate(List<Voter> voters, List<Candidate> candidates, Long disallowedTries){
        ElectionStatistics electionStatistics = new ElectionStatistics();
        Map<String, Integer> partyVotes = new HashMap<>();
        Integer validVotes;
        Integer invalidVotes;

        electionStatistics.setCandidates(candidates);
        electionStatistics.setTriesByDisallowed(disallowedTries);

        int sum = 0;
        for (Candidate candidate :
                candidates) {
            int votes = candidate.getVoters().size();
            sum += votes;

            String party = candidate.getParty();
            partyVotes.merge(party, votes, (e1, e2) -> e1 + e2);
        }

        validVotes = sum;
        invalidVotes = voters.size() - sum;

        electionStatistics.setValidVotes(validVotes);
        electionStatistics.setInvalidVotes(invalidVotes);
        electionStatistics.setPartyVotes(partyVotes);

        return electionStatistics;
    }
}

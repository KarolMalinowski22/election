package com.karolmalinowski.election.model.json;

import com.karolmalinowski.election.model.Candidate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CandidatesJson {
    private String publicationDate;
    private Candidate[] candidate;
}

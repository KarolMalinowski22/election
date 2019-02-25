package com.karolmalinowski.election.model.xml;

import com.karolmalinowski.election.model.Candidate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Candidates {
    private String publicationDate;
    private List<Candidate> candidates;
}

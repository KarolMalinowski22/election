package com.karolmalinowski.election.service;

import com.karolmalinowski.election.model.Voter;
import com.karolmalinowski.election.repository.VoterRepository;
import com.karolmalinowski.election.service.interfaces.VoterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultVoterService implements VoterService {
    private final int peselLength = 11;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    VoterRepository voterRepository;


    @Override
    public Voter createVoterInstance(String name, String surname, String pesel) throws IllegalArgumentException{
        valid(name, surname, pesel);
        Voter voter = new Voter();
        voter.setName(name);voter.setSurname(surname);voter.setPesel(passwordEncoder.encode(pesel));
        return voter;
    }
    @Override
    public void valid(String name, String surname, String pesel) throws IllegalArgumentException{
        String errorMessage = "";
        if("".equals(name)){
            errorMessage += "-Name is empty.\n";
        }
        if("".equals(surname)){
            errorMessage += "-Surname is empty.\n";
        }
        if(pesel.length() != peselLength){
            errorMessage += "-Pesel is incorrect. Check it.\n";
        }
        if(voterRepository.findAllByPesel(passwordEncoder.encode(pesel)).isPresent()){
            errorMessage += "-Voter of " + pesel + " have voted already.\n";
        }
        if("".equals(errorMessage)){
            return;
        }else{
            if(errorMessage.matches(".*\n")){
                errorMessage.substring(0, errorMessage.length() - 1);
            }
            throw new IllegalArgumentException(errorMessage);
        }
    }

}

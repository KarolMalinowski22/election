package com.karolmalinowski.election.service;

import com.karolmalinowski.election.model.Person;
import com.karolmalinowski.election.repository.PersonRepository;
import com.karolmalinowski.election.service.interfaces.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultPersonService implements PersonService {
    private final int peselLength = 11;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PersonRepository personRepository;
    @Override
    public void valid(Person person) throws IllegalArgumentException{
        String errorMessage = "";
        if("".equals(person.getName())){
            errorMessage += "-Name is empty.\n";
        }
        if("".equals(person.getSurname())){
            errorMessage += "-Surname is empty.\n";
        }
        if(person.getPesel().length() != peselLength){
            errorMessage += "-Pesel is incorrect. Check it.\n";
        }
        if(personRepository.findAllByPesel(passwordEncoder.encode(person.getPesel())).isPresent()){
            errorMessage += "-Person of " + person.getPesel() + " have voted already.\n";
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

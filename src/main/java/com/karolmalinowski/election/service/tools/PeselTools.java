package com.karolmalinowski.election.service.tools;

import com.google.gson.Gson;
import com.karolmalinowski.election.model.json.DisallowedBoxJson;
import com.karolmalinowski.election.model.json.DisallowedPerson;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Tools to validate pesel number or by pesel number.
 */
public class PeselTools {
    private static final int peselLength = 11;
    private static final int adultAge = 18;
    private static String disallowedUrl = "http://webtask.future-processing.com:8069/blocked";


    /**
     * Validates that pesel number is digits, is correct length and if person is adult.
     * @param pesel
     * @throws IllegalArgumentException containing one message, of the most important error, for user.
     */
    public static void valid(String pesel) throws IllegalArgumentException{
        pesel = pesel.trim();
        if(pesel.length() != peselLength){
            throw new IllegalArgumentException("-Pesel number length is incorrect.");
        }
        if(!pesel.matches("\\d{11}")){
            throw new IllegalArgumentException("-Pesel should only be numbers");
        }
        if(!(LocalDate.now().minusYears(adultAge).compareTo(getBirthdate(pesel))>=0)){
            throw new IllegalArgumentException("-You must be at least 18 years old.");
        }
    }

    /**
     * Only for PESEL document number.
     * @param pesel
     * @throws IllegalArgumentException
     */
    private static LocalDate getBirthdate(String pesel) throws IllegalArgumentException{
        String peselYear = pesel.substring(0, 2);
        String peselMonth = pesel.substring(2, 4);
        String peselDay = pesel.substring(4, 6);

        String actualYear;
        String actualMonth;
        String actualDay;

        if(peselMonth.matches("[2|3]\\d")){
            actualYear = "20" + peselYear;
            actualMonth = "" + (Integer.valueOf(peselMonth) - 20);
        }else{
            actualYear = "19" + peselYear;
            actualMonth = peselMonth;
        }
        actualDay = peselDay;
        LocalDate date;
        try {
            date = LocalDate.of(Integer.valueOf(actualYear), Integer.valueOf(actualMonth), Integer.valueOf(actualDay));
        }catch(DateTimeException e){
            throw new IllegalArgumentException("-Pesel number is incorrect.");
        }
        return date;
    }

    /**
     * Checks by pesel if person has been deprived of voting rights.
     * @param pesel
     * @return
     */
    public static boolean disallowed(String pesel) {
        try {
            String readUrl = UrlTools.readUrl(disallowedUrl);
            Gson gson = new Gson();
            DisallowedBoxJson disallowed = gson.fromJson(readUrl, DisallowedBoxJson.class);
            DisallowedPerson[] disallowedPeople = disallowed.getDisallowed().getPerson();
            List<DisallowedPerson> disallowedPeopleList = Arrays.asList(disallowedPeople);
            if (disallowedPeopleList.stream().map(e -> e.getPesel()).collect(Collectors.toList()).contains(pesel)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

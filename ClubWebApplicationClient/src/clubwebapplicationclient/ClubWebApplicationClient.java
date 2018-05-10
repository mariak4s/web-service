/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clubwebapplicationclient;

import ee.ttu.idu0075._2015.ws.club.AddClubRequest;
import ee.ttu.idu0075._2015.ws.club.AddClubResponse;
import ee.ttu.idu0075._2015.ws.club.ClubType;
import ee.ttu.idu0075._2015.ws.club.GetClubRequest;
import java.math.BigInteger;
import java.util.Scanner;

/**
 *
 * @author Maria
 */
public class ClubWebApplicationClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String token = "salajane";
        
        AddClubRequest addClub = new AddClubRequest();
        addClub.setToken(token);
        addClub.setRequestCode(BigInteger.valueOf(2));
        addClub.setClubName("Fitlife");
        addClub.setClubCity("Kuressaare");
        addClub.setClubCountry("Estonia");
        addClub.setClubStatus("open");
                
        try {
            addClub(addClub);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
      
        GetClubRequest getClub = new GetClubRequest();
        getClub.setToken(token);
        getClub.setId(BigInteger.valueOf(2));
        ClubType response = getClub(getClub);

        System.out.println("Club with id=2: \n" + response.getClubName() + ", " + response.getClubCity() 
                + ", " + response.getClubCountry() + ", " + response.getClubStatus() + ".");

    }

    private static AddClubResponse addClub(ee.ttu.idu0075._2015.ws.club.AddClubRequest parameter) {
        ee.ttu.idu0075._2015.ws.club.ClubService service = new ee.ttu.idu0075._2015.ws.club.ClubService();
        ee.ttu.idu0075._2015.ws.club.ClubPortType port = service.getClubPort();
        return port.addClub(parameter);
    }

    private static ClubType getClub(ee.ttu.idu0075._2015.ws.club.GetClubRequest parameter) {
        ee.ttu.idu0075._2015.ws.club.ClubService service = new ee.ttu.idu0075._2015.ws.club.ClubService();
        ee.ttu.idu0075._2015.ws.club.ClubPortType port = service.getClubPort();
        return port.getClub(parameter);
    }
}
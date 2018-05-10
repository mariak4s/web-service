/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import com.sun.xml.internal.ws.developer.SchemaValidation;
import ee.ttu.idu0075._2015.ws.club.AddClubRequest;
import ee.ttu.idu0075._2015.ws.club.AddClubResponse;
import ee.ttu.idu0075._2015.ws.club.AddClubTrainingRequest;
import ee.ttu.idu0075._2015.ws.club.AddClubTrainingResponse;
import ee.ttu.idu0075._2015.ws.club.AddTrainingRequest;
import ee.ttu.idu0075._2015.ws.club.AddTrainingResponse;
import ee.ttu.idu0075._2015.ws.club.ClubTrainingListType;
import ee.ttu.idu0075._2015.ws.club.ClubTrainingType;
import ee.ttu.idu0075._2015.ws.club.ClubType;
import ee.ttu.idu0075._2015.ws.club.GetClubListRequest;
import ee.ttu.idu0075._2015.ws.club.GetClubListResponse;
import ee.ttu.idu0075._2015.ws.club.GetClubRequest;
import ee.ttu.idu0075._2015.ws.club.GetClubTrainingListRequest;
import ee.ttu.idu0075._2015.ws.club.GetTrainingListRequest;
import ee.ttu.idu0075._2015.ws.club.GetTrainingListResponse;
import ee.ttu.idu0075._2015.ws.club.GetTrainingRequest;
import ee.ttu.idu0075._2015.ws.club.TrainingType;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.jws.WebService;

/**
 *
 * @author Maria
 */
@WebService(serviceName = "clubService", portName = "clubPort", endpointInterface = "ee.ttu.idu0075._2015.ws.club.ClubPortType", targetNamespace = "http://www.ttu.ee/idu0075/2015/ws/club", wsdlLocation = "WEB-INF/wsdl/ClubWebService/clubService.wsdl")
@SchemaValidation
public class ClubWebService {
    
    private static final String API_TOKEN = "salajane";
    static int nextTrainingId = 1;
    static int nextClubId = 1;
    static List<TrainingType> trainingList = new ArrayList<TrainingType>();
    static List<ClubType> clubList = new ArrayList<ClubType>();
    static HashMap<BigInteger, TrainingType> addTrainingMap = new HashMap();
    static HashMap<BigInteger, ClubType> addClubMap = new HashMap();
    static HashMap<BigInteger, ClubTrainingType> addClubTrainingMap = new HashMap();

    public TrainingType getTraining(GetTrainingRequest parameter) throws InvalidTokenException {
        TrainingType training = null;

        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())){
            throw new InvalidTokenException();
        }
        
        for (int i = 0; i < trainingList.size(); i++){
            if (trainingList.get(i).getId().equals(parameter.getId())){
                training = trainingList.get(i);
            }
        }
       return training;

    }

    public AddTrainingResponse addTraining(AddTrainingRequest parameter) throws InvalidTokenException, InvalidRequestCodeException {
        AddTrainingResponse response = new AddTrainingResponse();
        TrainingType training = new TrainingType();
        
        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())){
            throw new InvalidTokenException();
        }
        
        if (parameter.getRequestCode() == null) {
            throw new InvalidRequestCodeException();
        }
        
        response.setResponseCode(parameter.getRequestCode());
        
        if (addTrainingMap.containsKey(parameter.getRequestCode())) {
            response.setTraining(addTrainingMap.get(parameter.getRequestCode()));
        } else {
            training.setId(BigInteger.valueOf(nextTrainingId++));
            training.setTrainingName(parameter.getTrainingName());
            training.setTrainerName(parameter.getTrainerName());
            training.setTrainingStyle(parameter.getTrainingStyle());
            training.setDurationInMins(parameter.getDurationInMins());
            training.setTotalPlaces(parameter.getTotalPlaces());

            trainingList.add(training);
            addTrainingMap.put(parameter.getRequestCode(), training);
            response.setTraining(training);
        }
        return response;
    }

    public GetTrainingListResponse getTrainingList(GetTrainingListRequest parameter) throws InvalidTokenException {
        GetTrainingListResponse response = new GetTrainingListResponse();
        
        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())) {
            throw new InvalidTokenException();
        }
        
        for (TrainingType trainingType : trainingList) {
            if (trainingDetailsEqual(parameter, trainingType)) {
                response.getTraining().add(trainingType);
            }
        }
        return response;
    }

    public ClubType getClub(GetClubRequest parameter) throws InvalidTokenException {
        ClubType ct = null;
        
        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())) {
            throw new InvalidTokenException();
        }

        for (int i = 0; i < clubList.size(); i++) {
            if (clubList.get(i).getId().equals(parameter.getId())) {
                ct = clubList.get(i);
            }
        }    
        return ct;
    }

    public AddClubResponse addClub(AddClubRequest parameter) throws InvalidTokenException, InvalidRequestCodeException {
        AddClubResponse response = new AddClubResponse();
        ClubType ct = new ClubType();
        
        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())){
            throw new InvalidTokenException();
        }
        
        if (parameter.getRequestCode() == null) {
            throw new InvalidRequestCodeException();
        }
        
        response.setResponseCode(parameter.getRequestCode());
        
        if (addClubMap.containsKey(parameter.getRequestCode())) {
            response.setClub(addClubMap.get(parameter.getRequestCode()));
        } else {
            ct.setId(BigInteger.valueOf(nextClubId++));
            ct.setClubName(parameter.getClubName());
            ct.setClubCity(parameter.getClubCity());
            ct.setClubCountry(parameter.getClubCountry());
            ct.setClubStatus(parameter.getClubStatus());
            ct.setClubTrainingList(new ClubTrainingListType());
            
            clubList.add(ct);
            addClubMap.put(parameter.getRequestCode(), ct);
            response.setClub(ct);
        }
        return response;       
    }

    public GetClubListResponse getClubList(GetClubListRequest parameter) throws InvalidTokenException{
        
        GetClubListResponse response = new GetClubListResponse();
        
        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())){
            throw new InvalidTokenException();
        }

        for (ClubType clubType : clubList) {
            if (clubDetailsEqual(parameter, clubType)){
                response.getClub().add(clubType);
            }
        }       
        return response;
    }

    public ClubTrainingListType getClubTrainingList(GetClubTrainingListRequest parameter) throws InvalidTokenException {
        ClubTrainingListType clubTrainingList = null;
        
        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())){
            throw new InvalidTokenException();
        }
 
        for (int i = 0; i < clubList.size(); i++) {
            if (clubList.get(i).getId().equals(parameter.getClubId())) {
                clubTrainingList = clubList.get(i).getClubTrainingList();
            }
        }    
        return clubTrainingList;
    }

    public AddClubTrainingResponse addClubTraining(AddClubTrainingRequest parameter) throws InvalidTokenException, InvalidRequestCodeException{
        AddClubTrainingResponse response = new AddClubTrainingResponse();
        ClubTrainingType clubTraining = new ClubTrainingType();

        if (!API_TOKEN.equalsIgnoreCase(parameter.getToken())){
            throw new InvalidTokenException();
        }
        
        if (parameter.getRequestCode() == null) {
            throw new InvalidRequestCodeException();
        }
        
        response.setResponseCode(parameter.getRequestCode());
        
        if (addClubTrainingMap.containsKey(parameter.getRequestCode())){
            response.setClubTraining(addClubTrainingMap.get(parameter.getRequestCode()));
        } else {
            GetTrainingRequest trainingRequest = new GetTrainingRequest();
            trainingRequest.setId(parameter.getTrainingId());
            trainingRequest.setToken(parameter.getToken());
            
            clubTraining.setTraining(getTraining(trainingRequest));
            clubTraining.setStartDate(parameter.getStartDate());
            clubTraining.setEndDate(parameter.getEndDate());
            clubTraining.setStatus(parameter.getStatus().toLowerCase());
        
            for (ClubType clubType : clubList) {
                if (clubType.getId().equals(parameter.getClubId()) 
                        && !clubType.getClubTrainingList().getClubTraining().contains(clubTraining)) {
                    clubType.getClubTrainingList().getClubTraining().add(clubTraining);
                    addClubTrainingMap.put(parameter.getRequestCode(), clubTraining);
                    response.setClubTraining(clubTraining);
                }
            }
        }
        return response;
    }
    
    private static boolean trainingDetailsEqual(GetTrainingListRequest parameter, TrainingType tt) {
        return trainingNameMatch(parameter, tt) && trainerNameMatch(parameter, tt)
                && trainingStyleMatch(parameter, tt) && durationInMinsMatch(parameter, tt);
    }
    
    private static boolean trainingNameMatch(GetTrainingListRequest parameter, TrainingType tt) {
        return parameter.getTrainingName() == null || (parameter.getTrainingName() != null && parameter.getTrainingName().equalsIgnoreCase(tt.getTrainingName()));
    }
    
    private static boolean trainerNameMatch(GetTrainingListRequest parameter, TrainingType tt) {
        return parameter.getTrainerName() == null || (parameter.getTrainerName() != null && parameter.getTrainerName().equalsIgnoreCase(tt.getTrainerName()));
    }
    private static boolean trainingStyleMatch(GetTrainingListRequest parameter, TrainingType tt) {
        return parameter.getTrainingStyle() == null || (parameter.getTrainingStyle() != null && parameter.getTrainingStyle().equalsIgnoreCase(tt.getTrainingStyle()));
    }
    
    private static boolean durationInMinsMatch(GetTrainingListRequest parameter, TrainingType tt) {
        return parameter.getDurationInMins() == null || (parameter.getDurationInMins() != null && parameter.getDurationInMins().equals(tt.getDurationInMins()));    
    }

    private static boolean clubDetailsEqual(GetClubListRequest parameter, ClubType ct){
        return clubCityMatch(parameter, ct) && clubCountryMatch(parameter, ct)
            && clubStatusMatch(parameter, ct) && hasRelatedTrainingsMatch(parameter, ct);
    }
    
    private static boolean clubCityMatch(GetClubListRequest parameter, ClubType ct){
        return parameter.getClubCity() == null || (parameter.getClubCity().equalsIgnoreCase(ct.getClubCity()));
    }

    private static boolean clubCountryMatch(GetClubListRequest parameter, ClubType ct){
        return parameter.getClubCountry() == null || (parameter.getClubCountry().equalsIgnoreCase(ct.getClubCountry()));
    }
    
    private static boolean clubStatusMatch(GetClubListRequest parameter, ClubType ct){
        return parameter.getClubStatus() == null || (parameter.getClubStatus().equalsIgnoreCase(ct.getClubStatus()));
    }

    private static boolean hasRelatedTrainingsMatch(GetClubListRequest parameter, ClubType ct){
        return parameter.getHasRelatedTrainings() == null 
                || (parameter.getHasRelatedTrainings().equalsIgnoreCase("yes")
                && !ct.getClubTrainingList().getClubTraining().isEmpty())
                || (parameter.getHasRelatedTrainings().equalsIgnoreCase("no")
                && ct.getClubTrainingList().getClubTraining().isEmpty());
    }
}
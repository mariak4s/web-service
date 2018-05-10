/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import ee.ttu.idu0075._2015.ws.club.AddTrainingRequest;
import ee.ttu.idu0075._2015.ws.club.AddTrainingResponse;
import ee.ttu.idu0075._2015.ws.club.GetTrainingListRequest;
import ee.ttu.idu0075._2015.ws.club.GetTrainingListResponse;
import ee.ttu.idu0075._2015.ws.club.GetTrainingRequest;
import ee.ttu.idu0075._2015.ws.club.TrainingType;
import java.math.BigInteger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * REST Web Service
 *
 * @author Maria
 */
@Path("trainings")
public class TrainingsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of TrainingsResource
     */
    public TrainingsResource() {
    }

    @GET
    @Produces("application/json")
    public GetTrainingListResponse getTrainingList(@QueryParam("token") String token) throws InvalidTokenException{
        ClubWebService ws = new ClubWebService();
        GetTrainingListRequest request = new GetTrainingListRequest();
        request.setToken(token);
        return ws.getTrainingList(request);
    }

    @GET
    @Path("{id : \\d+}")
    @Produces("application/json")
    public TrainingType getTraining(@PathParam("id") String id,
            @QueryParam("token") String token) throws InvalidTokenException{
        ClubWebService ws = new ClubWebService();
        GetTrainingRequest request = new GetTrainingRequest();
        request.setId(BigInteger.valueOf(Integer.parseInt(id)));
        request.setToken(token);
        return ws.getTraining(request);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public AddTrainingResponse addTraining(AddTrainingRequest content,
            @QueryParam("requestCode") int requestCode,
            @QueryParam("token") String token) throws InvalidTokenException, InvalidRequestCodeException {
        ClubWebService ws = new ClubWebService();
        AddTrainingRequest request = new AddTrainingRequest();
        request.setRequestCode(BigInteger.valueOf(requestCode));
        request.setToken(token);
        request.setTrainingName(content.getTrainingName());
        request.setTrainerName(content.getTrainerName());
        request.setTrainingStyle(content.getTrainingStyle());
        request.setTotalPlaces(content.getTotalPlaces());
        request.setDurationInMins(content.getDurationInMins());
        
        return ws.addTraining(request);
    }
}

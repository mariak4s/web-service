/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package src;

import ee.ttu.idu0075._2015.ws.club.AddClubRequest;
import ee.ttu.idu0075._2015.ws.club.AddClubResponse;
import ee.ttu.idu0075._2015.ws.club.AddClubTrainingRequest;
import ee.ttu.idu0075._2015.ws.club.AddClubTrainingResponse;
import ee.ttu.idu0075._2015.ws.club.ClubTrainingListType;
import ee.ttu.idu0075._2015.ws.club.ClubType;
import ee.ttu.idu0075._2015.ws.club.GetClubListRequest;
import ee.ttu.idu0075._2015.ws.club.GetClubListResponse;
import ee.ttu.idu0075._2015.ws.club.GetClubRequest;
import ee.ttu.idu0075._2015.ws.club.GetClubTrainingListRequest;
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
@Path("clubs")
public class ClubsResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ClubsResource
     */
    public ClubsResource() {
    }

    /**
     * Retrieves representation of an instance of src.ClubsResource
     * @param token
     * @param clubCity
     * @param clubCountry
     * @param clubStatus
     * @param hasRelatedTrainings
     * @throws InvalidTokenException
     * @return an instance of ee.ttu.idu0075._2015.ws.club.GetClubListResponse
     */

    @GET
    @Produces("application/json")
    public GetClubListResponse getClubList(@QueryParam("token") String token,
            @QueryParam("city") String clubCity,
            @QueryParam("country") String clubCountry,
            @QueryParam("status") String clubStatus,
            @QueryParam("trainings") String hasRelatedTrainings) throws InvalidTokenException {
        
        ClubWebService ws = new ClubWebService();
        GetClubListRequest request = new GetClubListRequest();
        
        request.setToken(token);
        request.setClubCity(clubCity);
        request.setClubCountry(clubCountry);
        request.setHasRelatedTrainings(hasRelatedTrainings);
        
        return ws.getClubList(request);
    }
    
    /**
     *
     * @param content
     * @param token
     * @param requestCode
     * @return 
     */
    
    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public AddClubResponse addClub(AddClubRequest content,
            @QueryParam("token") String token,
            @QueryParam("requestCode") int requestCode) throws InvalidTokenException, InvalidRequestCodeException {
        ClubWebService ws = new ClubWebService();
        AddClubRequest request = new AddClubRequest();
        request.setToken(token);
        request.setRequestCode(BigInteger.valueOf(requestCode));
        request.setClubName(content.getClubName());
        request.setClubCity(content.getClubCity());
        request.setClubCountry(content.getClubCountry());
        request.setClubStatus(content.getClubStatus());
        return ws.addClub(request);    
    }
    
    /**
     * Retrieves representation of an instance of src.ClubsResource
     * @param id
     * @param token
     * @throws InvalidTokenException
     * @return an instance of ee.ttu.idu0075._2015.ws.club.ClubType
     */
    @GET
    @Path("{id : \\d+}") //digits only
    @Produces("application/json")
    public ClubType getClub(@PathParam("id") String id,
            @QueryParam("token") String token) throws InvalidTokenException {
        ClubWebService ws = new ClubWebService();
        GetClubRequest request = new GetClubRequest();
        request.setId(BigInteger.valueOf(Integer.parseInt(id)));
        request.setToken(token);
        return ws.getClub(request);
    }
    /**
     * PUT method for updating or creating an instance of ClubsResource
     * @param content representation for the resource
     * @param token
     * @param requestCode
     * @param id
     * @throws InvalidTokenException
     * @throws InvalidRequestCodeException
     * @return AddClubTrainingResponse
     */
    @POST
    @Path("{id: \\d+}/trainings")
    @Consumes("application/json")
    @Produces("application/json")
    public AddClubTrainingResponse addClubTraining (AddClubTrainingRequest content,
            @QueryParam("token") String token, 
            @QueryParam("requestCode") int requestCode,
            @PathParam("id") String id) throws InvalidTokenException, InvalidRequestCodeException {
        ClubWebService ws = new ClubWebService();
        AddClubTrainingRequest request = new AddClubTrainingRequest();
        request.setToken(token);
        request.setRequestCode(BigInteger.valueOf(requestCode));
        request.setClubId(BigInteger.valueOf(Integer.parseInt(id)));
        request.setTrainingId(content.getTrainingId());
        request.setStartDate(content.getStartDate());
        request.setEndDate(content.getEndDate());
        request.setStatus(content.getStatus());
        return ws.addClubTraining(request);
    }

    @GET
    @Path("{id : \\d+}/trainings") //support digit only
    @Produces("application/json")
    public ClubTrainingListType getClubTrainingList(@PathParam("id") String id,
            @QueryParam("token") String token) throws InvalidTokenException{
        ClubWebService ws = new ClubWebService();
        GetClubTrainingListRequest request = new GetClubTrainingListRequest();
        request.setClubId(BigInteger.valueOf(Integer.parseInt(id)));
        request.setToken(token);
        return ws.getClubTrainingList(request);
    }
}

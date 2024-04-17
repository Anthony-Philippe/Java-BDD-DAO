package com.course.message;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.ArrayList;
import java.util.List;

@Path("/api/message")
public class MessageResource {
    private List<Perso> persoList = new ArrayList<>();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    @Incoming("perso-channel")
    public void processMessage(Perso perso) {
        persoList.add(perso);
    }

    @GET
    @Path("/persoList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Perso> getPersoList() {
        return persoList;
    }

    @GET
    @Path("/countByHouse")
    @Produces(MediaType.TEXT_PLAIN)
    public int countPersoByHouse(@QueryParam("house") Houses house) {
        return (int) persoList.stream().filter(perso -> perso.getHouse().equals(house)).count();
    }
}
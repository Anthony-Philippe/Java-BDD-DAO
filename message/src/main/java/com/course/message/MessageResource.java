package com.course.message;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import java.util.ArrayList;
import java.util.List;

@Path("/api/message")
public class MessageResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy";
    }

    private final List<Perso> persoList = new ArrayList<>();
    @Incoming("channel")
    public void processMessage(Perso perso) {
        persoList.add(perso);
    }

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Perso> getPersoList() {
        return persoList;
    }

    @GET
    @Path("/countByHouse")
    @Produces(MediaType.TEXT_PLAIN)
    public int countPersoByHouse(@QueryParam("house") Houses house) {
        int count = 0;
        for (Perso perso : persoList) {
            if (perso.getHouse() == house) {
                count++;
            }
        }
        return count;
    }
}
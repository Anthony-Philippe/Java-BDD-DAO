package com.course;

import com.course.api.CharactersApi;
import com.course.model.InlineResponse2004;
import com.course.model.CharacterModel;
import com.course.model.InlineResponse2005;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.io.IOException;
import java.util.List;

@Path("/hello")
public class ExampleResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @GET
    @Path("/characters")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CharacterModel> getCharacters() {
        return list;
    }

    CharactersApi charactersApi;
    @GET
    @Path("/characters/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CharacterModel getCharacter(@PathParam("id") String id) {
        return charactersApi.getCharacter(id).getData();
    }

    //Get all characters
    @GET
    @Path("/characters/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CharacterModel> getAllCharacters(){
        return list;
    }

    ObjectMapper objectMapper = new ObjectMapper();
    List<CharacterModel> list = objectMapper.readerForListOf(CharacterModel.class).readValue(getClass().getClassLoader().getResourceAsStream("hp.json"));

    public ExampleResource() throws IOException {
        list = objectMapper.readerForListOf(CharacterModel.class).readValue(getClass().getClassLoader().getResourceAsStream("hp.json"));
    }

    @Inject
    EntityManager entityManager;

    @POST
    @Path("/database")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String initializeDatabase() {
        try {
            for (CharacterModel characterModel : list) {
                entityManager.persist(characterModel);
            }
            return "Database initialized successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to initialize database: " + e.getMessage();
        }
    }
}

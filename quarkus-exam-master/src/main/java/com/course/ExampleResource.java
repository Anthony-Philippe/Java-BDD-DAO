package com.course;

import com.course.model.CharacterModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Path("/index")
public class ExampleResource {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Hello from Quarkus REST";
    }

    @Inject
    @Channel("character-created")
    Emitter<String> characterEmitter;

    @POST
    @Path("/database")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String database() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<CharacterModel> list = objectMapper.readerForListOf(CharacterModel.class).readValue(getClass().getClassLoader().getResourceAsStream("hp.json"));
        for (CharacterModel characterModel : list) {
            characterModel.getAttributes().persist();
            characterModel.persist();
            characterEmitter.send("New character added: " + characterModel.getJsonId());
        }
        return "Database provided by the json";
    }

    //GET on /
    @GET
    @Path("/characters")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CharacterModel> findAll() {
        return CharacterModel.listAll();
    }

    //GET on character/{id}
    @GET
    @Path("/characters/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public CharacterModel findCharacterById(@PathParam("id") String id) {
        return CharacterModel.findById(Optional.ofNullable(id));
    }

    //GET on houses/{house}
    @GET
    @Path("/characters/byHouse/{house}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CharacterModel> findCharactersByHouse(@PathParam("house") String house) {
        return CharacterModel.findByHouse(house);
    }

    //GET on houses-count/{house}
    @GET
    @Path("/characters/countByHouse/{house}")
    @Produces(MediaType.TEXT_PLAIN)
    public int countCharactersByHouse(@PathParam("house") String house) {
        return CharacterModel.countByHouse(house);
    }

    //GET on species/{specie}
    @GET
    @Path("/characters/bySpecies/{species}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CharacterModel> findCharactersBySpecies(@PathParam("species") String species) {
        return CharacterModel.findBySpeciesLike(species);
    }
}
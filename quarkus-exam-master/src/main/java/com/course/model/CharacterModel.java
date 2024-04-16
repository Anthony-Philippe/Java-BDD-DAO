package com.course.model;

import com.course.Houses;
import com.course.dto.Character;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;

import java.util.List;
import java.util.Optional;

/**
 * Representation of a character.
 **/
@Entity
@NamedQueries({
        @NamedQuery(name = "Character.getBySpecies", query = "from CharacterModel where attributes.species like ?1")
})
public class CharacterModel extends PanacheEntity {

    /**
     * The unique identifier of the character. Must be a valid UUID v4.
     **/
    private String jsonId = null;

    private String type = null;
    @OneToOne
    private Character attributes = new Character();
    @jakarta.persistence.Transient
    private CharacterLinks links = new CharacterLinks();
    public static List<CharacterModel> findByHouse(String houseName) {
        return find("attributes.house = ?1", houseName).list();
    }
    public static List<CharacterModel> findBySpeciesLike(String species) {
        return find("#Character.getBySpecies", species).list();
    }

    @JsonProperty("id")
    public String getJsonId() {
        return jsonId;
    }

    @JsonProperty("id")
    public void setJsonId(String id) {
        this.jsonId = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Character getAttributes() {
        return attributes;
    }

    public void setAttributes(Character attributes) {
        this.attributes = attributes;
    }

    public CharacterLinks getLinks() {
        return links;
    }

    public void setLinks(CharacterLinks links) {
        this.links = links;
    }

    //GET on /
    public List<CharacterModel> findAll(String name) {
        return CharacterModel.listAll();
    }
    //GET on character/{id}
    public List<CharacterModel> findById(Long id) {
        return CharacterModel.findById(Optional.ofNullable(id));
    }
    //GET on houses/{house}
    public List<CharacterModel> findByHouse(Houses house) {
        return CharacterModel.findByHouse(house.name());
    }

    //GET on houses-count/{house}
    public int countByHouse(Houses house) {
        return CharacterModel.findByHouse(house.name()).size();
    }
}

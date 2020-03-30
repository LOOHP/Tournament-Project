package com.loohp.tournament.Player;

import java.util.UUID;

public class Player {

    UUID id;
    String name;    
    String school;
    boolean seeded;

    public Player (UUID id, String name, String school, boolean seeded) {
        this.id = id;
        this.name = name;
        this.school = school;
        this.seeded = seeded;
    }
    
    public UUID getId() {
    	return this.id;
    }
    
    public void setId(UUID Id) {
    	this.id = Id;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getSchool() {
    	return this.school;
    }
    
    public void setSchool(String school) {
    	this.school = school;
    }
    
    public boolean getSeeded() {
    	return this.seeded;
    }
    
    public void setSeeded(boolean seeded) {
    	this.seeded = seeded;
    }

}
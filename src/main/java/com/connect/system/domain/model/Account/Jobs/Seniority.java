package com.connect.system.domain.model.Account.Jobs;


import java.util.List;
import java.util.ArrayList;

public enum Seniority {
    LEANER("LEANER"),
    ASSISTANT("ASSISTANT"),
    INTERN("INTERN"),
    TRAINEE("TRAINEE"),
    JUNIOR("JUNIOR"),
    PLENO("PLENO"),
    SPECIALIST("SPECIALIST"),
    SENIOR("SENIOR"),
    MASTER("MASTER");

    private String seniority;

    private Seniority(String seniority) {
        this.seniority = seniority;}
    public String getSeniority() {
        return seniority;
    }
    public List<String> obtainSeniority() {
        List<String> senioritys = new ArrayList<>();
        for (Seniority nivel : Seniority.values()) {
            senioritys.add(nivel.toString());
        }
        return senioritys;
    }

}

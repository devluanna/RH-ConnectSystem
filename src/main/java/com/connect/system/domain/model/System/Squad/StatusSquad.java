package com.connect.system.domain.model.System.Squad;

public enum StatusSquad {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String status_squad;

    private StatusSquad(String status_squad){
        this.status_squad = status_squad;
    }


}

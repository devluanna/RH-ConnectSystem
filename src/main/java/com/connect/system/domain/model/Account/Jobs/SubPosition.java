package com.connect.system.domain.model.Account.Jobs;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


public enum SubPosition {

    NOTAPPLICABLE("NOTAPPLICABLE"),
    ADMINISTRATIVEMANAGEMENT("ADMINISTRATIVEMANAGEMENT"),
    MEMBER("MEMBER"),
    TEACHLEAD("TEACHLEAD"),

    TEAMLEADER("TEAMLEADER"),

    PRODUCTOWNER("PRODUCTOWNNER"),

    PROJECTMANAGER("PROJECTMANAGER");


    private String sub_position;

    private SubPosition(String sub_position) {
        this.sub_position = sub_position;
    }

    public String getSub_position() {
        return sub_position;
    }

    public List<String> obtainSubPosition() {
        List<String> positions = new ArrayList<>();
        for (SubPosition position : SubPosition.values()) {
            positions.add(position.toString());
        }
        return positions;
    }
}

package com.connect.system.domain.model.Account.Jobs;


import java.util.ArrayList;
import java.util.List;

public enum TypeOfRecord {

    TECHNOLOGY("TECHNOLOGY"),
    ADMINISTRATION("ADMINISTRATION"),
    HUMANRESOURCES("HUMAN RESOURCES");

    private String type_of_record;

    private TypeOfRecord(String type_of_record) {
        this.type_of_record = type_of_record;
    }

    public String getType_of_record() {
        return type_of_record;
    }

    public List<String> obtainType() {
        List<String> types = new ArrayList<>();
        for (TypeOfRecord areas : TypeOfRecord.values()) {
            types.add(areas.toString());
        }
        return types;
    }
}

package com.connect.system.domain.model.Account.Jobs;

import java.util.ArrayList;
import java.util.List;

public enum Office {

    CEO("CHIEFEXECUTIVEOFFICER"),
    PRESIDENT("PRESIDENT"),
    DIRECTOR("DIRECTOR"),
    HEADDELIVERY("HEADDELIVERY"),
    MANAGER("MANAGER"),
    COORDINATOR("COORDINATOR"),
    ASSISTANT("ASSISTANT"),
    ENGINEER("ENGINEER"),
    ANALYST("ANALYST"),
    DEVELOPER("DEVELOPER");

    private String office;

    private Office(String office) {
        this.office = office;
    }

    public String getOffice() {
        return office;
    }

    public List<String> obtainOffice() {
        List<String> offices = new ArrayList<>();
        for (Office roles : Office.values()) {
            offices.add(roles.toString());
        }
        return offices;
    }

}

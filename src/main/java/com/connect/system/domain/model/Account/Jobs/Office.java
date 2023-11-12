package com.connect.system.domain.model.Account.Jobs;

import java.util.ArrayList;
import java.util.List;

public enum Office {

    EXECUTIVE("EXECUTIVE"),
    DIRECTOR("DIRECTOR"),
    MANAGER("MANAGER"),
    COORDINATOR("COORDINATOR"),
    PRODUCTOWNER("PRODUCTOWNER"),
    TEACHLEAD("TEACHLEAD"),
    TEAMLEADER("TEAMLEADER"),
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

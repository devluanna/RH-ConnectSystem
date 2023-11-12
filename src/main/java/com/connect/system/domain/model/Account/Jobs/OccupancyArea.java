package com.connect.system.domain.model.Account.Jobs;

import java.util.ArrayList;
import java.util.List;

public enum OccupancyArea {

    PROJECTS("PROJECTS"),
    PORTFOLIO("PORTFOLIO"),
    TESTER("TESTER(QA)"),
    MOBILE("MOBILE"),
    WEB("WEB"),
    WEBANDMOBILE("WEB AND MOBILE"),
    DEVOPS("DEVOPS"),
    SECURITY("SECURITY"),
    SOLUTIONSARCHITECT("SOLUTIONS ARCHITECT"),
    AWSARCHITECT("AWS ARCHITECT"),
    CLOUDARCHITECT("CLOUD ARCHITECT");

    private String occupancy_area;

    private OccupancyArea(String occupancy_area) {
        this.occupancy_area = occupancy_area;
    }

    public List<String> obtainOccupancyArea() {
        List<String> occupancys = new ArrayList<>();
        for (OccupancyArea areas : OccupancyArea.values()) {
            occupancys.add(areas.toString());
        }
        return occupancys;
    }

}

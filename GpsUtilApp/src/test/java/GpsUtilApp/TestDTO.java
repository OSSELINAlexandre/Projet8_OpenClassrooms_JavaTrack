package GpsUtilApp;

import java.util.Comparator;

public class TestDTO {

    public String nameAttraction;
    public Double distance;

    public TestDTO(){

    }
    public TestDTO(String nameAttraction, Double distance){
        this.nameAttraction = nameAttraction;
        this.distance = distance;
    }

    public String getNameAttraction() {
        return nameAttraction;
    }

    public void setNameAttraction(String nameAttraction) {
        this.nameAttraction = nameAttraction;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }



}

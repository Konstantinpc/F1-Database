package sim.subd;

public class GrandPrix {
    private String location;
    private String name;
    private Integer laps;
    private Integer is_rain;
    private Double length;
    private Integer turns;

    public GrandPrix(String location, String name, Integer laps, Integer is_rain, Double length, Integer turns) {
        this.location=location;
        this.name=name;
        this.laps = laps;
        this.is_rain = is_rain;
        this.length = length;
        this.turns = turns;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    public Integer getIs_rain() {
        return is_rain;
    }

    public void setIs_rain(Integer is_rain) {
        this.is_rain = is_rain;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getTurns() {
        return turns;
    }

    public void setTurns(Integer turns) {
        this.turns = turns;
    }
}

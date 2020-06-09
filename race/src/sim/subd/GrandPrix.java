package sim.subd;

public class GrandPrix {
    private Integer laps;
    private Boolean is_rain;
    private Double lap_length;
    private Integer turns;

    public GrandPrix(Integer laps, Boolean is_rain, Double lap_length, Integer turns) {
        this.laps = laps;
        this.is_rain = is_rain;
        this.lap_length = lap_length;
        this.turns = turns;
    }

    public Integer getLaps() {
        return laps;
    }

    public void setLaps(Integer laps) {
        this.laps = laps;
    }

    public Boolean getIs_rain() {
        return is_rain;
    }

    public void setIs_rain(Boolean is_rain) {
        this.is_rain = is_rain;
    }

    public Double getLap_length() {
        return lap_length;
    }

    public void setLap_length(Double lap_length) {
        this.lap_length = lap_length;
    }

    public Integer getTurns() {
        return turns;
    }

    public void setTurns(Integer turns) {
        this.turns = turns;
    }
}

package sim.subd;

import java.util.Random;

public class Contestant{
    private String name;
    private Double speed;
    public String time;
    private Teams team;
    private Integer points=0;
    private Quality quality;

    public Contestant(String name, Teams team, Quality quality) {
        this.name = name;
        this.team = team;
        this.points = 0;
        this.quality = quality;
        this.speed = team.getSpeed();
    }

    public Contestant() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void calcSpeed(boolean rain){
        if(rain) setSpeed(getSpeed()*0.4);
        Random rand = new Random();
        double randomValue = getQuality().getMin() + (getQuality().getMax() - getQuality().getMin()) * rand.nextDouble();
        setSpeed(getSpeed()+getSpeed()*randomValue);
    }

    public String calcTime(double length, int laps){
        int time_h = (int) (length/getSpeed());
        int time_m = (int) (((length/getSpeed())-time_h)*60);
        int time_s = (int) (((((length/getSpeed())-time_h)*60)-time_m)*60);
        int time_ms = (int) (((((((length/getSpeed())-time_h)*60)-time_m)*60)-time_s)*100);
        time = (time_h +":"+time_m+":"+time_s+":"+time_ms);
        return time;
    }

    public Teams getTeam() {
        return team;
    }

    public void setTeam(Teams team) {
        this.team = team;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Quality getQuality() {
        return quality;
    }

    public void setQuality(Quality quality) {
        this.quality = quality;
    }
}

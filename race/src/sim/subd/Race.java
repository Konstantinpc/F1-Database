package sim.subd;

import java.text.CollationElementIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Race {
    private List<Contestant> contestants = new ArrayList<>();

    private List<Integer> points = Arrays.asList(25, 18, 15, 12, 10, 8, 6, 4, 2, 1);

    public Race() {}

    public List<Contestant> getContestants() {
        return contestants;
    }

    public void addContestant(Contestant new_contestant){
        contestants.add(new_contestant);
    }

    public void resetContestants(){
        for(int i=0; i<contestants.size(); i++){
            contestants.get(i).setTime("0");
        }
    }

    public void simulateRace(double length, boolean r, int laps){

        for(int j=0; j<contestants.size(); j++) {
            contestants.get(j).calcSpeed(r);
            contestants.get(j).setTime(contestants.get(j).calcTime(length, laps));
        }

        Collections.sort(contestants, new ByTimeComparator());
        //resetContestants();
        stringContestants();
    }

    public void createStanding(){
        List<Contestant> standing = new ArrayList<>(contestants);
        Collections.sort(standing, new PointsComparator());
        for(int i=0; i<standing.size(); i++){
            System.out.println((i+1)+". "+ standing.get(i).getName()+" "+standing.get(i).getPoints());
        }
    }

    public void stringContestants(){
        for(int i=0; i<contestants.size(); i++){
            if(i<9) {
                System.out.println((i+1)+". "+ contestants.get(i).getName()+" "+contestants.get(i).getTime() +" "+points.get(i));
                contestants.get(i).setPoints(contestants.get(i).getPoints()+points.get(i));
            }else{
                System.out.println((i+1)+". "+ contestants.get(i).getName() +" "+contestants.get(i).getTime() + " 0");
                contestants.get(i).setPoints(contestants.get(i).getPoints()+0);
            }
        }
        System.out.println("");

        resetContestants();
    }
}

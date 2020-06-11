package sim.subd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.CollationElementIterator;
import java.util.*;

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

    public void simulateRace(double length, int r, int laps, int id){

        for(int j=0; j<contestants.size(); j++) {
            contestants.get(j).calcSpeed(r);
            contestants.get(j).setTime(contestants.get(j).calcTime(length, laps));
        }

        Collections.sort(contestants, new ByTimeComparator());
        stringContestants(id);
    }

    public void stringContestants(int id){
        try {
            //Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Connect to server and db
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/F1_Season?serverTimezone=" + TimeZone.getDefault().getID(),"root","samolevski");
            int d=0;
            int p=0;
            for(int i=0; i<contestants.size(); i++){
                if(i<10) {
                    System.out.println((i+1)+". "+ contestants.get(i).getName()+" - "+contestants.get(i).getTime() +" - "+points.get(i));
                    contestants.get(i).setPoints(contestants.get(i).getPoints()+points.get(i));


                    PreparedStatement stmt = con.prepareStatement("UPDATE Contestants SET Points=Points+"+points.get(i)+" WHERE Name='" +contestants.get(i).getName()+"';");
                    int affectedRows = stmt.executeUpdate();
                    p=points.get(i);
                }else{
                    System.out.println((i+1)+". "+ contestants.get(i).getName() +" - "+contestants.get(i).getTime() + " - 0");
                    contestants.get(i).setPoints(contestants.get(i).getPoints()+0);
                    p=0;
                }
                PreparedStatement res = con.prepareStatement("SELECT Id FROM Contestants WHERE Name='"+contestants.get(i).getName()+"'");
                ResultSet r = res.executeQuery();

                while(r.next()){
                    d = r.getInt(1);
                }


                PreparedStatement h = con.prepareStatement("INSERT INTO History(Id, Quasification, Id_pilot, Time, Points_race, Id_gp) " +
                        "VALUES (NULL, "+(i+1)+", "+d+", '"+contestants.get(i).getTime()+"', "+p+", "+id+");");
                int affectedRows = h.executeUpdate();

            }
        }catch(Exception e){ System.out.println(e);}


        resetContestants();
    }
}

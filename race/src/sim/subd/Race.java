package sim.subd;

import java.sql.*;
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
        Connection con=null;
        PreparedStatement query=null;
        ResultSet rs=null;
        Integer affectedRows;
        try {
            //Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Connect to server and db
            con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/F1_Season?serverTimezone=" + TimeZone.getDefault().getID(),"root","samolevski");
            con.setAutoCommit(false);
            int id_pilot=0;
            int points_for_table=0;
            for(int i=0; i<contestants.size(); i++){
                if(i<10) {
                    System.out.println((i+1)+". "+ contestants.get(i).getName()+" - "+contestants.get(i).getTime() +" - "+points.get(i));
                    contestants.get(i).setPoints(contestants.get(i).getPoints()+points.get(i));
                    query = con.prepareStatement("UPDATE Contestants SET Points=Points+"+points.get(i)+" WHERE Name='" +contestants.get(i).getName()+"';");
                    affectedRows = query.executeUpdate();
                    if(affectedRows == 1) { con.commit(); }
                    else { con.rollback(); }
                    points_for_table=points.get(i);
                }else{
                    System.out.println((i+1)+". "+ contestants.get(i).getName() +" - "+contestants.get(i).getTime() + " - 0");
                    contestants.get(i).setPoints(contestants.get(i).getPoints()+0);
                }
                PreparedStatement res = con.prepareStatement("SELECT Id FROM Contestants WHERE Name='"+contestants.get(i).getName()+"'");
                ResultSet r = res.executeQuery();

                while(r.next()){
                    id_pilot = r.getInt(1);
                }
                PreparedStatement h = con.prepareStatement("INSERT INTO History(Id, Quasification, Id_pilot, Time, Points_race, Id_gp) " +
                        "VALUES (NULL, "+(i+1)+", "+id_pilot+", '"+contestants.get(i).getTime()+"', "+points_for_table+", "+id+");");
                affectedRows = h.executeUpdate();
                if(affectedRows == 1) { con.commit(); }
                else { con.rollback(); }
            }
            con.commit();
        }catch (SQLException | ClassNotFoundException ex) {
            try{
                if(con != null)
                    con.rollback();
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
            System.out.println(ex.getMessage());
        } finally {
            try {
                if(con != null) con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        resetContestants();
    }
}

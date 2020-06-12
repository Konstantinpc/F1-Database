package sim.subd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;
import java.util.TimeZone;

public class Main {
    public static void main(String[] args) {
        Connection con=null;
        Statement query=null;
        PreparedStatement query1=null;
        ResultSet rs=null;
        Integer affectRows=null;

        try{
            //Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Connect to server and db
            con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/F1_Season?serverTimezone=" + TimeZone.getDefault().getID(),"root","samolevski");
            con.setAutoCommit(false);
            //Create tables
            query = con.createStatement();
            query.execute("CREATE TABLE IF NOT EXISTS Contestants( Id INTEGER AUTO_INCREMENT NOT NULL, Name VARCHAR(40)," +
                    "Team VARCHAR(40), Quality VARCHAR(40), Points INTEGER, Is_active BOOLEAN, PRIMARY KEY(Id, Name));");
            query.execute("CREATE TABLE IF NOT EXISTS GrandsPrix( Id INTEGER AUTO_INCREMENT NOT NULL, Location VARCHAR(40)," +
                    " Name VARCHAR(40), Laps INTEGER, Is_rain BOOLEAN, Length DOUBLE, Turns INTEGER, Is_over BOOLEAN, PRIMARY KEY(Id));");
            query.execute("CREATE TABLE IF NOT EXISTS History( Id INTEGER AUTO_INCREMENT NOT NULL, Quasification Integer," +
                    " Id_pilot INTEGER, Time VARCHAR(40), Points_race INTEGER, Id_gp INTEGER, FOREIGN KEY(Id_pilot) REFERENCES Contestants(Id)," +
                    " FOREIGN KEY(Id_gp) REFERENCES GrandsPrix(Id), PRIMARY KEY(Id));");

            //Input in table Contestants
            Scanner scan = new Scanner(System.in);
            System.out.println("Current Contestants:");
            query1 = con.prepareStatement("SELECT * FROM Contestants;");
            rs = query1.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1)+". "+rs.getString(2)+" - "+ Teams.valueOf(rs.getString(3))+" - "+ Quality.valueOf(rs.getString(4)));
            }
            query1 = con.prepareStatement("SELECT COUNT(*) FROM Contestants;");
            rs = query1.executeQuery();
            while(rs.next()){
                if(rs.getInt(1)==0){
                    System.out.println("No one contestant!");
                    System.out.println("Input Contestants:");
                    while(true) {
                        String line = scan.nextLine();
                        if(line.equals("")){
                            break;
                        }else {
                            String[] arrOfStr = line.split(", ", 0);
                            query1 = con.prepareStatement("INSERT INTO Contestants(Id, Name, Team, Quality, Points, Is_active) " +
                                    "VALUES (NULL, '"+arrOfStr[0]+"', '"+arrOfStr[1]+"', '" + arrOfStr[2]+"', 0, 1);");
                            affectRows = query1.executeUpdate();
                            if(affectRows == 1) { con.commit(); }
                            else { con.rollback(); }
                        }
                    }
                }else{
                    while(true) {
                        String line = scan.nextLine();
                        if (line.equals("")) {
                            break;
                        }
                    }
                }
            }

            //Input in table GrandsPrix
            Scanner scann = new Scanner(System.in);
            query1 = con.prepareStatement("SELECT COUNT(*) FROM GrandsPrix;");
            rs = query1.executeQuery();
            while(rs.next()){
                if(rs.getInt(1)==0){
                    System.out.println("No GrandsPrix!");
                    System.out.println("Input GrandsPrix:");
                    while(true) {
                        String line = scann.nextLine();
                        if(line.equals("")){
                            break;
                        }else {
                            String[] arrOfStr = line.split(", ", 0);
                            query1 = con.prepareStatement("INSERT INTO GrandsPrix(Id, Location, Name, Laps," +
                                    " Is_rain, Length, Turns, Is_over) " +
                                    "VALUES (NULL, '"+arrOfStr[0]+"', '"+arrOfStr[1]+"', "+Integer.parseInt(arrOfStr[2])+", "+Integer.parseInt(arrOfStr[5])+", "
                                    + Double.parseDouble(arrOfStr[3])+", "+Integer.parseInt(arrOfStr[4])+", 0);");
                            affectRows = query1.executeUpdate();
                            if(affectRows == 1) { con.commit(); }
                            else { con.rollback(); }
                        }
                    }
                }else{
                    System.out.println("Passed GP:");
                    query1 = con.prepareStatement("SELECT * FROM GrandsPrix WHERE Is_over=1;");
                    rs = query1.executeQuery();
                    while (rs.next()) {
                        System.out.println(rs.getString(1)+". "+rs.getString(2)+" - "+ rs.getString(3)+" - "+
                                rs.getInt(4)+" - "+rs.getDouble(6)+" - "+rs.getInt(7));
                    }
                    System.out.println("Not passed GP:");
                    query1 = con.prepareStatement("SELECT * FROM GrandsPrix WHERE Is_over=0;");
                    rs = query1.executeQuery();
                    while (rs.next()) {
                        System.out.println(rs.getString(1)+". "+rs.getString(2)+" - "+ rs.getString(3)+" - "+
                                rs.getInt(4)+" - "+rs.getDouble(6)+" - "+rs.getInt(7));
                    }
                    while(true) {
                        String line = scann.nextLine();
                        if (line.equals("")) {
                            break;
                        }
                    }
                }
            }

            //Select for simulation, delete from table Contestants, update in table standing
            Scanner scan_for_sim = new Scanner(System.in);
            System.out.println("Start simulation:");
            while(true) {
                String line = scan_for_sim.nextLine();
                Race race = new Race();
                String[] arrOfStr = line.split(" ", 0);
                if (arrOfStr[0].equals("sim")) {

                    //Select all Constants for the race
                    query1 = con.prepareStatement("SELECT * FROM Contestants WHERE Is_active=1;");
                    rs = query1.executeQuery();
                    while (rs.next()) {

                        race.addContestant(new Contestant(rs.getString(2), Teams.valueOf(rs.getString(3)), Quality.valueOf(rs.getString(4))));
                    }
                    //Select current GrandPrix
                    query1 = con.prepareStatement("SELECT * FROM GrandsPrix WHERE Is_over=0 LIMIT 1;");
                    rs = query1.executeQuery();

                    while (rs.next()) {
                        System.out.println(rs.getString(2) + " - " + rs.getString(3) + " 2020:");
                        race.simulateRace(rs.getDouble(6), rs.getInt(5), rs.getInt(4), rs.getInt(1));
                    }
                    System.out.println("");

                    query1 = con.prepareStatement("UPDATE GrandsPrix SET Is_over=1 WHERE Is_over=0 LIMIT 1;");
                    affectRows = query1.executeUpdate();
                    if(affectRows == 1) { con.commit(); }
                    else { con.rollback(); }
                    query1 = con.prepareStatement("SELECT COUNT(Is_over) FROM GrandsPrix WHERE Is_over=0;");
                    rs = query1.executeQuery();
                    int check = 0;
                    while (rs.next()) {
                        if (rs.getInt(1) == 0) {
                            System.out.println("Write 'restart'/'continue'");
                            check = 1;
                        }
                    }
                    while (check == 1) {
                        line = scann.nextLine();
                        if (line.equals("restart")) {
                            System.out.println("Restart the simulator");
                            System.out.println("");
                            query1 = con.prepareStatement("DELETE FROM History;");
                            affectRows = query1.executeUpdate();
                            query1 = con.prepareStatement("UPDATE GrandsPrix SET Is_over=0;");
                            affectRows = query1.executeUpdate();
                            query1 = con.prepareStatement("UPDATE Contestants SET Points=0;");
                            affectRows = query1.executeUpdate();
                            con.commit();
                            check = 0;
                            break;
                        } else if (line.equals("continue")) System.out.println("");break;
                    }

                }else if (line.equals("end")) break;
                else if(arrOfStr[0].equals("standing")){
                    if(arrOfStr[1].equals("pilots")){
                        query1 = con.prepareStatement("SELECT ROW_NUMBER() OVER (ORDER BY Points DESC) row_num, Name, Team, Points FROM Contestants ORDER BY Points DESC;");
                        rs = query1.executeQuery();
                        System.out.println("Standing Pilots:");
                        while (rs.next()) {
                            System.out.println(rs.getInt(1)+". "+rs.getString(2) + " - " + rs.getString(3) + " - "+rs.getInt(4));
                        }
                    }else if(arrOfStr[1].equals("teams")){
                        query1 = con.prepareStatement("SELECT ROW_NUMBER() OVER (ORDER BY Points DESC) row_num, Team, SUM(Points) FROM Contestants GROUP BY Team ORDER BY Points DESC;");
                        rs = query1.executeQuery();
                        System.out.println("Standing Teams:");
                        while (rs.next()) {
                            System.out.println(rs.getInt(1)+". "+ rs.getString(2) + " - "+rs.getInt(3));
                        }
                    }
                    System.out.println("");
                }
                else if(arrOfStr[0].equals("history")){
                    query1 = con.prepareStatement("SELECT h.Quasification, c.Name, c.Team, h.Time, h.Points_race, gp.Location FROM History h" +
                            " LEFT JOIN Contestants c ON c.Id=h.Id_pilot" +
                            " LEFT JOIN GrandsPrix gp ON gp.Id=h.Id_gp" +
                            " WHERE gp.Location='"+arrOfStr[1]+"';");
                    rs = query1.executeQuery();
                    System.out.println("Full history "+arrOfStr[1]+":");
                    while (rs.next()) {
                        System.out.println(rs.getInt(1) + ". " + rs.getString(2) + " - " + rs.getString(3) + " - " + rs.getString(4) + " - " +
                                rs.getInt(5));
                    }
                    System.out.println("");
                }else if(line.equals("change pilot")){
                    Scanner scanner = new Scanner(System.in);
                    String f_pilot_name = scanner.nextLine();
                    query1=con.prepareStatement("UPDATE Contestants SET Is_Active=0 WHERE Name='"+f_pilot_name+"'");
                    affectRows=query1.executeUpdate();
                    query1=con.prepareStatement("SELECT * FROM Contestants WHERE Name='"+f_pilot_name+"';");
                    rs=query1.executeQuery();
                    String s=new String();
                    while(rs.next()){
                        s=rs.getString(3);
                    }
                    System.out.println("to");
                    scanner = new Scanner(System.in);
                    String s_pilot = scanner.nextLine();
                    String[] arrOfpilot = s_pilot.split(", ", 0);
                    query1=con.prepareStatement("INSERT INTO Contestants(Id, Name, Team, Quality, Points, Is_active)" +
                            " VALUES (NULL, '"+arrOfpilot[0]+"', '"+s+"', '"+arrOfpilot[1]+"', 0, 1)");
                    affectRows=query1.executeUpdate();
                    System.out.println("");
                    con.commit();

                }else if(line.equals("restart")){
                    System.out.println("Restart the simulator");
                    System.out.println("");
                    query1 = con.prepareStatement("DELETE FROM History;");
                    affectRows = query1.executeUpdate();
                    query1 = con.prepareStatement("UPDATE GrandsPrix SET Is_over=0;");
                    affectRows = query1.executeUpdate();
                    query1 = con.prepareStatement("UPDATE Contestants SET Points=0;");
                    affectRows = query1.executeUpdate();
                    query1 = con.prepareStatement("DELETE FROM Contestant WHERE Is_active=0;");
                    affectRows = query1.executeUpdate();
                    con.commit();
                }
            }
            con.commit();
        } catch (SQLException | ClassNotFoundException ex) {
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

    }
}

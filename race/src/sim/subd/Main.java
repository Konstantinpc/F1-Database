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


        try{
            //Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            //Connect to server and db
            Connection con=DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/F1_Season?serverTimezone=" + TimeZone.getDefault().getID(),"root","samolevski");

            //Create tables
            Statement query = con.createStatement();
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
            PreparedStatement query1 = con.prepareStatement("SELECT * FROM Contestants;");
            ResultSet rs = query1.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1)+". "+rs.getString(2)+" - "+ Teams.valueOf(rs.getString(3))+" - "+ Quality.valueOf(rs.getString(4)));
            }
            System.out.println("Input Contestants:");
            while(true) {
                String line = scan.nextLine();
                if(line.equals("")){
                    break;
                }else {

                    String[] arrOfStr = line.split(", ", 0);
                    query1 = con.prepareStatement("INSERT INTO Contestants(Id, Name, Team, Quality, Points, Is_active) " +
                            "VALUES (NULL, '"+arrOfStr[0]+"', '"+arrOfStr[1]+"', '" + arrOfStr[2]+"', 0, 1);");
                    int affectRows = query1.executeUpdate();
                }
            }

            //Input in table GrandsPrix
            Scanner scann = new Scanner(System.in);
            System.out.println("Input GrandsPrix:");
            System.out.println("Passed:");
            query1 = con.prepareStatement("SELECT * FROM GrandsPrix WHERE Is_over=1;");
            rs = query1.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1)+". "+rs.getString(2)+" - "+ rs.getString(3)+" - "+
                        rs.getInt(4)+" - "+rs.getDouble(6)+" - "+rs.getInt(7));
            }
            System.out.println("Not passed:");
            query1 = con.prepareStatement("SELECT * FROM GrandsPrix WHERE Is_over=0;");
            rs = query1.executeQuery();
            while (rs.next()) {
                System.out.println(rs.getString(1)+". "+rs.getString(2)+" - "+ rs.getString(3)+" - "+
                        rs.getInt(4)+" - "+rs.getDouble(6)+" - "+rs.getInt(7));
            }
            while(true) {
                String line = scann.nextLine();
                if(line.equals("")){
                    break;
                }else {
                    String[] arrOfStr = line.split(", ", 0);
                    query1 = con.prepareStatement("INSERT INTO GrandsPrix(Id, Location, Name, Laps," +
                            " Is_rain, Length, Turns, Is_over) " +
                            "VALUES (NULL, '"+arrOfStr[0]+"', '"+arrOfStr[1]+"', "+Integer.parseInt(arrOfStr[2])+", NULL, "
                            + Double.parseDouble(arrOfStr[3])+", "+Integer.parseInt(arrOfStr[4])+", 0);");
                    int affectRows = query1.executeUpdate();
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
                    PreparedStatement stmt = con.prepareStatement("SELECT * FROM Contestants;");
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        race.addContestant(new Contestant(rs.getString(2), Teams.valueOf(rs.getString(3)), Quality.valueOf(rs.getString(4))));
                    }
                    //Select current GrandPrix
                    stmt = con.prepareStatement("SELECT * FROM GrandsPrix WHERE Is_over=0 LIMIT 1;");
                    ResultSet rs_gp = stmt.executeQuery();

                    while (rs_gp.next()) {
                        System.out.println(rs_gp.getString(2) + " - " + rs_gp.getString(3) + " 2020:");
                        race.simulateRace(rs_gp.getDouble(6), rs_gp.getInt(5), rs_gp.getInt(4), rs_gp.getInt(1));
                    }
                    PreparedStatement stmt2 = con.prepareStatement("UPDATE GrandsPrix SET Is_over=1 WHERE Is_over=0 LIMIT 1;");
                    int affectedRows = stmt2.executeUpdate();

                }else if(arrOfStr[0].equals("end")) break;
                else if(arrOfStr[0].equals("standing")){
                    PreparedStatement stmt = con.prepareStatement("SELECT ROW_NUMBER() OVER (ORDER BY Points DESC) row_num, Name, Team, Points FROM Contestants ORDER BY Points DESC;");
                    ResultSet rst = stmt.executeQuery();
                    System.out.println("Standing:");
                    while (rst.next()) {
                        System.out.println(rst.getInt(1)+". "+rst.getString(2) + " - " + rst.getString(3) + " - "+rst.getInt(4));
                    }
                }
                else if((arrOfStr[0]+" "+arrOfStr[1]).equals("full history")){
                    Statement querye = con.createStatement();
                    querye.execute("CREATE TABLE IF NOT EXISTS History( Id INTEGER AUTO_INCREMENT NOT NULL, Quasification Integer," +
                            " Id_pilot INTEGER, Time VARCHAR(40), Points_race INTEGER, Id_gp INTEGER, FOREIGN KEY(Id_pilot) REFERENCES Contestants(Id)," +
                            " FOREIGN KEY(Id_gp) REFERENCES GrandsPrix(Id), PRIMARY KEY(Id));");
                    PreparedStatement stmt = con.prepareStatement("SELECT h.Quasification, c.Name, c.Team, h.Time, h.Points_race, gp.Location FROM History h" +
                            " LEFT JOIN Contestants c ON c.Id=h.Id_pilot" +
                            " LEFT JOIN GrandsPrix gp ON gp.Id=h.Id_gp;");
                    ResultSet rst = stmt.executeQuery();
                    System.out.println("Full history:");
                    while (rst.next()) {
                        System.out.println(rst.getInt(1) + ". " + rst.getString(2) + " - " + rst.getString(3) + " - " + rst.getString(4) + " - " +
                                rst.getInt(5) + " - " + rst.getString(6));
                    }

                }
            }
            con.close();
        }catch(Exception e){ System.out.println(e);}

    }
}

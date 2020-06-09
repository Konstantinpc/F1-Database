package sim.subd;

public class Main {
    public static void main(String[] args) {

        Contestant p1 = new Contestant("Daniel Richiardo", Teams.RENAULT, Quality.STAR);
        Contestant p2 = new Contestant("Lewis Hamilton", Teams.MERCEDES, Quality.SUPERSTAR);
        Contestant p3 = new Contestant("Sebastien Vettel", Teams.FERRARI, Quality.STAR);
        Contestant p4 = new Contestant("Charles Leclerc", Teams.FERRARI, Quality.WUNDERKIND);
        Contestant p5 = new Contestant("Max Verstappen", Teams.RED_BULL, Quality.WUNDERKIND);

        GrandPrix gp1 = new GrandPrix(53, false, 309.690, 17);
        GrandPrix gp2 = new GrandPrix(53, true, 306.720, 11);


        Race race = new Race();
        race.addContestant(p1);
        race.addContestant(p2);
        race.addContestant(p3);
        race.addContestant(p4);
        race.addContestant(p5);
        race.simulateRace(gp1.getLap_length(), gp1.getIs_rain(), gp1.getLaps());
        race.simulateRace(gp2.getLap_length(), gp2.getIs_rain(), gp2.getLaps());

        race.createStanding();

    }
}

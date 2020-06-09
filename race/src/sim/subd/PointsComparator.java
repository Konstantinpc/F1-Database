package sim.subd;

import java.util.Comparator;

public class PointsComparator implements Comparator<Contestant> {

    @Override
    public int compare(Contestant contestant, Contestant t1) {
        return t1.getPoints().compareTo(contestant.getPoints());

    }
}

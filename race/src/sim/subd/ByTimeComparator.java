package sim.subd;

import java.util.Comparator;

public class ByTimeComparator implements Comparator<Contestant> {

    @Override
    public int compare(Contestant contestant, Contestant t1) {
        String[] arrOfStr = contestant.getTime().split(":", 0);
        Integer h = Integer.parseInt(arrOfStr[0]);
        Integer m = Integer.parseInt(arrOfStr[1]);
        Integer s = Integer.parseInt(arrOfStr[2]);
        Integer ms = Integer.parseInt(arrOfStr[3]);

        String[] arrOfStr2 = t1.getTime().split(":", 0);
        Integer h2 = Integer.parseInt(arrOfStr2[0]);
        Integer m2 = Integer.parseInt(arrOfStr2[1]);
        Integer s2 = Integer.parseInt(arrOfStr2[2]);
        Integer ms2 = Integer.parseInt(arrOfStr2[3]);
        if (h.compareTo(h2) == 0) {
            if (m.compareTo(m2) == 0) {
                if (s.compareTo(s2) == 0) {
                    return ms.compareTo(ms2);
                } else return s.compareTo(s2);
            } else return m.compareTo(m2);
        } else return h.compareTo(h2);

    }
}

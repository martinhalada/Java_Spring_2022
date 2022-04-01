import org.hamcrest.CoreMatchers;
import org.junit.Assert;

import static org.junit.jupiter.api.Assertions.*;

class KalkulackaTest {

    @org.junit.jupiter.api.Test
    public void vypocitej_soucet() {
        Kalkulacka kalkulacka = new Kalkulacka(1,2,'+');
        double spravne = 3;
        assertEquals(spravne,kalkulacka.vypocitej());
    }

    @org.junit.jupiter.api.Test
    public void vypocitej_rozdil() {
        Kalkulacka kalkulacka = new Kalkulacka(1,2,'-');
        double spravne = -1;
        assertEquals(spravne,kalkulacka.vypocitej());
    }

    @org.junit.jupiter.api.Test
    public void vypocitej_soucin() {
        Kalkulacka kalkulacka = new Kalkulacka(2,2,'*');
        double spravne = 4;
        assertEquals(spravne,kalkulacka.vypocitej());
    }

    @org.junit.jupiter.api.Test
    public void vypocitej_podil() {
        Kalkulacka kalkulacka = new Kalkulacka(1,2,'/');
        double spravne = 0.5;
        assertEquals(spravne,kalkulacka.vypocitej());
    }
}
import java.util.ArrayList;
import java.util.Collections;

public class Kupcek {

    private ArrayList<Karta> kup; //seznam vseh 52 kart

    public Kupcek() {
        kup = new ArrayList<Karta>();

        for (int i = 0; i < 4; i++) { //gremo preko vseh simbolo kart
            for (int j = 0; j < 13; j++) { //in njihovih rangov
                if (j == 0) { //prvi indeks je vedno as torej mu damo vrednost 11
                    Karta card = new Karta(i, j, 11); //ustvarimo nasega asa z i-tim simbolom in j-tim rangom
                    kup.add(card); //karto dodamo v kupcek
                }
                else if (j >= 10) { //podobno storimo za karte Fant, Kraljica, Kralj in jim dodamo vrednost 10
                    Karta card = new Karta(i, j, 10);
                    kup.add(card);
                }
                else { //za vse preostale karte povecamo vrednost za +1 indeksa zaradi poteka kart 2[1] 3[2] etc.
                    Karta card = new Karta(i, j, j+1);
                    kup.add(card);
                }
            }
        }
    }
//metode za mesanje kart ter, vzemanje iz kupcka. sprotoma seveda odstranemo karto iz kupcka ki smo jo vzeli
    public void premesaj() {
        Collections.shuffle(kup);
    }
    public Karta vzemiKarto(int i) {
        return kup.get(i);
    }
    public Karta odstraniKarto(int i) {
        return kup.remove(i);
    }
}
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.io.IOException;

public class Karta {
    private int simbol; //simbol na karti karo[0] kriz[1] srce[3] pik[4]
    private int rang; //rang karte as[0] 2[1] 3[2] 4[3] 5[4] 6[5] 7[6] 8[7] 9[8] 10[9] fant[10] kraljica[11] kralj[12]
    private int vrednost; //vrednost karte od 1 do 11, kjer 1 in 11 zavzame vrednost as
    private int xOs; // pozicija na x osi
    private int yOs; // pozicija na y osi

    public Karta() { //privzete vrednosti
        simbol = 0;
        rang = 0;
        vrednost = 0;
    }

    public Karta(int s, int r, int v) { //
        simbol = s;
        rang = r;
        vrednost = v;
    }
//metode ki vrnejo simbol, vrednost in rang karte
    public int getSimbol() {
        return simbol;
    }
    public int getRang() {
        return rang;
    }
    public int getVrednost() {
        return vrednost;
    }

    public void izrisKarte(Graphics2D g1, boolean nasprotnikVrsta, boolean navzdol, int stKarte) throws IOException {//metoda ki izrise karte iz nase slike
        BufferedImage deckImg = ImageIO.read(new File("Slike/vseKarte.png")); //preberemo naso sliko kart
        int sirinaSlike = 950; //dimenzije nase slike morajo biti natancne
        int visinaSlike = 392;
// ustvarimo matriko [][] za x,y os
        BufferedImage[][] vseKarte = new BufferedImage[4][13]; //ker imamo 4 razlicne simbole in 13 kart za vsak simbol
        BufferedImage ozadjeKarte = ImageIO.read(new File("Slike/ozadjeKarte.PNG")); //ponazori drug stran kart
//"izrezemo" iz nase vecje slike vse karte ki lezijo relativno na sliki, delimo z 13 in 4 da se "premikamo" desno in navzdol po sliki
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                vseKarte[i][j] = deckImg.getSubimage(j *sirinaSlike/13, i* visinaSlike /4, sirinaSlike/13, visinaSlike /4);
            }
        }
//te karte nato izrisemo glede na pozicijo igralcev, nasprotnik na vrhu igralec pa na dnu
        if (nasprotnikVrsta) {
            yOs = 80;
        }
        else {
            yOs = 400;
        }

        xOs = 400 + 80 * stKarte; // da se karte ne prekrivajo jih sprotoma na desno premikamo
//preverimo ce je karta navzdol obrnjena, ce je jo izrisemo navzdol obrnjeno tako da narisemo njeno ozadje
        if (navzdol) {
            g1.drawImage(ozadjeKarte, xOs, yOs, null );
        }
        else {
            g1.drawImage(vseKarte[simbol][rang], xOs, yOs, null); //drugace jo izrisemo navzgor obrnjeno
        }
    }
}










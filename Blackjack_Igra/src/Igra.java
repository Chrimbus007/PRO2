import java.util.ArrayList;
import java.awt.event.*;
import javax.swing.*;

public class Igra {

    ArrayList<Karta> nasprotnikRoka; //seznam kart za nasprotnikovo roko in igralcevo rkok
    ArrayList<Karta> igralecRoka;

    public boolean navzdol; //preveri ce je karta navzdol obrnjena
    public boolean nasprotnikZmaga; //preveri ce je nasprotnik zmagal
    public volatile boolean konecRunde; //preveri ali je konec runde , volatile je za sinhronizacijo med Thread razredi v test



    JFrame frame; //ustvarimo novo okno
    Kupcek kupcek; //ustvarimo kupcek kart
    Interakcija Igralnica; //klicemo razred Interakcija da ustvarimo naso Igralnico in nase karte
    Interakcija grafKarta;

    JButton butHit; //4 gumbi, 3 za dejansko igranje igre in 1 za izhod iz igralnice
    JButton butStand;
    JButton butDouble;
    JButton butIzhod;

    public Igra(JFrame f) {//ustvarimo igro ki prejme kot parameter nas frame
        kupcek = new Kupcek(); //ustvarimo nov kupcek ki ga premesamo
        kupcek.premesaj();
        nasprotnikRoka = new ArrayList<Karta>(); //ustvarimo sezname kjer bodo hranjene karte ki jih dobita igralec in nasprotnik
        igralecRoka = new ArrayList<Karta>();
        Igralnica = new Interakcija(nasprotnikRoka, igralecRoka); //klicemo konstruktor iz razreda interakcija
        frame = f;
        navzdol = true; //karte so vedno navzdol obrnjene
        nasprotnikZmaga = true; //nasprotnik vedno "zmaga" prvo igro ki jo nato odstejemo
        konecRunde = false; //runda se ne konca ob pricetku igre
    }

    public void grafikaIgre() {//ustvarimo grafiko za naso igro

        frame.setTitle("Igra Blackjack"); //naslov igralnega okna
        frame.setSize(1130, 665); //velikost se ujema z velikostjo ozadja
        frame.setLocationRelativeTo(null); //centriramo okno
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); //preprecimo da se velikost spreminja

        butHit = new JButton("HIT"); //ustvarimo gumbe in jim dolocimo velikost ter pozicijo na igralnem polju
        butHit.setBounds(420, 550, 100, 50); //We set their bounds.
        butStand = new JButton("STAND");
        butStand.setBounds(520, 550, 100, 50);
        butDouble = new JButton("DOUBLE");
        butDouble.setBounds(620, 550, 100, 50);
        butIzhod = new JButton("IZHOD");
        butIzhod.setBounds(900, 550, 150, 50);

        frame.add(butHit); //dodamo gumbe v naso okno
        frame.add(butStand);
        frame.add(butDouble);
        frame.add(butIzhod);

        butIzhod.addActionListener(new ActionListener() { //potreben poslusalec za dogodek ko se igra zapre
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Zapustili ste igro"); //povemo uporabniku da je zapustil igro
                System.exit(0); //privzeti ukaz za izhod iz igre
            }
        });
//dodamo k oknu karte ki jih imata nasa igralca
        Igralnica = new Interakcija(nasprotnikRoka, igralecRoka);
        Igralnica.setBounds(0, 0, 1130, 665);
        frame.add(Igralnica);
        frame.setVisible(true);
    }

    public void zazeniIgro() { //metoda za zagon dejanske igre

        for(int i = 0; i<2; i++) { //nasprotnik vedno pricne s prvimi 2 kartami
            nasprotnikRoka.add(kupcek.vzemiKarto(i));
        }
        for(int i = 2; i<4; i++) { //naslednji 2 karti pa damo igralcu
            igralecRoka.add(kupcek.vzemiKarto(i));
        }
        for (int i = 0; i < 4; i++) { //nato jih le se odstranimo iz kupcka kart, ker smo jih izvlekli
            kupcek.odstraniKarto(0);
        }
//dodamo karte ki jih imata igralec in nasprotnik na igralno polje
        grafKarta = new Interakcija(nasprotnikRoka, igralecRoka);
        grafKarta.setBounds(0, 0, 1130, 665);
        frame.add(grafKarta);
        frame.setVisible(true);

        pregled(nasprotnikRoka); //vedno pregledamo nasprotnikovo roko saj on zacne z 2 kartama vedno
        pregled(igralecRoka);

        butHit.addActionListener(new ActionListener() { //poslusalec za gumb HIT
            public void actionPerformed(ActionEvent e) {

                dodajKarto(igralecRoka); //dodamo karto iz kupcka igralcu
                pregled(igralecRoka); //preverimo ce ni presegel 17, kar je pogoj da se igra nadaljuje
                if (vsotaKart(igralecRoka)<17 && vsotaKart(nasprotnikRoka)<17){
                    dodajKarto(nasprotnikRoka);
                    pregled(nasprotnikRoka);
                }
            }
        });

        butDouble.addActionListener(new ActionListener() {//poslusalec za gumb DOUBLE
            public void actionPerformed(ActionEvent e) {

                dodajKarto(igralecRoka); //igralcu dodamo 2 karte
                dodajKarto(igralecRoka);
                pregled(igralecRoka); //preverimo njegovo roko in naredimo isto kot pri poslusalcu za HIT
                if (vsotaKart(igralecRoka)<17 && vsotaKart(nasprotnikRoka)<17){
                    dodajKarto(nasprotnikRoka);
                    pregled(nasprotnikRoka);
                }
            }
        });

        butStand.addActionListener(new ActionListener() {//poslusalec za gumb STAND
            public void actionPerformed(ActionEvent e) {
                while (vsotaKart(nasprotnikRoka)<17) { //ker stand ne da karte igralcu preverimo le nasprotnikovo roko in mu dodamo karto
                    dodajKarto(nasprotnikRoka);
                    pregled(nasprotnikRoka);
                }
                if ((vsotaKart(nasprotnikRoka)<21) && vsotaKart(igralecRoka)<21) { // ce nihce ni presegel 21 zmaga tisti ki ima vecjo vrednost kart
                    if(vsotaKart(igralecRoka) > vsotaKart(nasprotnikRoka)) {
                        navzdol = false;
                        nasprotnikZmaga = false;
                        JOptionPane.showMessageDialog(frame, "Igralec je zmagal to rundo");
                        sleep();
                        konecRunde = true;
                    }
                    else {
                        navzdol = false;
                        JOptionPane.showMessageDialog(frame, "Nasprotnik je zmagal to rundo");
                        sleep();
                        konecRunde = true;
                    }
                }
            }
        });
    }
    public void dodajKarto(ArrayList<Karta> roka) {//metoda za dodajanje in odstranjevanje kart v roke in iz kupcka
        roka.add(kupcek.vzemiKarto(0));
        kupcek.odstraniKarto(0);
        navzdol = true;
    }

    public void pregled(ArrayList<Karta> roka) {//metoda ki preveri ce je igralec/nasprotnik izgubil ker je v prvih 2 kartah ze dosegel 21
        if (roka.equals(igralecRoka)) {
            if(vsotaKart(roka) == 21){ //ce je igralec dosegel 21 v prvih dveh kartah
                navzdol = false;
                nasprotnikZmaga = false; //ker je igralec zmagal
                JOptionPane.showMessageDialog(frame, "Igralec je zmagal"); //sporocimo uporabniku
                sleep(); //in uspavamo program za 1 sekundo
                konecRunde = true;
            }
            else if (vsotaKart(roka) > 21) { //ce je vrednost kart v igralcevi roki vecja od 21 je izgubil
                navzdol = false; JOptionPane.showMessageDialog(frame, "Igralec je izgubil"); //sporocimu uporabniku
                sleep(); //uspavamo za 1 sekundo in koncamo rundo
                konecRunde = true;
            }
        }
        else { //preverimo se nasprotnikovo roko
            if(vsotaKart(roka) == 21) {
                navzdol = false;
                JOptionPane.showMessageDialog(frame, "Nasprotnik je zmagal");
                sleep();
                konecRunde = true;
            }
            else if (vsotaKart(roka) > 21) { //enako kot pri igralcu preverimo
                navzdol = false;
                nasprotnikZmaga = false;
                JOptionPane.showMessageDialog(frame, "Nasprotnik je izgubil");
                sleep();
                konecRunde = true;
            }
        }
    }

//metode za karto As ki lahko zavzame vrednost 11 ali 1
    public boolean jeAs(ArrayList<Karta> roka) {//preverimo ce je v roki as
        for (int i = 0; i < roka.size(); i++){
            if(roka.get(i).getVrednost() == 11) {
                return true; //ce smo ga nasli vrnemo true
            }
        }
        return false; //ce asa nismo nasli v roki
    }

    public int stAsov(ArrayList<Karta> roka){ //preverimo stevilo asov v rokah, za to da se odlocimo ali je as vreden 11 ali 1
        int stAsov = 0; //nastavimo stevec asov na 0
        for (int i = 0; i < roka.size(); i++) { //gremo cez seznam kart
            if(roka.get(i).getVrednost() == 11) { //ker je privzeta vrednost asa 11 smo as nasli in povecamo stevec za 1
                stAsov++;
            }
        }
        return stAsov; //vrnemo stevilo asov
    }

    public int vsotaAsov(ArrayList<Karta> roka) { //metoda ki izracuna vsoto vrednosti kart ce vsebuje asa z 11
        int vsota = 0;
        for (int i = 0; i < roka.size(); i++){
            vsota = vsota + roka.get(i).getVrednost();
        }
        return vsota;
    }

    public int vsotaKart(ArrayList<Karta> roka) {//vsota kart v primeru ce as stejemo kot vrednost 1
        if(jeAs(roka)) { //ce smo nasli asa v roki
            if(vsotaAsov(roka) <= 21) { //ce vsota ne preseze 21
                return vsotaAsov(roka); //vrnemo vsoto z asom vrednosti 11
            }
            else{
                for (int i = 0; i < stAsov(roka); i++) { //ce presezemo 21 z asom v roki
                    int vsota = vsotaAsov(roka)-(i+1)*10; //od vsakega asa odstejemo vrednost 10
                    if(vsota <= 21) { //ce nismo sli cez 21 po tej operaciji vrnemo vsoto
                        return vsota;
                    }
                }
            }
        }
        else { //ce asov sploh nimamo v roki sestejemo vse vrednosti dosedanjih kart
            int vsota = 0;
            for (int i = 0; i < roka.size(); i++) {
                vsota = vsota + roka.get(i).getVrednost();
            }
            return vsota;
        }
        return 22; //vrnemo 22 oz "izgubimo" rundo ce vsaka od prejsnjih metod ni uspela
    }

    public static void sleep() {// metoda uspava program za 1 sekundo
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {}
    }

}

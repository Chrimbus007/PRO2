import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import java.io.*;
import javax.swing.JComponent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.awt.*;



//implementiramo poslusalec miske za nas JComponent
public class Interakcija extends JComponent implements MouseListener {
    public BufferedImage ozadje; // izrisali bomo 2 slike, sliko za zeton in sliko za ozadje nase igre
    public BufferedImage zeton;
    private ArrayList<Karta> nasprotnikRoka; //2 seznama za karte ki jih imata igralca v roki
    private ArrayList<Karta> igralecRoka;
    private int nasprotnikZmaga; //stevca za stevilo zmag nasprotnika in igralca
    private int igralecZmaga;
    public boolean navzdol = true; //preverimo ce je karta navzdol ali navzgor obrnjena, privzamemo da so vse karte navzdol obrnjene
    public static boolean narejenaStava = false; //preverimo ce je igralec naredil stavo, pred pricetkom igre je ne naredi
    private int trenutnaBilanca; //nase trenutna bilanca vseh zetonov
    public static int stava; //koliko stavimo na rundo

    public Interakcija(ArrayList<Karta> nas, ArrayList<Karta> igr) { //konstruktor za razred ki vzame 2 seznama ki predstavljata karte v rokah igralcev
        nasprotnikRoka = nas;
        igralecRoka = igr;
        nasprotnikZmaga = 0; //stevca zacneta z 0
        igralecZmaga = 0;
        trenutnaBilanca = 2000; //nastavimo zacetno bilanco ki se ujema z bilanco v razredu test
        addMouseListener(this); //dodamo poslusalec miske
    }
//uporabimo vgrajeno metodo paintComponent za risanje
    public void paintComponent(Graphics g) {
        Graphics2D g1 = (Graphics2D) g;

        try {
            ozadje = ImageIO.read(new File("Slike/ozadje.png")); //nastavimo ozadje in sliko zetona
            zeton = ImageIO.read(new File("Slike/zeton3.png"));
        }
        catch(IOException e) {}
//izrisemo slike, tekst in nastavimo velikost in vrsto ter barvo pisave v paintComponent
        g1.drawImage(ozadje, 0, 0, null);
        g1.drawImage(zeton, 50, 300, null);
        g1.setColor(Color.WHITE);
        g1.setFont(new Font("Arial", Font.BOLD, 22));
        g1.drawString("Nasprotnik", 500, 50); //
        g1.drawString("Igralec", 500, 380);
        g1.drawString("Nasprotnik je zmagal : ", 10, 100); //predstavimo igralcu kolikokrat je zmagal igralec in kolikokrat nasprotnik
        g1.drawString("Igralec je zmagal: ", 20, 150);
        g1.drawString(Integer.toString(nasprotnikZmaga), 300, 100);
        g1.drawString(Integer.toString(igralecZmaga), 300, 150);
        g1.setFont(new Font("Arial", Font.BOLD, 15));
        g1.drawString("Pritisnite na zeton da pricnete staviti", 50, 250);
        g1.setFont(new Font("Arial", Font.BOLD, 20));
        g1.drawString("Trenutna Bilanca: " + trenutnaBilanca, 50, 550);



        try { //izrisemo karte ki jih ima nasprotnik v roki
            for (int i = 0; i < nasprotnikRoka.size(); i++) {//gremo preko kart
                if (i == 0) { //preverimo prvo karto in pregledamo ce je navzdol obrnjena
                    if(navzdol) {
                        nasprotnikRoka.get(i).izrisKarte(g1, true, true, i); //izrisemo vsako karto posamezno
                    }
                    else {
                        nasprotnikRoka.get(i).izrisKarte(g1, true, false, i); //ce ni navzdol obrnjena izrisemo posamezno karto
                    }
                }
                else {
                    nasprotnikRoka.get(i).izrisKarte(g1, true, false, i); //ce ni prva karta jo izrisemo
                }
            }
        }
        catch (IOException e) {}

        try { //izrisemo karte za igralcevo roko
            for (int i = 0; i < igralecRoka.size(); i++) { //podobno naredimo za igralca kot za nasprotnika vendar je manj dela kr igralec vedno vidi svoje karte
                igralecRoka.get(i).izrisKarte(g1, false, false, i);
            }
        }
        catch (IOException e) {}
    }

    public void osvezi(int bil, int igZM, int nasZM, boolean nz) { //metoda osvezi nas JComponent ko je klicana
        trenutnaBilanca = bil;
        igralecZmaga = igZM;
        nasprotnikZmaga = nasZM;
        navzdol = nz;
        this.repaint();
    }
//nadzorujemo kaj se zgodi z miskinim klikom
    public void mousePressed(MouseEvent e) {
        int mouseX = e.getX(); //koordinate cursorja
        int mouseY = e.getY();

        if(mouseX>= 50 && mouseX<=200 && mouseY>=300 && mouseY<=450) {//dogodek se zgodi samo ce uporabnik klikne na sliko zetona
            narejenaStava = true; //potrdimo da je bila stava narejena
            String[] options = new String[] {"5", "10", "20", "100"}; //nastavimo gumbe za stave ki so na voljo
            int response = JOptionPane.showOptionDialog(null, "Prosimo izberite velikost stave", "Stavljenje v poteku", 
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]); //uporabniku pokazemo da poteka stava.
            if(response == 0) {//igralec stavi po gumbih z indeksi od leve proti desni 5 = [0], 10 = [1] etc.
                stava = 5;
                trenutnaBilanca -= 5;
            }
            else if(response == 1) {
                stava = 10;
                trenutnaBilanca -= 10;
            }
            else if(response == 2) {
                stava = 20;
                trenutnaBilanca -= 20;
            }
            else if(response == 3) {
                stava = 100;
                trenutnaBilanca -= 100;
            }
            else { //za privzeto vzamemo 5 zetonov za stavo, v primeru da uporabnik okno zapre brez stave
                stava = 5;
                trenutnaBilanca -= 5;
            }
            System.out.println("Stavili ste: " + stava);

            Test.novaIgra.zazeniIgro(); //nato pricnemo igro
        }

    }
    //preostalih metod za misko ne potrebujemo vendar jih program zahteva
    public void mouseExited(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseClicked(MouseEvent e) {

    }

}

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;


public class Interakcija_Meni extends JComponent implements ActionListener{ //uporabimo JComponent ker imamo veliko objektov ki jih bomo uporabljali

    private JButton butIgraj = new JButton("Igraj"); //ustvarimo 3 gumbe za nas meni
    private JButton butIzhod = new JButton("Izhod");
    private JButton butNav = new JButton("Navodila");
    private static BufferedImage ozadje; //ustvarimo tudi sliko za ozadje

    public Interakcija_Meni() { //konstruktor za razred
        butIgraj.addActionListener(this); //vsem gumbom dodamo poslusalce dogodkov
        butIzhod.addActionListener(this);
        butNav.addActionListener(this);
    }

    public void paintComponent(Graphics g) { //uporabimo paintComponent za risanje nasega menija
        Graphics2D g1 = (Graphics2D) g;

        try {
            ozadje = ImageIO.read(new File("Slike/ozadje.png"));
        }
        catch(IOException e) {}

        g1.drawImage(ozadje, 0, 0, null); //narisemo naso ozadje v meni
        g1.setFont(new Font("Arial", Font.BOLD, 80)); //dodamo naslov in spremenimo velikost ter pisavo naslova
        g1.setColor(Color.WHITE);
        g1.drawString("Blackjack", 350, 100);


        butIgraj.setBounds(500, 300, 150, 80); //nastavimo kje bodo gumbi
        butIzhod.setBounds(500, 400, 150, 80);
        butNav.setBounds(80, 75, 150, 80);
        butIgraj.setFont(new Font("Arial", Font.BOLD, 30)); //in njihovo pisavo in velikost
        butIzhod.setFont(new Font("Arial", Font.BOLD, 30));
        butNav.setFont(new Font("Arial", Font.BOLD, 20));

        super.add(butIgraj); //dodamo k JComponent te gumbe
        super.add(butIzhod);
        super.add(butNav);
    }
    public void actionPerformed(ActionEvent e) {//nadzorujemo delovanje gumbov
        JButton izbran = (JButton)e.getSource();//gumb nastavimo v spremenljivko izbran

        if(izbran == butIzhod) { //ce izberemo izhod zapustimo igro
            System.exit(0);
        }
        else if(izbran == butIgraj) { //ce kliknemo na gumb Igraj pricnemo igro
            Test.trenutnoStanje = Test.StanjeIgre.Igra; //enacimo trenutno stanje oken, ker zapustimo meni ga odstranimo skupaj v celoti
            Test.MeniOkno.dispose();
            Test.osvezitevIgre.start(); //nato pricnemo 2 operaciji, osvezitev igre in preverjanje igre v razredu test
            Test.preveriIgro.start();
        }
        else if(izbran == butNav) {//s klikom na gumb prikazemo navodila igre
            JOptionPane.showMessageDialog(this, "Cilj igre je premagati nasprotnikovo roko brez prekoracitve vrednosti 21" +
                            "\nAsi so vredni 1 in 11 tock. Fant, Kraljica, Kralj 10, karte z stevilkami pa njihova pripadajoca vrednost. Avtomaticno zmagate ce v prvih dveh kartah dobite 21.", "Navodila za Igro", JOptionPane.INFORMATION_MESSAGE);
        }

    }

}
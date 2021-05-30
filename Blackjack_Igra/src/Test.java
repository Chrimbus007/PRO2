import javax.swing.JFrame;

public class Test {
//ustvarimo 2 okna, enega za igro in enega za meni
    public static JFrame MeniOkno = new JFrame();
    public static JFrame IgralnoOkno = new JFrame();
//stevca zmag za nasprotnika in igralca ter zacetna bilanca ki jo sprotoma zvisujemo/znizujemo v poteku igre
    private static int IgralecZmaga = 0;
    private static int NasprotnikZmaga = 0;
    public static int trenutnaBilanca = 2000;

    public static Igra novaIgra = new Igra(IgralnoOkno); //ustvarimo razred igra ki prejme kot ukaz JFrame
    private static boolean prvic = true; //preverimo ce se igra prvic igra

    public enum StanjeIgre { //predstavimo stanje igre. v enem stanju bo meni, v drugem pa igra
        Meni, Igra
    }

    public static StanjeIgre trenutnoStanje = StanjeIgre.Meni; //vedno pricnemo z menijem

    public static void main(String[] args) throws InterruptedException {
        if(trenutnoStanje == StanjeIgre.Meni) {
            odpiranje(); //odpremo meni
        }
    }

    public static void odpiranje() { //metoda za odpiranje menija
        MeniOkno.setTitle("Igra Blackjack"); //naslov menija
        MeniOkno.setSize(1130, 650); //nastavimo velikost okna da se ujema z velikostjo nasega ozadja
        MeniOkno.setLocationRelativeTo(null); //okno centriramo z ukazom null
        MeniOkno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MeniOkno.setResizable(false); //preprecimo da se okno lahko pomanjsa ali poveca

        Interakcija_Meni grafika = new Interakcija_Meni(); //uporabimo razred Interakcija_Meni ki vsebuje nase gumbe in slike za meni
        MeniOkno.add(grafika); //jo dodamo k nasemu meniju in ga naredimo vidnega uporabniku
        MeniOkno.setVisible(true);
    }

    public static Thread osvezitevIgre = new Thread () { //uporabimo vgrajeni razred Thread in pripadajoco metodo run da neprestano osvezujemo igro
        public void run () {
            while (true){ //ker se zanka ne sme prekiniti jo damo kot true
                novaIgra.Igralnica.osvezi(trenutnaBilanca, IgralecZmaga, NasprotnikZmaga -1, novaIgra.navzdol); //klicemo iz razreda Igra metode in dele za igro, nasprotnik zacne z 1 zmago avtomaticno zato jo odstejemo
            }
        }
    };

    public static Thread preveriIgro = new Thread () { //ta razred Thread pa pregleduje naso igro za scenarij ko se igra konca. enako uporabimo vgrajeno metodo run in zanko while ne prekinemo nikoli
        public void run () {
            while (true) {
                if (prvic || novaIgra.konecRunde) { // uporabimo metodo pregled iz razreda Igra da preverimo ce se igra prvic zacne in ali je konec runde
                    System.out.println("Osvezujem igro");
                    if (novaIgra.nasprotnikZmaga){//ce igralec zmaga mu pristejemo eno zmago,
                        NasprotnikZmaga++;
                        trenutnaBilanca -= Interakcija.stava; //tudi odstranimo vrednost stave iz nase bilance ker smo izgubili
                    }
                    else { //enako naredimo za igralca
                        IgralecZmaga++;
                        trenutnaBilanca += Interakcija.stava *2; //dodamo 2-krat naso zacetno stavo ker smo jo predhodno odstranili ko smo stavili iz nase bilance
                    }
                    IgralnoOkno.getContentPane().removeAll(); //odstranimo vse iz igralnega okna
                    novaIgra = new Igra(IgralnoOkno); //in zacnemo novo igro v istem oknu
                    novaIgra.grafikaIgre(); //ustvarimo novo igro v nasem oknu

                    prvic = false; //prvic nastavimo na false ker smo logicno igro ze igrali ce smo vse prej izvedli
                }
            }
        }
    };
}

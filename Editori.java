import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.util.*;

/**
 *Luokan avulla voi luoda omia kentti‰‰n pelattavaksi Labyrintti-peliss‰.
 *Editorissa on graafinen k‰yttˆliittym‰ jota k‰ytet‰‰n hiirell‰. Jos tekem‰‰ns‰
 *labyrinttia haluaa pelata, se pit‰‰ tallentaa. Tiedostot tallennetaan omalla
 *tiedostop‰‰tteell‰ varustettuna joten ne eiv‰t ylikirjoita muita tiedostoja.
 *
 * @author Joonatan Heiskanen
 * @version 1.00 2012/4/17
 */

public class Editori extends JFrame{

	/**Kehys jossa editori toimii.*/
	private static Editori ikkuna;
	/**Luotavan labyrintin leveys. Labyrintit yht‰ korkeita kuin leveit‰.*/
	private static int koko = 1;
	/**Tieto siit‰ mink‰ osan(sein‰, lattia, maali) k‰ytt‰j‰ haluaa lis‰t‰ labyrinttiin*/
	private static int valinta;
	/**JLabelin periv‰ olio jolla on lis‰ttyn‰ ominaisuutena tieto sen sijainnista. T‰m‰
	 *tieto piti lis‰t‰ jotta ohjelma tiet‰‰ mit‰ ruutua k‰ytt‰j‰ muokkaa.*/
	private TeeLabyrintti asd;
	/**TeeLabyrintti-oliot tallennetaan t‰h‰n taulukkoon.*/
	private static TeeLabyrintti[][] taulukko;
	/**X-koordinaatti*/
	private int i;
	/**Y-koordinaatti*/
	private int j;
	/**Merkkijonotaulukko johon tallennetaan merkkipohjaista esityst‰ labyrintist‰.
	 *T‰st‰ saatava merkkijono on se mit‰ tallennetaan tiedostoon, ja n‰in ollen m‰‰r‰‰
	 *labyrintin rakenteen.*/
	private static char [][] labyrintti;
	/**Muuttuja kertoo onko k‰ytt‰j‰lle jo n‰ytetty tiettyj‰ huomautuksia, jotta niit‰ ei
	 *n‰ytet‰ turhaan useampaa kertaa.*/
	private boolean ekaKerta = true;

	/**Editorin alustaja. Ensimm‰isen‰ luodaan rakenteen tallentava merkkijono, jonka j‰lkeen
	 *se alustetaan v‰lilyˆnneill‰. N‰in estet‰‰n tyhjien kohtien olemassaolo. T‰m‰n j‰lkeen
	 *JFrameen lis‰t‰‰n paneelit kent‰n muokkausta ja palojen valintaa varten. Sitten kent‰n
	 *muokkaukseen k‰ytett‰v‰ paneeli t‰ytet‰‰n JLabelin perivill‰ TeeLabyrintti-olioilla, joihin
	 *kuhunkin lis‰t‰‰n mouseListener ja kunkin ymp‰rille piirret‰‰n rajat. Kun jotakin ruutua
	 *klikataan ohjelma tarkistaa mik‰ pala on valittu ja piirt‰‰ vastaavan kuvan siihen ruutuun ja
	 *rakenteen tallentavaan merkkijonotaulukkoon.*/
	public Editori() {

		setLayout(new BorderLayout());
		//Paneeli jossa itse labyrinttia muokataan
		JPanel piirtoAlusta = new JPanel();
		piirtoAlusta.setLayout(new GridLayout(koko, koko));
		//Paneeli jossa sijaitsevat palan valitsemiseen k‰ytett‰v‰t nappulat
		JPanel valitsin = new JPanel();

		JButton seina = new JButton("Sein‰");
		seina.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				valinta = 1;//Sein‰
			}
		});
		JButton lattia = new JButton("Lattia");
		lattia.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				valinta = 2;//Lattia
			}
		});
		JButton teleportti= new JButton ("Teleportti");
		teleportti.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {

				valinta = 3;//Teleportti
			}
		});
		JButton maali = new JButton ("Maali");
		maali.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {

				valinta = 4;//Maali
			}
		});
		JButton hahmo = new JButton("Hahmo");
		hahmo.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				valinta = 5;//Hahmo
			}
		});
		JButton sieni = new JButton("Sieni");
		sieni.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				valinta = 6;//Sieni, eli suunnanvaihto
			}
		});

		valitsin.add(lattia);
		valitsin.add(seina);
		valitsin.add(teleportti);
		valitsin.add(maali);
		valitsin.add(hahmo);
		valitsin.add(sieni);

		//Tallennusnappula
		JButton tallenna = new JButton("Tallenna");
		tallenna.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				tallennaLabyrintti();
			}
		});
		add(tallenna, BorderLayout.EAST);
		/*T‰ytet‰‰n taulukko TeeLabyrintti-olioilla. Kuhunkin olioon tallennetaan sen sijainti,
		 *ja sille piirret‰‰n rajat jotta ruudut erottaa toisistaan. Lis‰t‰‰n myˆs kuuntelijat
		 *jotta jotain tapahtuu kun ruutuja klikataan*/
		taulukko = new TeeLabyrintti[koko][koko];
		for(i=0; i<taulukko.length;i++) {
			for (j=0; j<taulukko[i].length;j++) {
			asd = new TeeLabyrintti();
			taulukko[i][j] = asd;
			taulukko[i][j].setPaikka(i, j);
			taulukko[i][j].setBorder(BorderFactory.createLineBorder(Color.black));
			taulukko[i][j].addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					switch(valinta) {

						case 1://Sein‰
							piirraRuutu('O', new ImageIcon("kuvat/sein‰.jpg"), e);
							break;

						case 2://Lattia
							piirraRuutu(' ', new ImageIcon(), e);
							break;

						case 3://Teleportti
							if(ekaKerta) {
								tarkistaTeleportit();
							}
							piirraRuutu('*', new ImageIcon("kuvat/teleportti.png"), e);
							break;

						case 4://Maali
							piirraRuutu('@', new ImageIcon("kuvat/maali.png"), e);
							break;

						case 5://Hahmo
							piirraRuutu('#', new ImageIcon("kuvat/ukkeli.png"), e);
							break;
						case 6://Sieni
							piirraRuutu('§', new ImageIcon("kuvat/sieniMod.jpg"), e);
							break;
					}
				}
		});
			piirtoAlusta.add(taulukko[i][j]);
			taulukko[i][j].setSize(new Dimension(50,50));
			}
		}

		add(valitsin, BorderLayout.NORTH);
		add(piirtoAlusta, BorderLayout.CENTER);
		//Varmistetaan ett‰ k‰ytt‰j‰ ei vahingossa sulje editoria
		JButton lopeta = new JButton ("Sulje");
		lopeta.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				Object[] napit = {"Kyll‰", "Ei"};
				int valinta = JOptionPane.showOptionDialog (ikkuna,
															"Haluatko varmasti sulkea editorin?",
															"",
															JOptionPane.YES_NO_OPTION,
															JOptionPane.QUESTION_MESSAGE,
															null,
															napit,
															napit[1]);
				if (valinta == JOptionPane.YES_OPTION)
					ikkuna.dispose();
			}
		});
		add(lopeta, BorderLayout.SOUTH);
	}
	/**Sis‰luokka joka perii JLabelin. Lis‰yksen‰ olioon voi tallentaa tiedon sen paikasta,
	 *ja t‰m‰n tiedon voi myˆs oliosta hakea. N‰it‰ tietoja tarvitaan metodissa joka tutkii
	 *mit‰ ruutua k‰ytt‰j‰ haluaa muokata*/
	class TeeLabyrintti extends JLabel {
		private int paikkaI;
		private int paikkaJ;

		public void setPaikka(int paikkaI, int paikkaJ) {
			this.paikkaI = paikkaI;
			this.paikkaJ = paikkaJ;
		}

		public int getI() {
			return paikkaI;
		}

		public int getJ() {
			return paikkaJ;
		}
	}
	/**Metodi tarkastaa kent‰ll‰ olevien teleporttien m‰‰r‰n. Jos m‰‰r‰ on yli 2, k‰ytt‰j‰lle n‰ytet‰‰n varoitus.
	 *Teleporttien toiminta on puutteellista jos niit‰ laitetaan kent‰lle yli 2. Virheilmoitus n‰ytet‰‰n vain kerran.*/
	public void tarkistaTeleportit() {
		int portteja = 0;

		for (i = 0; i<labyrintti.length;i++) {
			for(j=0; j<labyrintti[i].length;j++){

				if(labyrintti[i][j] == '*')
					portteja++;
			}
		}
		if(portteja>1){
			JOptionPane.showMessageDialog(null,"Useamman kuin kahden teleportin k‰ytt‰minen ei ole suositeltavaa, tekij‰t eiv‰t vastaa virheellisest‰ toiminnasta.",
			"VAROITUS", JOptionPane.ERROR_MESSAGE);
			ekaKerta = false;
		}
	}
	/**Metodi piirt‰‰ oikean kuvan oikeaan kohtaan editorin ruudukkoa ja lis‰‰ oikean merkin oikeaan kohtaan
	 *rakenteen m‰‰r‰‰v‰‰n merkkijonotaulukkoon. Metodissa katsotaan mit‰ oliota klikattiin, jonka j‰lkeen
	 *katsotaan mitk‰ olivat sen x-ja y-koordinaatit. T‰m‰ on mahdollista koska jokaiseen ruutuun on
	 *tallennettu sen oma sijainti. "Piirt‰minen" tarkoittaa k‰yt‰nnˆss‰ olion ikonin vaihtoa.*/
	public void piirraRuutu(char merkki, ImageIcon kuva, MouseEvent e) {

			taulukko[((TeeLabyrintti)e.getSource()).getI()][((TeeLabyrintti)e.getSource()).getJ()].setIcon(kuva);
			labyrintti[(((TeeLabyrintti)e.getSource())).getI()][((TeeLabyrintti)e.getSource()).getJ()] = merkki;
	}
	
	public static void lataaLabyrintti() {
		boolean nimiVaarin = false;
		String tarkistus = "";
		
		do {
			nimiVaarin = true;
			try {
				tarkistus = JOptionPane.showInputDialog("Anna avattavan labyrintin nimi");
				File testi = new File(tarkistus + ".glb");
				if (testi.exists()) {
					nimiVaarin = false;
				}
				else if(tarkistus.equals("")) {
					JOptionPane.showMessageDialog(null, "Virheellinen tiedostonimi");
				}
				else
					JOptionPane.showMessageDialog(null, "Virheellinen tiedostonimi2");
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}while(nimiVaarin);

		File file = new File(tarkistus + ".glb");
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			/*Toistetaan lukua kunnes luettava rivi on tyhj‰. Koska labyrintin tyhj‰t kohdat
			 *ovat v‰lilyˆntej‰ luku ei katkea vaikka jollain rivill‰ olisikin pelkk‰‰ lattiaa*/
			while ((text = reader.readLine()) != null) {
			contents.append(text).append("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
		try {
			if (reader != null) {
			reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		  }
		}
		boolean ekalla = true;
		int rivinLeveys  =0;
		int rivej‰ = 0;
		for(int i = 0; i<contents.length(); i++) {
			if (contents.charAt(i) == '\n') {
				if (ekalla) {
					rivinLeveys = i;
					ekalla = false;
				}
				rivej‰++;
				contents.deleteCharAt(i);
			}
		}
		koko = rivinLeveys;
		luoTaulukko();
		for (int i=0; i<rivej‰; i++) {
			for (int j=0; j < rivinLeveys; j++) {
				
				labyrintti[i][j] = contents.charAt(rivinLeveys * i + j);
			}
		}
		ikkuna = new Editori();
		for (int i = 0; i< labyrintti.length; i++) {
			for (int j=0; j<labyrintti[i].length; j++) {
				
				switch (labyrintti[i][j]) {
					
					case 'O':
						taulukko[i][j].setIcon(new ImageIcon("kuvat/sein‰.jpg"));
						break;
					case ' ':
						taulukko[i][j].setIcon(new ImageIcon("kuvat/lattia.png"));
						break;
					case '*':
						taulukko[i][j].setIcon(new ImageIcon("kuvat/teleportti.png"));
						break;
					case '@':
						taulukko[i][j].setIcon(new ImageIcon("kuvat/maali.png"));
						break;
					case '#':
						taulukko[i][j].setIcon(new ImageIcon("kuvat/ukkeli.png"));
						break;
					case '§':
						taulukko[i][j].setIcon(new ImageIcon("kuvat/sieniMod.jpg"));
						break;
				}
			}
		}
		
	}
	/**Metodi tallentaa tehdyn labyrintin tulostamalla merkkijonotaulukon tekstitiedostoon. K‰ytt‰j‰ syˆtt‰‰
	 *tiedostonimen jonka per‰‰n lis‰t‰‰n tiedostop‰‰te .glb jotta tiedosto ei ylikirjoita mit‰‰n.
	 *Labyrinttia ei ole pakko tallentaa.*/
	public static void luoTaulukko() {
		labyrintti = new char[koko][koko];
		for (int i = 0; i<labyrintti.length;i++) {
			for(int j=0; j<labyrintti[i].length;j++){

				labyrintti[i][j] = ' ';
			}
		}
	}
	public void tallennaLabyrintti() {

		boolean nimiVaarin = true;
		boolean varmistusVaarin = true;
		String tiedosto = "";
		PrintWriter output = null;

		do {
			nimiVaarin = false;
			tiedosto = JOptionPane.showInputDialog("Anna tiedostonimi");

			try {
				File testi = new File(tiedosto);
				if (testi.exists()) {
					nimiVaarin = false;
				}
			}
			catch (Exception e) {
			}

		} while (nimiVaarin);

		try {

			output = new PrintWriter (tiedosto + ".glb");
		}
		catch (Exception error){
		}

		//Itse tulostus tapahtuu t‰ss‰
		for (i=0; i<labyrintti.length;i++) {
			if(i>0){
			output.print(System.getProperty("line.separator"));
			}
			for(j=0; j<labyrintti[i].length ;j++) {
				output.print(labyrintti[i][j]);
			}
		}

		output.close();
		JOptionPane.showMessageDialog(null, "Tallennus onnistui", "", JOptionPane.INFORMATION_MESSAGE);

	}

	/**Main-metodissa tehd‰‰n editorin JFrame ja kysyt‰‰n luotavan labyrintin koko. Koon rajoitukset johtuvat
	 *k‰ytett‰v‰n n‰yttˆtilan rajoituksista, sill‰ jos labyrintist‰ yritt‰‰ tehd‰ liian ison se ei mahdu en‰‰
	 *n‰ytˆlle. Liian pieni, niin layoutit eiv‰t toimi oikein eli osa nappuloista j‰‰ kehyksen ulkopuolella.*/
	public static void main(String [] args) {
		String tarkistus = "";
		
		Object[] napit = {"Luo uusi", "Muokkaa vanhaa"};
		int valinta = JOptionPane.showOptionDialog (ikkuna,
													"Haluatko luoda uuden labyrintin vai muokata vanhaa?",
													"",
													JOptionPane.YES_NO_OPTION,
													JOptionPane.QUESTION_MESSAGE,
													null,
													napit,
													napit[1]);
		if (valinta == JOptionPane.YES_OPTION) {
			do {
				try {
					tarkistus =  JOptionPane.showInputDialog("Anna labyrintin koko(min. 10 max. 20)");
					koko = Integer.parseInt (tarkistus);
				} catch(Exception e){
				}
				if (tarkistus == null)
					System.exit(0);
			} while (koko < 10 || koko > 20);
			luoTaulukko();
			ikkuna = new Editori();
		}
		else if(valinta == JOptionPane.NO_OPTION) {
			lataaLabyrintti();
		}
		
		ikkuna.setTitle("Editori");
    	ikkuna.setResizable(false);
    	ikkuna.setSize(50*koko,50*koko);
    	ikkuna.setLocationRelativeTo(null);
    	ikkuna.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    	ikkuna.setVisible(true);
    	
	}

}
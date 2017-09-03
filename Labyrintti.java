
import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.Arrays;

/**
 *Luokassa luodaan graafinen labyrintti-peli. Labyrintti piirret‰‰n omaan JPaneliinsa joka piirret‰‰n
 *JFrameen. Aloitettaessa uusi labyrintti avataan joko tiedostosta uusi kentt‰, tai luokkaan valmiiksi kirjoitettu esimerkki.
 *Labyrintin l‰p‰isemisen j‰lkeen pelaajan k‰ytt‰m‰ aika tallennetaan tiedostoon aikavertailua varten. Ohjelma k‰ytt‰‰ erilaisia
 *kuuntelijoita tarkistamaan tehtyj‰ siirtoja. Labyrintti k‰ynnistet‰‰n luokan Valikko kautta.
 *
 * @author Joonatan Heiskanen
 * @version 1.00 2012/4/17
 */

public class Labyrintti extends JPanel {
	/**Yhden ruudun korkeys ja leveys*/
	private static final int TILE_WIDTH=40,TILE_HEIGHT=40;
	/**K‰ytˆss‰ olevat asetukset*/
	private boolean asetus1=false,asetus2=false,asetus3=false;
	/**Ajastin*/
	private static Kellotus kello = new Kellotus();
	private static int leveys;
	/**Labyrintin rakenne, eli k‰yt‰nnˆss‰ se mink‰ pohjalta valmis labyrintti piirret‰‰n*/
	private static StringBuffer labyrintti;
	/**X-koordinaatti labyrintti "taulukossa"*/
	private static int x;
	/**Y-koordinaatti labyrintti "taulukossa"*/
	private static int y;
	/**Valitun labyrintin nimi*/
	private static String kentta;
	/**Labyrintin hahmolle varattu kuvaolio*/
	private BufferedImage hahmo = null;
	/**Labyrintin seinille varattu kuvaolio*/
	private BufferedImage seina = null;
	/**Labyrintin lattialle varattu kuvaolio*/
	private BufferedImage lattia = null;
	/**Labyrintin teleporteille varattu kuvaolio*/
	private BufferedImage teleportti = null;
	/**Labyrintin maalille varattu kuvaolio*/
	private BufferedImage maali = null;
	private BufferedImage maaliDisabled = null;
	/**Labyrintin ohjaimia vaihtavalle sienelle varattu kuvaolio*/
	private BufferedImage sieni = null;
	private BufferedImage checkpointDisabled = null;
	private BufferedImage checkpointEnabled = null;
	private static JLabel teksti = new JLabel ("");
	/**Olio joka k‰sittelee aikojen parhausj‰rjestyst‰*/
	private HighscoreManager lista;
	/**Muuttujan avulla muutetaan hahmon ohjausta. Riippuen muuttujan arvosta, hahmo liikkuu ylˆsp‰in
	 *joko painamalla vasemmalle, alas tai oikealle. 0 tarkoittaa ett‰ painamalla ylˆs p‰‰see ylˆsp‰in*/
	private int maistissa;
	private boolean checkpoint;
	private boolean checkpoint2;

	/**Alustaja jolla Labyrintti-olio luodaan. Alustajassa m‰‰ritell‰‰n eri kohteisiin k‰ytett‰v‰t
	 *kuvat, oletuslabyrintti ja luodaan n‰pp‰inkuuntelija jolla tarkkaillaan k‰ytt‰h‰n tekemi‰ siirtoja.
	 *Alustaja saa parametreinaan halutun hahmon kokonaislukuna, sek‰ halutut asetukset boolean muuttujina.
	 *@param hahmovalinta Mik‰ kolmesta hahmosta on valittu
	 *@param asetus1 Kertoo onko asetus1 p‰‰ll‰ vai poissa t‰ss‰ instanssissa
	 *@param asetus2 Kertoo onko asetus2 p‰‰ll‰ vai poissa t‰ss‰ instanssissa
	 *@param asetus3 Kertoo onko asetus3 p‰‰ll‰ vai poissa t‰ss‰ instanssissa*/
	public Labyrintti(int hahmovalinta,boolean asetus1,boolean asetus2,boolean asetus3) {
		this.asetus1=asetus1;this.asetus2=asetus2;this.asetus3=asetus3;
		Valikko.getFrame().add(teksti, BorderLayout.NORTH);
		try {
			seina = ImageIO.read(new File("kuvat/sein‰.jpg"));
			switch(hahmovalinta){
				case 1:hahmo = ImageIO.read(new File("kuvat/ukkeli.png"));break;
				case 2:hahmo = ImageIO.read(new File("kuvat/Shaolin-Dude.gif"));break;//Hahmo 2
				case 3:hahmo = ImageIO.read(new File("kuvat/Popeye.jpg"));break;//Hahmo 3
			}
			lattia = ImageIO.read(new File("kuvat/lattia.png"));
			teleportti = ImageIO.read(new File("kuvat/teleportti.png"));
			maali = ImageIO.read(new File("kuvat/maali.png"));
			maaliDisabled = ImageIO.read(new File("kuvat/maaliDisabled.png"));
			sieni = ImageIO.read(new File("kuvat/sieni.jpg"));
			checkpointDisabled = ImageIO.read(new File("kuvat/mustaNappi.png"));
			checkpointEnabled = ImageIO.read(new File("kuvat/vihreaNappi.jpg"));
			JButton lopeta = new JButton ("Sulje");
			lopeta.addMouseListener(new MouseAdapter () {
				public void mouseClicked(MouseEvent e) {
					Valikko.getFrame().dispose();
				}
		});
		Valikko.getFrame().add(lopeta, BorderLayout.SOUTH);
		} catch(IOException e) {e.printStackTrace();}

		labyrintti = new StringBuffer(
					 "----------------------\n" +
					 "O OOOO     OO OOOOOO O\n" +
					 "O     OOOO O         O\n" +
					 "OOOO         OOO OOO O\n" +
					 "O O  OOOOOOOO   §OO  O\n" +
					 "O  O O      O OOOOO OO\n" +
					 "OO O O OOOO O O    §OO\n" +
					 "O  O O O*   O O OOOOOO\n" +
					 "O O  O OOOOOOOO OO*§ O\n" +
					 "O?  O      §     OOO@O\n" +
					 "----------------------");
		//Ladataan labyrintti tiedostosta
		lataa();
		tarkistaLabyrintti();
		for (int i=0; i<labyrintti.length() ; i++) {
			if (labyrintti.charAt(i) == '#') {
				y = i/(leveys+1);
				x = i - ((leveys+1) * this.y);
				break;
			}
			x = 1;
			y = 1;
		}
		 /*Haluttu siirto v‰litet‰‰n n‰pp‰inkuuntelijalla metodille joka tarkistaa miten siirto vaikuttaa
		 *pelitilanteeseen*/
		addKeyListener(new KeyAdapter() {
             public void keyPressed(KeyEvent ke)
             {
             	 if(ke.getKeyCode() == KeyEvent.VK_ENTER) {
             	 }
                 if(ke.getKeyCode() == KeyEvent.VK_DOWN)
                    {
                   if (maistissa == 0)
                    	tarkistaSiirto(x, y+1);
                    else
                    	switch(maistissa) {
                    		case 1:
                    			tarkistaSiirto(x, y-1);
                    			break;
                    		case 2:
                    			tarkistaSiirto(x+1, y);
                    			break;
                    		case 3:
                    			tarkistaSiirto(x-1, y);
                    			break;
                    	}
                 }
                 if(ke.getKeyCode() == KeyEvent.VK_UP)
                    {
                   	if (maistissa == 0)
                    	tarkistaSiirto(x, y-1);
                    else
                    	switch(maistissa) {
                    		case 1:
                    			tarkistaSiirto(x, y+1);
                    			break;
                    		case 2:
                    			tarkistaSiirto(x-1, y);
                    			break;
                    		case 3:
                    			tarkistaSiirto(x+1, y);
                    			break;
                    	}
                 }
                 if(ke.getKeyCode() == KeyEvent.VK_LEFT)
                    {
                    if (maistissa == 0)
                    	tarkistaSiirto(x-1, y);
                    else
                    	switch(maistissa) {
                    		case 1:
                    			tarkistaSiirto(x+1, y);
                    			break;
                    		case 2:
                    			tarkistaSiirto(x, y+1);
                    			break;
                    		case 3:
                    			tarkistaSiirto(x, y-1);
                    			break;
                    	}
                 }
                 if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
                    {
                    if (maistissa == 0)
                    	tarkistaSiirto(x+1, y);
                   	else
                    	switch(maistissa) {
                    		case 1:
                    			tarkistaSiirto(x-1, y);
                    			break;
                    		case 2:
                    			tarkistaSiirto(x, y-1);
                    			break;
                    		case 3:
                    			tarkistaSiirto(x, y+1);
                    			break;
                    	}
                 }
             }

         });
	}

	public static int getTileWidth() {
		return TILE_WIDTH;
	}
	public static int getTileHeight() {
		return TILE_HEIGHT;
	}
	public static int getLeveys() {
		return leveys;
	}
	/**Paint-metodi*/
	public void paint(Graphics g) {
		super.paint(g);
		String[]taulukko = labyrintti.toString().split("\n");
		for(int i =0; i< taulukko.length; i++) {
			for (int j = 0; j < taulukko[i].length(); j++) {
				if ((taulukko[i].charAt(j) == 'O' || taulukko[i].charAt(j) == '-') && asetus1)
					g.drawImage(seina,10 + TILE_WIDTH*j,20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);

				else if ((taulukko[i].charAt(j) == ' ' || taulukko[i].charAt(j) == '#')&& asetus1)
					g.drawImage(lattia,10+TILE_WIDTH*j, 20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);

				else if (taulukko[i].charAt(j) == '*')
					g.drawImage(teleportti,10+TILE_WIDTH*j, 20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);

				else if (taulukko[i].charAt(j) == '@') {
					if(tarkistaCheckpointit())
						g.drawImage(maali,10+TILE_WIDTH*j, 20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);
					else
						g.drawImage(maaliDisabled,10+TILE_WIDTH*j, 20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);
				}
				else if (taulukko[i].charAt(j) == '§')
					g.drawImage(sieni,10+TILE_WIDTH*j, 20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);
				else if (taulukko[i].charAt(j) == '?') 
						g.drawImage(checkpointDisabled,10+TILE_WIDTH*j, 20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);
				else if (taulukko[i].charAt(j) == '!')
						g.drawImage(checkpointEnabled,10+TILE_WIDTH*j, 20+TILE_HEIGHT*i,TILE_WIDTH,TILE_HEIGHT, null);
				
			}
		}
		g.drawImage(hahmo,10+TILE_WIDTH*x,20+TILE_HEIGHT*y, TILE_WIDTH,TILE_HEIGHT,null);
	}

	/**Metodi k‰ynnist‰‰ ajastimen ja p‰ivitt‰‰ labyrintti-ikkunassa n‰kyv‰‰ ajastinta*/
	public void aloita(){
		this.requestFocusInWindow();
		kello.aloita();
		Timer timer = new Timer (1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			teksti.setText("Kentt‰: " + kentta + "    Aika: " + Long.toString(kello.getAikaSekunteina()));
			}
		});
		timer.start();
	}

	/*Tarkastetaan k‰ytt‰j‰n tekem‰ siirto. Parametrina saadaan
	 *siirron j‰lkeiset x- ja y-koordinaatit. N‰ist‰ lasketaan
	 *kyseisen merkin paikka labyrintin rakenteen m‰‰r‰‰v‰ss‰
	 *merkkijonossa, ja valitaan toiminto sen mukaan mik‰
	 *merkki tuossa paikassa on.
	 *@param x Halutun siirron x koordinaatti
	 *@param y Halutun siirron y koordinaatti*/
   	public void tarkistaSiirto(int x, int y) {

   		switch (labyrintti.charAt(x + (leveys+1)*y)) {

			/*Lattia*/
   			case ' ': case '#':
   				this.x = x;
   				this.y = y;
   				repaint();
   				break;
   			/*Maali. Maaliin tullessa ajastimet pys‰ytet‰‰n ja tallennetaan saatu aika Highscore listaan.*/
   			case '@':
   				this.x = x;
   				this.y = y;
   				repaint();
   				if(tarkistaCheckpointit()) {
	   				kello.pysayta();
	   				lista = new HighscoreManager();
					lista.lisaaAika(JOptionPane.showInputDialog("Anna nimesi"), (int)kello.getAikaSekunteina(), kentta);
	   				JOptionPane.showMessageDialog(this, "Aikasi:\n" + kello.getAikaSekunteina() + " sekuntia");
	   				Valikko.getFrame().dispose();
   				}
   				break;
   			/*Teleportti. Teleportti toimii etsim‰ll‰ labyrintti-merkkijonosta teleporttimerkit, eli '*'. T‰m‰n j‰lkeen
   			 *etsit‰‰n ensimm‰inen '*' joka ei ole siin‰ kohdassa johon juuri siirryttiin. Koska ohjelma etsii aina
   			 *ensimm‰isen '*' johon ei siirrytty, teleportit eiv‰t toimi oikein jos niit‰ laittaa kent‰lle kerralla
   			 *useamman kuin 2.*/
   			case '*':
   				this.x = x;
   				this.y = y;
   				repaint();
   				for(int i = 0; i<labyrintti.length(); i++) {
   					//y- ja x-koordinaatteja verrataan merkkijonossa olevaan merkkiin. Laskukaavat on saatu kokeilemalla.
   					if (labyrintti.charAt(i) == '*' &&(this.y != i/(leveys+1) || this.x != i - ((leveys+1) * this.y))) {

   						this.y =i/(leveys+1);
   						this.x = i - (leveys + 1) * (i/(leveys+1));
   						repaint();
   						break;
   					}
   				}
   				break;
   			case '§':
   				this.x = x;
   				this.y = y;
   				repaint();
   				int testi = 0;
   				do{
	   				testi = (int) (Math.random() * 4);
	   				System.out.println(testi);
   				}while(maistissa == testi);

	   			maistissa = testi;
   				break;
   			/*Checkpoint, eli ruutu jossa pit‰‰ k‰yd‰ ett‰ kent‰n voi l‰p‰ist‰.*/
   			case '?':
   				this.x = x;
   				this.y = y;
   				labyrintti.setCharAt(x + (leveys+1)*y,'!');
   				System.out.println(labyrintti);
   				repaint();
   				break;
   			/*Sein‰. Mukana siksi ett‰ sein‰‰n osumiselle voi lis‰t‰ toiminnallisuutta (esim. ‰‰ni‰) myˆhemmin*/
   			case 'O': break;

   		}
   	}
	
	public boolean tarkistaCheckpointit() {
		for (int i=0; i<labyrintti.length();i++) {
			if (labyrintti.charAt(i) == '?')
				return false;
		}
		return true;
	}
	/**Tarkastaa ett‰ labyrintti on rakennettu oikein, ja tarkastaa rivien leveyden.
	 *Sopivassa labyrintissa kaikki rivit ovat yht‰ leveit‰. Rivin leveytt‰ tarvitaan
	 *ohjelman muissa osissa. Tarkistus suoritetaan vertaamalla ensimm‰isen rivin
	 *leveytt‰ yksi kerrallaan jokaisen rivin leveyteen.*/
	public static void tarkistaLabyrintti() {

		int rivinPituus = 0;
		int vertailuArvo = 0;
		boolean ekaKerta = true;
		boolean tarkistusOK = true;

		for(int i = 0; i<labyrintti.length(); i++) {
			rivinPituus++;
			if (labyrintti.charAt(i) == '\n') {

				rivinPituus--;
				if(ekaKerta) {
					vertailuArvo = i;
					ekaKerta = false;
				}
				if(vertailuArvo != rivinPituus) {
					tarkistusOK = false;
					break;
				}
				rivinPituus = 0;
			}
		}
		if(tarkistusOK)
			leveys = vertailuArvo;
		else {
			JOptionPane.showMessageDialog(null, "Virheellinen labyrintti");

		}
	}
	/**Metodissa suoritetaan labyrintin lataaminen tiedostosta. Labyrintit tallennetaan tekstitiedostona,
	 *joten ladatessa tekstitiedoston sis‰ltˆ luetaan suoraan merkkijonomuuttujaan. K‰ytt‰j‰lt‰ kysyt‰‰n
	 *tiedostonimi jonka per‰‰n liitet‰‰n ohjelman k‰ytt‰m‰ .glb-tiedostop‰‰te. Jos tiedosto on olemassa
	 *se avataan, jos ei niin kysyt‰‰n uusi tiedosto. Jos k‰ytt‰j‰ painaa cancelia avataan valmis
	 *oletuslabyrintti.
	 *Vaikka metodin palautustyyppi on String, se ei palauta mit‰‰n. Palautus on vain
	 *keino pys‰ytt‰‰ metodin ajo sopivassa kohdassa.*/
	public String lataa() {
		boolean nimiVaarin = false;

		String tiedosto = "";

		do {
			nimiVaarin = true;
			tiedosto = JOptionPane.showInputDialog("Anna tiedostonimi(cancel avaa valmiin kent‰n)");
			try {

				File testi = new File(tiedosto + ".glb");
				if (testi.exists()) {
					nimiVaarin = false;
				}
				else if(tiedosto.equals("")) {
					JOptionPane.showMessageDialog(null, "Avataan valmis labyrintti");
					tiedosto = null;
				}
				else
					JOptionPane.showMessageDialog(null, "Virheellinen tiedostonimi");
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(null, "Avataan valmis labyrintti");
			}
			/*Jos k‰ytt‰j‰ painaa cancelia, m‰‰ritet‰‰n kent‰n nimeksi default ja poistutaan
			 *palauttamalla tyhj‰ merkkijono. Merkkijono kentta m‰‰r‰‰ siis vain peliss‰
			 *n‰ytett‰v‰n kent‰nnimen, eik‰ sill‰ ole mit‰‰n vaikutusta itse kentt‰‰n. Koska
			 *kent‰ksi ei aseteta mit‰‰n muuta, k‰ytet‰‰n olemassaolevaa labyrintti-merkkijonon
			 *sis‰ltˆ‰.*/
			if (tiedosto == null) {
				kentta = "default";
				return "";
			}

		} while (nimiVaarin);
		//Asetetaan kent‰nnimeksi avattavan tiedoston nimi.
		kentta=tiedosto;
		File file = new File(tiedosto + ".glb");
		StringBuffer contents = new StringBuffer();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;

			/*Toistetaan lukua kunnes luettava rivi on tyhj‰. Koska labyrintin tyhj‰t kohdat
			 *ovat v‰lilyˆntej‰ luku ei katkea vaikka jollain rivill‰ olisikin pelkk‰‰ lattiaa*/
			while ((text = reader.readLine()) != null) {
			contents.append(text).append(System.getProperty("line.separator"));
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
		labyrintti = contents;
		return "";
	}
}
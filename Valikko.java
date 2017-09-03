
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Valikko extends JFrame implements ActionListener{
	/** Valittu hahmo (oletus 1) */
	private int hahmo=1;
	/** Asetusvalikon checkboxien boolean arvot */
	private boolean asetus1=true,asetus2=false,asetus3=false;
	/** Valikon JFrame olio */
	private static JFrame frame;
	/** JButton oliot */
	private JButton uusipeli,editori,lopeta,highscore,asetukset,ohje;
	/** Ohjeikkunan olio */
	private Ohje ohjeikkuna;
	/** Asetusikkunan olio */
	private Asetus asetusikkuna;
	private Labyrintti plaa;
	/**Konstruktori*/
	public Valikko() {
		uusipeli=new JButton("Uusi Peli");
		editori=new JButton("Kentt‰editori");
		highscore=new JButton("High Score");
		asetukset=new JButton("Asetukset");
		ohje=new JButton("Ohje");
		lopeta=new JButton("Lopeta");
		uusipeli.addActionListener(this);
		ohje.addActionListener(this);
		asetukset.addActionListener(this);
		highscore.addActionListener(this);
		editori.addActionListener(this);
		lopeta.addActionListener(this);
		setLayout(new GridLayout(7, 1, 1, 1));
		JPanel title = new JPanel(){
			public void paint(Graphics g){
				g.setFont(new java.awt.Font("Arial",Font.BOLD,35));
				g.setColor(java.awt.Color.LIGHT_GRAY);
				g.drawString("LABYRINTTI",46,44);
				g.setColor(java.awt.Color.BLACK);
				g.drawString("LABYRINTTI",50,40);
			}
		};
			add(title);
	   		add(uusipeli);
	    	add(editori);
	    	add(highscore);
	    	add(asetukset);
	    	add(ohje);
			add(lopeta);
    }
	public static JFrame getFrame() {
		return frame;
	}
	/**Main metodi*/
    public static void main(String[] args) {
    	 try {
    	      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	    } catch(Exception e) {e.printStackTrace();}
    	Valikko ikkuna = new Valikko();
		ikkuna.setResizable(false);
    	ikkuna.setTitle("Valikko");
    	ikkuna.setSize(300,450);
    	ikkuna.setLocationRelativeTo(null);
    	ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	ikkuna.setVisible(true);

    }
    /**Kuuntelijametodi nappuloille*/
    public void actionPerformed(ActionEvent e) {
			if (e.getSource() == uusipeli){
				frame = new JFrame("Labyrintti");
				frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
				plaa = new Labyrintti(hahmo,asetus1,asetus2,asetus3);
        		frame.setSize(Labyrintti.getTileWidth()*Labyrintti.getLeveys(), Labyrintti.getTileHeight()*Labyrintti.getLeveys()+100);
        		frame.setVisible(true);
        		frame.setLocationRelativeTo(null);
        		frame.setResizable(true);
       			frame.add(plaa);
       			plaa.aloita();
			}
			else if(e.getSource()==editori){
				Editori.main(null);
			}
			else if(e.getSource()==highscore){
				HighscoreManager lista = new HighscoreManager();
				boolean nimiVaarin = false;
				String tiedosto = "";
				do {
				tiedosto = JOptionPane.showInputDialog("Anna tiedostonimi");
				nimiVaarin = true;
				try {
					File testi = new File(tiedosto);
					if (testi.exists()) {
						JOptionPane.showMessageDialog(null, lista.getAikaLista(tiedosto));
						nimiVaarin = false;
					}
					else
						JOptionPane.showMessageDialog(null, "Annettua tiedostoa ei lˆytynyt");
					}
				catch (Exception ex) {
					nimiVaarin = false;
				}
				} while (nimiVaarin);
			}
			else if(e.getSource() == asetukset){
				asetusikkuna = new Asetus();
				asetusikkuna.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
				asetusikkuna.setSize(300, 300);
				asetusikkuna.setVisible(true);
				asetusikkuna.setLocationRelativeTo(null);
				asetusikkuna.setVisible(true);
				asetusikkuna.setResizable(false);
			}
			else if (e.getSource() == ohje){
				ohjeikkuna = new Ohje();
				ohjeikkuna.setDefaultCloseOperation( JFrame.DO_NOTHING_ON_CLOSE );
				ohjeikkuna.setSize(530, 300);
				ohjeikkuna.setVisible(true);
				ohjeikkuna.setLocationRelativeTo(null);
				ohjeikkuna.setVisible(true);
				ohjeikkuna.setResizable(false);
			}
			else if(e.getSource()==lopeta)
				System.exit(0);
		}
    /**Sis‰luokka ohjeikkunalle*/
    public class Ohje extends JFrame{
    	public Ohje(){
    		JTextArea label = new JTextArea("Liikkuminen: Nuolin‰pp‰imill‰" + System.getProperty("line.separator") +
    			"Teht‰v‰n‰si on p‰‰st‰ ruutulipulle mahdollisimman nopeasti" + System.getProperty("line.separator") +
    			"V‰rikk‰‰t ympyr‰t ovat teleportteja. Astuessasi yhdest‰ sis‰‰n,"+System.getProperty("line.separator")+"tulet toisesta ulos." + System.getProperty("line.separator") +
    				System.getProperty("line.separator") + "Tekij‰t: Jaakko Roima, Joonatan Heiskanen, Ilmari Anturaniemi");
    		label.setBackground(new Color(240,240,240));
    		label.setEditable(false);
    		JButton pois = new JButton("Sulje");
    		setLayout(new BorderLayout());
    		add(label,BorderLayout.NORTH);
    		add(pois,BorderLayout.SOUTH);
    		pois.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				ohjeikkuna.dispose();
    			}
    		});
    	}
    }
  /**Sis‰luokka asetusikkunalle*/
  public class Asetus extends JFrame{
	  private int hahmovalinta=hahmo;
	  private JCheckBox check1;
	  private JCheckBox check2;
	  private JCheckBox check3;
      public Asetus(){
    		JPanel paneeli = new JPanel();
    		paneeli.setLayout(null);
    		JRadioButton hahmo1,hahmo2,hahmo3;
    		//
    		hahmo1=new JRadioButton("Hahmo 1",false);
    		hahmo2=new JRadioButton("Hahmo 2",false);
    		hahmo3=new JRadioButton("Hahmo 3",false);
    		//Muistaa edellisen valinnan
    		switch(hahmovalinta){
    		case 1:hahmo1=new JRadioButton("Hahmo 1",true);break;
    		case 2:hahmo2=new JRadioButton("Hahmo 2",true);break;
    		case 3:hahmo3=new JRadioButton("Hahmo 3",true);break;
    		}
    		ButtonGroup bhahmovalinta = new ButtonGroup();
    		bhahmovalinta.add(hahmo1);
    		bhahmovalinta.add(hahmo2);
    		bhahmovalinta.add(hahmo3);
    		//
    		hahmo1.setBounds(100,130,100,20);
    		paneeli.add(hahmo1);
    		//
    		hahmo2.setBounds(100,150,100,20);
    		paneeli.add(hahmo2);
    		//
    		hahmo3.setBounds(100,170,100,20);
    		paneeli.add(hahmo3);
    		//
    		hahmo1.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {hahmovalinta=1;}});
    		hahmo2.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {hahmovalinta=2;}});
    		hahmo3.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {hahmovalinta=3;}});
    		//
    		check1 = new JCheckBox("N‰kyv‰t sein‰t",asetus1);
    		check2 = new JCheckBox("Asetus 2",asetus2);
    		check3 = new JCheckBox("Asetus 3",asetus3);
    		JButton pois = new JButton("Sulje");
    		//
    		check1.setBounds(100,30,150,20);
    		paneeli.add(check1);
    		//
    		check2.setBounds(100,50,100,20);
    		//paneeli.add(check2);
    		//
    		check3.setBounds(100,70,100,20);
    		//paneeli.add(check3);
    		//
    		paneeli.add(pois);
    		pois.setBounds(87,230,125,40);
    		//
    		add(paneeli);
    		pois.addActionListener(new ActionListener(){
    			public void actionPerformed(ActionEvent e){
    				//Tallennetaan asetukset ja suljetaan
    				hahmo=hahmovalinta;
    				asetus1=check1.isSelected();
    				asetus2=check2.isSelected();
    				asetus3=check3.isSelected();
    				asetusikkuna.dispose();
    			}
    		});
    	}
    }
}
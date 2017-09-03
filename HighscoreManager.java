/**
 * @(#)HighscoreManager.java
 *
 * Lukee, syˆtt‰‰ ja j‰rjest‰‰ ajat tiedostoissa
 * @author
 * @version 1.00 2012/5/1
 */


import java.util.*;
import java.io.*;

/** alustetaan arraylist, luettava/kirjoitettava tiedosto sek‰ luku- ja kirjoitus
 */
public class HighscoreManager {
	// arraylist pistelistalle
    private ArrayList pistel = new ArrayList<Piste>();
    // tiedosto, jonne kirjoitetaan dataa ja mist‰ sit‰ luetaan
    private static String tiedosto = "";
    // kirjoittaja ja lukija
    ObjectOutputStream output = null;
    ObjectInputStream input = null;

	/** kutsutaan eri metodeja, joiden avulla lista saadaan tulostettua
	 *	@return pistel palauttaa j‰rjestetyn listan
	 */
    public ArrayList<Piste> getScores() {
        lataaTiedosto();
        sort();
        return pistel;
    }
	/** aikojen vertailua varten k‰ytetty metodi
	 */
    private void sort() {
        Vertaaja vertailija = new Vertaaja(); //luodaan Vertaaja-luokasta ilmentym‰
        Collections.sort(pistel, vertailija); //j‰rjest‰‰ ajat pistelistan ja vertailija-muuttujan avulla
	}

	/** metodi, joka kutsuu muita metodeja
	 *	k‰ytet‰‰n lis‰‰m‰‰n dataa tiedostoon
	 *	@param nimi pelaajan nimi
	 *	@param aika aika, joka peliss‰ kesti
	 *	@param tiedosto tiedostonnimi, johon data lis‰t‰‰n
	 */
	public void lisaaAika(String nimi, int aika, String tiedosto) {
		this.tiedosto = tiedosto;
        lataaTiedosto();
		tarkistaTiedosto(aika);
        pistel.add(new Piste(nimi, aika));	//lis‰t‰‰n uusi rivi arraylistiin, jossa on pelaajan nimi ja aika
        paivitaTiedosto();
	}
	/**	lataa tiedoston arraylistiin
	 */
	public void lataaTiedosto() {

        try {
            input = new ObjectInputStream(new FileInputStream(tiedosto));
            pistel = (ArrayList<Piste>) input.readObject();
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
        }
	}
	/**	kirjoittaa tiedostoon uuden rivin dataa
	 */
	public void paivitaTiedosto() {
        try {
            output = new ObjectOutputStream(new FileOutputStream(tiedosto));
            output.writeObject(pistel);
        } catch (IOException e) {
        }
	}
	/**	tarkistetaan tiedostosta, onko siell‰ parempaa aikaa
	 *	k‰ytet‰‰n m‰‰rittelem‰‰n, onko uusi aika highscore-listan arvoinen
	 *	@param aika pelikertaan k‰ytetty aika
	 */
	public void tarkistaTiedosto(int aika) {
		ArrayList<Piste> pistel;
		pistel = getScores();	//haetaan pisteet pistelistaan
		int vrtaika = 2147483647;	//luodaan vertailuaika
		int x = pistel.size();		//luodaaan x-muuttuja, joka on listan pituinen
		if (x < 5) { x = 0; }	//jos x on alle 5, se saa arvon 0
		else if (x > 5) { vrtaika = pistel.get(4).getAika(); }	//jos x on yli 5, vertailuajaksi tulee listan 5. alkio
		//tulostetaan voivottelut, jos pelaajan uusi aika on pienempi kuin vertailuaika
		if (aika > vrtaika){
			System.out.println("voi voi " + vrtaika);
		}

	}
	/**	palauttaa listan stringin‰
	 *	@param tiedostonimi tiedoston nimi, mist‰ data luetan
	 *	@return aikaString	palauttaa listan stringin‰
	 */
	public String getAikaLista(String tiedostonimi) {
        tiedosto=tiedostonimi;
        String aikaString = "";	//alustetaan aikaString-muuttuja
		ArrayList<Piste> pistel;
        pistel = getScores();	//ladataan tiedosto pistelistaan

        int i = 0;
        int x = pistel.size();
        if (x > 5) {		//m‰‰ritell‰‰n, kuinka monta alkiota listasta n‰ytet‰‰n
            x = 5;
        }
        // muotoillaan lista j‰rkev‰‰n muotoon
        while (i < x) {
            aikaString = aikaString + (i + 1) + ". " + pistel.get(i).getNimi() + " " + pistel.get(i).getAika() + System.getProperty("line.separator");
            i++;
        }
        //palautetaan saatu string
        return aikaString;
	}
	/** sis‰luokka, jota k‰ytet‰‰n arraylistin j‰rjest‰miseen
	 */
	static class Vertaaja implements Comparator<Piste> {
        /** m‰‰ritell‰‰n vertailuarvot
         * @param piste1 ensimm‰inen luku
         * @param piste2 toinen luku
         * @return palautetaan arvoja vertailun perusteella
         */
        public int compare(Piste piste1, Piste piste2) {
			//haetaan ajat muuttujille p1 ja p2
            int p1 = piste1.getAika();
            int p2 = piste2.getAika();

            if (p1 < p2) {return -1;}	//p1 pienempi
            else if (p1 > p2) {return 1;}	//p1 isompi
            else {return 0;}	//luvut yht‰suuret
        }
}
	/** sis‰luokka, josta saadaan haettua pelaajan nimi ja aika
	 */
	static class Piste implements Serializable {
	    //alustetaan muuttujat
	    private int piste3;
	    private String nimi;
		/** @return piste3 palautetaan aika
		 */
	    public int getAika() {
	        return piste3;
	    }
		/** @return nimi palautetaan pelaajan nimi
		 */
	    public String getNimi() {
	        return nimi;
	    }
		/** alustaja
		 *	@param nimi pelaajan nimi
		 *	@param piste3 pelaajan aika
		 */
	    public Piste(String nimi, int piste3) {
	        this.piste3 = piste3;
	        this.nimi = nimi;
	    }
	}
}

/**
 *Luokasta luotua oliota voi k‰ytt‰‰ sekuntikellona. Sill‰ on metodit kellon k‰ynnist‰miseen
 *ja pys‰ytt‰miseen, kuin myˆs ajan "katsomiseen" niin millisekunteina kuin sekunteinakin.
 *
 * @author Joonatan Heiskanen
 * @version 1.00 2012/4/3
 */

public class Kellotus {

    private long aloitus = 0;
    private long lopetus = 0;
    private boolean kaynnissa = false;

	/**K‰ynnist‰‰ kellon*/
    public void aloita() {
        this.aloitus = System.currentTimeMillis();
        this.kaynnissa = true;
    }

	/**Pys‰ytt‰‰ kellon*/
    public void pysayta() {
        this.lopetus = System.currentTimeMillis();
        this.kaynnissa = false;
    }

	/**Palauttaa ajan millisekunteina
	 *@return aika Kulunut aika millisekunteina*/
    public long getAika() {
        long aika;
        if (kaynnissa) {
             aika = (System.currentTimeMillis() - aloitus);
        }
        else {
            aika = (lopetus - aloitus);
        }
        return aika;
    }

	/**Palauttaa ajan sekunteina
	 *@return aika Kulunut aika sekunteina*/
    public long getAikaSekunteina() {
        long aika;
        if (kaynnissa) {
            aika = ((System.currentTimeMillis() - aloitus) / 1000);
        }
        else {
            aika = ((lopetus - aloitus) / 1000);
        }
        return aika;
    }
}
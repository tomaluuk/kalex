package kalex;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Luokka Oluiden tietojen hallintaan
 * @author Topi Luukkanen & Veikko Haakana
 * @version 26.3.2018
 *
 */
public class Olut {
	private int    olutId;
	private String olutNimi;
	private String panimo;
	private String maa;
	private String tyyppi;
	private double prosentit;
	private int    ebu;

	private static int seuraavaId = 1;	

	/**
	 * Rekisteröi oluen tietokantaan
	 * @return oluelle osoitettu id-numeron
	 * @example
	 * <pre name="test">
	 * Olut kalja1 = new Olut();
	 * kalja1.getId() === 0;
	 * kalja1.rekisteroi();
	 * Olut kalja2 = new Olut();
	 * kalja2.rekisteroi();
	 * int n1 = kalja1.getId();
	 * int n2 = kalja2.getId();
	 * n1 === n2-1;
	 */
	public int rekisteroi() {
		this.olutId = seuraavaId; 
		seuraavaId++;
		return this.olutId;
	}
	
	
	/**
	 * @return id-numero
	 */
	public int getId() {
		return this.olutId;
	}
	
	
	/**
	 * @return oluen nimi
	 */
	public String getNimi() {
	    return this.olutNimi;
	}
	
	
	/**
     * @return oluen panimo
     */
    public String getPanimo() {
        return this.panimo;
    }
    
    
    /**
     * @return oluen valmistusmaa
     */
    public String getMaa() {
        return this.maa;
    }
    
    
    /**
     * @return oluen tyyppi
     */
    public String getTyyppi() {
        return this.tyyppi;
    }
	
    
    /**
     * @return oluen alkoholiprosentti
     */
    public double getProsentit() {
        return this.prosentit;
    }
	
    
    /**
     * @return oluen EBU-arvo
     */
    public int getEbu() {
        return this.ebu;
    }
    
    
    /**
     * @param nimi oluen nimi
     */
    public void setNimi(String nimi) {
        this.olutNimi = nimi;
    }
    
    
    /**
     * @param panimo oluen panimo
     * 
     */
    public void setPanimo(String panimo) {
        this.panimo = panimo;
    }
    
    
    /**
     * @param maa oluen valmistusmaa
     * 
     */
    public void setMaa(String maa) {
        this.maa = maa;
    }
    
    
    /**
     * @param tyyppi oluen tyyppi
     */
    public void setTyyppi(String tyyppi) {
        this.tyyppi = tyyppi;
    }
    
    
    /**
     * @param prosentit oluen alkoholiprosentti
     * @return null, jos oikein muotoiltu. 
     *         merkkijono, jos virheellinen
     */
    public String setProsentit(double prosentit) {
        if (prosentit >= 0 && prosentit <= 100) {
            this.prosentit = prosentit;
            return "";
        }
        return "Prosenttiluvun tulee olla välillä 0 – 100. Käytä desimaalina pistettä.\n";
    }
    
    
    /**
     * @param ebu oluen EBU-arvo
     * @return null, jos oikein muotoiltu. 
     *         merkkijono, jos virheellinen
     */
    public String setEbu(int ebu) {
        if (ebu >= 0 && ebu < 150) {
            this.ebu = ebu;
            return "";
        }
        return "EBU-arvon tulee olla välillä 0 – 150.\n";
    }
    
    
	/**
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println("Id:" + olutId);
		out.println("Nimi:" + olutNimi);
		out.println("Panimo:" + panimo);
		out.println("Maa:" + maa);
		out.println("Tyyppi:" + tyyppi);
		out.println("Prosentit:" + prosentit);
		out.println("Ebu:" + ebu);
	}
	
	
	/**
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	
	private void setId(int nr) {
		olutId = nr;
		if (olutId >= seuraavaId) seuraavaId = olutId + 1;
	}
	
	
	/**
	 * Palauttaa Oluen tiedot merkkijonona jonka voi tallentaa tiedostoon
	 * @return olut merkkijonona tolpilla eroteltuna
	 * @example
	 * <pre name="test">
	 * 		Olut olut = new Olut();
	 * 		Olut.parse("	10	|	Karjala	|	Hartwall	|	Suomi	|	Lager	|	4,7%	|	12 ");
	 * 		olut.toString()		=== "10|Karjala|Hartwall|Suomi|Lager|4,7%|12";	
	 */
	@Override 
	public String toString( ) {
		return "" + 
			   getId() + "|" + 
			   olutNimi +  "|" + 
			   panimo + "|" + 
			   maa + "|" + 
			   tyyppi + "|" + 
			   prosentit + "|" + 
			   ebu;
	}
	
	
	/**
	 * Selvittää oluen tiedot tolpalla erotellust merkkijonosta
	 * Huolehtii, että seuraavaNro on suurempi kuin tuleva Id.
	 * @param rivi josta jäsenen tiedot otetaan
	 * 
	 * @example
	 * <pre name="test">
	 * Olut olut = new Olut();
	 * olut.parse("	10	|	Karjala	|	Hartwall	|	Suomi	|	Lager	|	4,7	|	12")
	 * olut.getId() === 10;
	 * olut.toString()	=== "10|Karjala|Hartwall|Suomi|Lager|4,7|12";
	 *
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		setId(Mjonot.erota(sb, '|', getId()));
		olutNimi = (Mjonot.erota(sb, '|', olutNimi));
		panimo = (Mjonot.erota(sb, '|', panimo));
		maa = (Mjonot.erota(sb, '|', maa));
		tyyppi = (Mjonot.erota(sb, '|', tyyppi));
		prosentit = (Mjonot.erota(sb,  '|',  prosentit));
		ebu = (Mjonot.erota(sb, '|', ebu));
	}
	
	
	@Override
	public int hashCode() {
	    return olutId;
	}
	
	
	@Override
	public boolean equals(Object olut) {
		if ( olut == null ) return false;
		return this.toString().equals(olut.toString());
	}
	
	
	/**
	 * Täyttää tiedot testioluelle
	 */
	public void karjalatKitusiin() {
		this.olutNimi = "Karjala";
		this.panimo = "Hartwall";
		this.maa = "Suomi";
		this.tyyppi = "Lager";
		this.prosentit = 4.7;
		this.ebu = 12;
	}


    /**
	 * Testiohjelma oluelle
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Olut karjala = new Olut();
		karjala.rekisteroi();
		karjala.karjalatKitusiin();
		karjala.tulosta(System.out);
	}
}

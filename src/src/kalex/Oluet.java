package kalex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import fi.jyu.mit.ohj2.WildChars;

/** 
 * @author Topi Luukkanen & Veikko Haakana
 * @version 26.3.2018
 * 
 * Kalexin oluiden käsittelyyn luotu luokka, joka osaa mm. lisätä uuden oluet
 */
public class Oluet {

	private static final int MAX_LKM = 2;
	private int                  lkm = 0;
	private static String   tiedosto = "oluet.dat";
	private Olut            alkiot[] = new Olut[MAX_LKM];
	
    //Tarkistaa onko tiedostossa tallentamatta olevia tietoja
	private boolean            muutettu = false;


	/**
	 * Lisää oluen tietorakenteeseen
	 * @param olut lisättävä olut
	 * @example
     * <pre name="test">
     * Oluet oluet = new Oluet();
     * Olut karjala1 = new Olut(), karjala2 = new Olut();
     * oluet.getLkm() === 0;
     * oluet.lisaa(karjala1); oluet.getLkm() === 1;
     * oluet.lisaa(karjala2); oluet.getLkm() === 2;
     * oluet.getAlkiot() === 2;
     * oluet.lisaa(karjala1); oluet.getLkm() === 3;
     * oluet.getAlkiot() === 4;
     * oluet.tuoOlut(0) === karjala1;
     * oluet.tuoOlut(1) === karjala2;
     * oluet.tuoOlut(2) === karjala1;
     * oluet.tuoOlut(1) == karjala1 === false;
     * oluet.tuoOlut(1) == karjala2 === true; 
     * oluet.lisaa(karjala1); oluet.getLkm() === 4;
     * oluet.lisaa(karjala1); oluet.getLkm() === 5;
     * oluet.lisaa(karjala1); oluet.getLkm() === 6;
     * oluet.lisaa(karjala1); oluet.getLkm() === 7;
     * </pre>
	 */
	public void lisaa(Olut olut) {
		if (lkm >= alkiot.length) {
		    Olut[] temp_alkiot = alkiot.clone();
            alkiot = new Olut[temp_alkiot.length + MAX_LKM];
            System.arraycopy(temp_alkiot, 0, alkiot, 0, temp_alkiot.length);
		}
		
    		alkiot[lkm] = olut;
    		lkm++;
    		muutettu = true;
	}

	
	/**
	 * Palauttaa listaan kirjattujen oluiden lukumäärän
	 * @return oluiden lukumäärä
	 */
	public int getLkm() {
		return lkm;
	}
	
	
	/**
	 * Palauttaa alkioiden lukumäärän
	 * @return alkioiden lkm
	 */
	public int getAlkiot() {
	    return alkiot.length;
	}

	
	/**
	 * Tuo oluen tiedot olion muodossa
	 * @param i oluen indeksi listassa
	 * @return i:nnes Olut-olio
	 * @throws IndexOutOfBoundsException Jos syötetään virheellinen indeksi 
	 */
	public Olut tuoOlut(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Virheellinen indeksi: " + i);
		return alkiot[i];
	}	

	
	/**
	 * Hakee oluen id:llä
	 * @param id id
	 * @return Olut
	 */
	public Olut getOlutById(int id) {
	    for (int i = 0; i < lkm; i++) {
	        if (alkiot[i].getId() == id) {
	            return alkiot[i];
	        }
	    }
	    return null;
	}
	
	
	/**
	 * Vie oluen tietorakenteeseen
	 * @throws SailoException Tallennuksen yhteydessä tapahtuvan virheen varalle
	 */
	public void vieTiedostoon() throws SailoException {
		throw new SailoException("Tiedostoon tallentaminen ei toimi vielä " + tiedosto);
	}

	
	/**
	 * Lukee oluet tiedostosta tietorakenteeseen
	 * @throws SailoException Tiedoston lukemisen yhteydessä tapahtuvan virheen varalle 
	 */
	public void lueTiedostosta() throws SailoException {
		try (BufferedReader br = new BufferedReader(new FileReader(tiedosto))) {
			String rivi;
			
			while ((rivi = br.readLine()) != null) {
				rivi = rivi.trim();
				if (rivi.charAt(0) != ';') {
				    Olut olut = new Olut();
				    olut.parse(rivi); 
				    lisaa(olut);
				}
			}
			muutettu = false;
		} catch (FileNotFoundException e ) {
			throw new SailoException("Tiedostoa " + tiedosto + " ei voi avata!");
		} catch (IOException e ) {
			throw new SailoException("Ongelmia tiedostoa luettaessa: " + e.getMessage());
		}
	}
	
	
	/**
	 * Tiedoston tallentaminen
	 * @throws SailoException
	public void tallenna() throws SailoException {
		lueTiedostosta(getTiedosto());
	}
	 */
	
	public void tallenna() throws SailoException {
		if (!muutettu) return;
		
        try ( PrintWriter writer = new PrintWriter(new FileWriter(tiedosto)) ) {
            writer.print(";Olut");
            writer.print("\n" + ";oid|nimi|panimo|maa|tyyppi|prosentit|ebu");
            for (int i = 0; i < lkm; i++) {
                if(this.alkiot[i] != null) writer.print("\n" + this.alkiot[i].toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedostoa " + tiedosto + " ei voida aukaista");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + tiedosto + " kirjoittamisessa ongelmia");
        }
        
		muutettu = false;
	}
	
	
	/**
	 *  Palauttaa tiedoston nimen, jota käytetään tallennukseen
	 * @return tallennustiedon nimi
	 */
	public String getTiedostonNimi() {
		return tiedosto;
	}
	
	
	/**
	 * 
	 * @param hakuehto  haettavan merkkijono
	 * @return collection kaikista hakuehtoa vastaavista oluista
	 */
	public List<Olut> etsi(String hakuehto) {
	    String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        List<Olut> loytyneet = new ArrayList<Olut>(); 
        for (int i = 0; i < lkm; i++) { 
            if (WildChars.onkoSamat(alkiot[i].getNimi(), ehto)) loytyneet.add(alkiot[i]);   
        } 
          
        return loytyneet;
	}
	
	
    /**
     * Tarkastaa, löytyykö olut jo tietorakenteesta
     * @param olutData oluen tiedot
     * @return -1 jos ei ole, Oluen id jos löytyy
     */
    public int onkoOlemassa(String[] olutData) {
        for (int i = 0; i < alkiot.length; i++) {
            if (alkiot[i] != null &&
                alkiot[i].getNimi().equalsIgnoreCase(olutData[0]) &&
                alkiot[i].getPanimo().equalsIgnoreCase(olutData[1]) &&
                alkiot[i].getMaa().equalsIgnoreCase(olutData[2]) &&
                alkiot[i].getTyyppi().equalsIgnoreCase(olutData[3]) &&
                alkiot[i].getProsentit() == Double.parseDouble(olutData[4])) { 
                return alkiot[i].getId();
            }
        }
        
        return -1;
    }
    
	
	/**
	 * Testipääohjelma Oluet-luokalle	 
	 * * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Oluet oluet = new Oluet();
		Olut karjala = new Olut(), karjala2 = new Olut();
		karjala.rekisteroi();
		karjala.karjalatKitusiin();
		karjala2.rekisteroi();
		karjala2.karjalatKitusiin();
		oluet.lisaa(karjala);
        oluet.lisaa(karjala2);

        System.out.println("----------Testi----------");

        for (int i = 0; i < oluet.getLkm(); i++) {
        	Olut olut = oluet.tuoOlut(i);
        	System.out.println("Oluen indeksi: " + i);
        	olut.tulosta(System.out);
        	System.out.println();
        }

	}

}

package kalex;

import java.util.ArrayList;
import java.util.List;

import kalex.Kayttaja;
import kalex.Kayttajat;
import kalex.Olut;
import kalex.Oluet;
import kalex.KayttajanOlut;
import kalex.KayttajanOluet;
/**
 * @author Topi Luukkanen
 * @version 9.3.2018
 *
 */
public class Kalex {

	private Kayttajat kayttajat = new Kayttajat();
	private Oluet oluet = new Oluet();
	private KayttajanOluet kayttajanOluet = new KayttajanOluet();

	
    /**
     * @return Kantaan tallennettujen käyttäjien lukumäärä
     */
    public int getKayttajaLkm() {
        return kayttajat.getLkm();
    }

    
    /**
     * @return Kantaan tallennettujen oluiden lukumäärä
     */
    public int getOlutLkm() {
        return oluet.getLkm();
    }

    
    /**
     * 
     * @param kayttaja Lisättävä käyttäjä
     * @throws SailoException Jos käyttäjän lisäyksessä ongelmia
     */

    public void lisaa(Kayttaja kayttaja) throws SailoException {
    	kayttajat.lisaa(kayttaja);
    }
    
    
    /**
     * @param olut Lisättävä olut
     * @throws SailoException Jos oluen lisäyksessä ongelmia
     */
    public void lisaa(Olut olut) throws SailoException {
        oluet.lisaa(olut);
    }

    
    /**
     * @param kOlut Kaytta<janOlut -olio
     * @throws SailoException Jos kirjauksen lisäyksessä ongelmia
     */
    public void lisaa(KayttajanOlut kOlut) throws SailoException {
        kayttajanOluet.lisaa(kOlut);
    }
    
    
    /**
     * Palauttaa "taulukossa" hakuehtoon vastaavien oluiden viitteet
     * @param hakuehto hakuehto
     * @return tietorakenteen löytyneistä oluista
     * @throws SailoException jos jotakin menee väärin
     */
    public List<Olut> etsi(String hakuehto) throws SailoException {
        return oluet.etsi(hakuehto);
    }
     
    
    /**
     * Lukee kaikki tiedostoon tallennetut käyttäjät 
     * @throws SailoException Jos lukemisen yhteydessä tapahtuu virhe
     */
    public void lueTiedostot() throws SailoException {
    	kayttajat = new Kayttajat();    	
    	kayttajat.lueTiedostosta();

        oluet = new Oluet(); 
        oluet.lueTiedostosta();
 
        kayttajanOluet = new KayttajanOluet(); 
        kayttajanOluet.lueTiedostosta();
    }
    
    
    /**
     * Lukee käyttäjän oluet listaan
     * @param aktiivinenKayttaja valittu käyttäjä
     * @return Käyttäjän kaikki kirjatut olut Array-listassa
     * @throws SailoException Jos lukemisen yhteydessä tapahtuu virhe
     */
    public ArrayList<KayttajanOlut> haeKayttajanOluet(int aktiivinenKayttaja) throws SailoException{
        return kayttajanOluet.haeKaikkiKayttajalta(aktiivinenKayttaja);
    }
    
    
    /**
     * @param i oluen indeksi
     * @return viite i:teen oluseen
     * @throws IndexOutOfBoundsException jos i on väärin
     */
    public Olut tuoOlut(int i) throws IndexOutOfBoundsException {
        return oluet.tuoOlut(i);
    }
    
    
    /**
     * Hakee oluen id:llä
     * @param id id
     * @return Olut
     */
    public Olut getOlutById(int id) {
        return oluet.getOlutById(id);
    }
    
    
    /**
     * 
     * @param i Käyttäjän indeksi
     * @return Käyttäjä
     * @throws IndexOutOfBoundsException Jos indeksiä ei ole taulukossa
     */
    public Kayttaja tuoKayttaja(int i) throws IndexOutOfBoundsException {
        return kayttajat.tuoKayttaja(i);
        
    }
    
    
    /**
     * @return palauttaa viitteen kayttajanOluet-tietorakenteeseen
     */
    public KayttajanOluet getKayttajanOluet() {
        return kayttajanOluet;
    }
    
    
    /**
     * @param aktiivinenKayttaja kirjaukseen liitetty käyttäjä
     * @param olutId kirjaukseen liitetty olut 
     * @return Käyttäjän kirjaus
     */
    public KayttajanOlut getKirjaus(int aktiivinenKayttaja, int olutId) {
        return kayttajanOluet.getKirjaus(aktiivinenKayttaja, olutId);
    }
    
    
    /**
     * Hakee kirjauksen olut-id:n
     * @param i indeksi kayttajienOluet.dat:ista
     * @return kirjauksen olut-id, arvosana ja kommentti
    public String getKirjauksenTiedot(int i) {
        return kayttajanOluet.getKirjauksenTiedot(i);
    }
     */
    
    
    /**
     * Hakee kirjauksen käyttäjä-id:n
     * @param i indeksi kayttajienOluet.dat:ista
     * @return id
     */
    public int getKirjauksenKayttajaId(int i) {
        return kayttajanOluet.getKayttajaId(i);
    }
    
    
    /**
     * Vie päivitykset tietorakenteeseen
     * @param kayttajaId käyttäjän id
     * @param olutId oluen id
     * @param arvosana kirjauksen päivitetty arvosana 
     * @param kommentti kirjauksen päivitetty kommentti 
     * @return null, jos oikein muotoiltu. 
     *         merkkijono, jos virheellinen
     */
    public String paivitaKirjaus(int kayttajaId, int olutId, Double arvosana, String kommentti) {
        return kayttajanOluet.paivitaKirjaus(kayttajaId, olutId, arvosana, kommentti);
    }
    
    /**
     * @throws SailoException Jos tallennettaessa tapahtuu virhe
     */
    public void tallenna() throws SailoException{
        boolean virhe = false;
        String virheMsg = "";
        try {
            kayttajat.tallenna();
        } catch ( SailoException e) {
            virheMsg = e.getMessage();
            virhe = true;
        }
        
        try {
            oluet.tallenna();
        } catch (SailoException e){
            virheMsg += e.getMessage();
            virhe = true;
        }
        
       
        try {
            kayttajanOluet.tallenna();
        } catch (SailoException e){
            virheMsg += e.getMessage();
            virhe = true;
        }
        
        if (virhe) throw new SailoException(virheMsg);
    }
    
    
    /**
     * Tarkastaa löytyykö syötettyjä tietoja vastaava olut
     * @param olutData oluen tiedot
     * @return -1 jos olutta ei vielä ole kirjattu, oluen id jos olut on jo kirjattu
     */
    public int onkoOlemassa(String[] olutData) {
        return oluet.onkoOlemassa(olutData);
    }

    
    /**
     * Poistaa käyttäjältä valitun oluen
     * @param kOlut käyttäjän olut
     * @param kId aktiivisen käyttäjän id
     * @return merkkijono, jossa tekstiä jos virhe on tapahtunut
     * @throws SailoException jos tallennuksessa ongelmia
     */
    public String poistaOlut(Olut kOlut, int kId) throws SailoException {
        String ret = kayttajanOluet.poistaOlut(kOlut.getId(), kId);
        tallenna();
        return ret;
    }


    /**
     * @param poistettava käyttäjä
     * @return merkkijono, jossa tekstiä jos virhe on tapahtunut
     * @throws SailoException jos tallennuksessa ongelmia
     */
    public String poistaKayttaja(Kayttaja poistettava) throws SailoException {
        if ( poistettava == null ) return "Valitse ensin poistettava käyttäjä!";
        
        String ret = kayttajat.poista(poistettava.getID());
        kayttajanOluet.poistaKaikki(poistettava.getID()); 
        tallenna();
        return ret; 
    }

    
    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        //
    }
}

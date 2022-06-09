package kalex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 13.3.2018
 *
 */
public class KayttajanOluet {
    
    private String tiedosto = "kayttajienOluet.dat";
    private boolean muutettu = false;
    
    private final ArrayList<KayttajanOlut> alkiot = new ArrayList<KayttajanOlut>();
    
    
    /**
     * Käyttäjän oluiden alustus
     */
    public KayttajanOluet() {
        //Ei tarvita vielä
    }
    
    
    /**
     * @param kOlut Käyttäjän tekemän kirjauksen lisäys
     * @example
     * <pre name="test">
     * KayttajanOluet kayttajanOluet = new KayttajanOluet();
     * KayttajanOlut kayttajanOlut = new KayttajanOlut(), kayttajanOlut2 = new KayttajanOlut();
     * kayttajanOluet.getLkm() === 0;
     * kayttajanOluet.lisaa(kayttajanOlut); kayttajanOluet.getLkm() === 1;
     * kayttajanOluet.lisaa(kayttajanOlut2); kayttajanOluet.getLkm() === 2;
     * kayttajanOluet.getLkm() === 2; 
     * </pre>
     */
    public void lisaa(KayttajanOlut kOlut) {
        alkiot.add(kOlut);
        muutettu = true;
    }
    
    
    /**
     * Palauttaa kirjattujen oluiden lukumäärän
     * @return kirjattujen oluiden lkm
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    
    /**
     * Lukee käyttäjien kirjaukset tiedostosta tietorakenteeseen.  
     * @throws SailoException jos lukeminen epäonnistuu
     */
    public void lueTiedostosta() throws SailoException {
        try (BufferedReader br = new BufferedReader(new FileReader(tiedosto)) ) {
            String rivi;
            
            while ((rivi = br.readLine()) != null) {
                rivi = rivi.trim();
                if (rivi.charAt(0) != ';') {
                    KayttajanOlut kayttajanOlut = new KayttajanOlut();
                    kayttajanOlut.parse(rivi); // virhekäsittely?
                    lisaa(kayttajanOlut);
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
     * Tallentaa käyttäjien kirjaamat oluet tiedostoon  
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if (!muutettu) return;

        try ( PrintWriter writer = new PrintWriter(new FileWriter(tiedosto)) ) {
            writer.print(";Käyttäjien Oluet");
            writer.print("\n" + ";kid|oid|arvosana|kommentti");
            for (int i = 0; i < this.alkiot.size(); i++) {
                if(this.alkiot.get(i) != null) writer.print("\n" + this.alkiot.get(i).toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedostoa " + tiedosto + " ei voida aukaista");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + tiedosto + " kirjoittamisessa ongelmia");
        }
        
        muutettu = false;
    }
    
    
    /**
     * Metodi käyttäjän kirjaamien oluiden hakemiseksi
     * @param kId Käyttäjä-id
     * @return Käyttäjän oluet listassa
     */
    public ArrayList<KayttajanOlut> haeKaikkiKayttajalta(int kId){
        ArrayList<KayttajanOlut> olutlista = new ArrayList<KayttajanOlut>();
        for (int i = 0; i < alkiot.size(); i++) {
            if (kId == alkiot.get(i).getKayttajaId()) olutlista.add(alkiot.get(i));
        }
        if (olutlista.size() == 0) return null;
        return olutlista;
    }
    
    
    /**
     * @param oId oluen id
     * @param kId käyttäjän id
     * @return palauttaa 
     */
    public KayttajanOlut getKirjaus(int kId, int oId) {
        KayttajanOlut kOlut = null;
        
        for (int i = 0; i < alkiot.size(); i++) {
            if (alkiot.get(i).getKayttajaId() == kId && alkiot.get(i).getOlutId() == oId) {
                kOlut = alkiot.get(i);
            }
        }
        
        return kOlut;
    }
    
    
    /**
     * @param i indeksi käyttäjienOluet.dat tiedostossa
     * @return Käyttäjän id
     */
    public int getKayttajaId(int i) {
        return alkiot.get(i).getKayttajaId();
    }
    
    
    /**
     * Päivittää kirjauksen arvosanan ja kommentin
     * @param kayttajaId käyttäjän id
     * @param olutId oluen id
     * @param arvosana kirjauksen arvosana
     * @param kommentti kirjauksen kommentti
     * @return null, jos oikein muotoiltu. 
     *         merkkijono, jos virheellinen
     */
    public String paivitaKirjaus(int kayttajaId, int olutId, Double arvosana, String kommentti) {
        String err = "";
        KayttajanOlut k = getKirjaus(kayttajaId, olutId);
        err = k.setArvosana(arvosana);
        k.setKommentti(kommentti);

        if (!err.equals("")) {
            return err;
        }

        muutettu = true;
        return "";
    }

    

    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        KayttajanOluet kayttajanOluet = new KayttajanOluet();
        KayttajanOlut kayttajanOlut = new KayttajanOlut(), kayttajanOlut2 = new KayttajanOlut();
        kayttajanOlut.kiminKarjala();
        kayttajanOlut2.kiminKarjala();
        
        kayttajanOluet.alkiot.add(kayttajanOlut);

        kayttajanOluet.lisaa(kayttajanOlut);
        kayttajanOluet.lisaa(kayttajanOlut2);
        /*
        System.out.println("------------Testi------------");
 
        for (int i = 0; i < kayttajanOluet.getLkm(); i++) {
             KayttajanOlut kOlut = kayttajanOluet.alkiot.get(i);
             System.out.println("Kirjauksen indeksi: " + i);
             kOlut.tulosta(System.out);
             System.out.println();
        }
         */
    }


    /** 
     * Poistaa Oluen
     * @param oId oluen id
     * @param kId käyttäjän id
     * @return merkkijono, jossa tekstiä jos virhe on tapahtunut
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Kayttaja kayttaja = new Kayttaja();
     * kayttaja.rekisteroi(); kayttaja.kimilleKalexia();
     * Olut olut = new Olut();
     * olut.rekisteroi(); olut.karjalatKitusiin();
     * KayttajanOluet kayttajanOluet = new KayttajanOluet(); 
     * KayttajanOlut kayttajanOlut = new KayttajanOlut();
     * kayttajanOlut.setTestIds(kayttaja.getID(), olut.getId()); kayttajanOlut.kiminKarjala();
     * kayttajanOluet.lisaa(kayttajanOlut);
     * kayttajanOluet.getLkm() === 1;
     * kayttajanOluet.poistaOlut(kayttajanOlut.getOlutId(), kayttajanOlut.getKayttajaId());
     * kayttajanOluet.getLkm() === 0; 
     * </pre> 
     *  
     */ 
    public String poistaOlut(int oId, int kId) {  
        for (int i = 0; i < alkiot.size(); i++) { 
            if (alkiot.get(i).getKayttajaId() == kId) {
                if (alkiot.get(i).getOlutId() == oId) {
                    alkiot.remove(alkiot.get(i));
                    muutettu = true; 
                    return "";
                }
            }
        }
        return "Poistettavaa olutta ei löytynyt";
    }


    /**
     * @param id käyttäjän id
     * @return merkkijono, jossa tekstiä jos virhe on tapahtunut
     */
    public String poistaKaikki(int id) {
        boolean onkoPoistettu = false;
        for (int i = 0; i < alkiot.size();) { 
            if (alkiot.get(i).getKayttajaId() == id) {
                alkiot.remove(alkiot.get(i));
                muutettu = true; 
                onkoPoistettu = true;
            } else i++;
        }
        if (onkoPoistettu) return "";
        return "Poistettavia oluita ei löytynyt";
    } 

}

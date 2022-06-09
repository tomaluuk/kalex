package kalex;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 9.3.2018
 *
 */
public class Kayttajat {

    private static final int MAX_LKM  = 2; // Kayttaja-oliotaulukon pituus alustettaessa
    private int              lkm      = 0; // Kayttaja-olioiden lukumäärä taulukossa
    private static String    tiedosto = "kayttajat.dat"; // Tiedosto, johon käyttäjien tiedot on tallennettu
    private Kayttaja[]       alkiot   = new Kayttaja[MAX_LKM]; // Kayttaja-oliotaulukko
 
    //Tarkistaa onko tiedostossa tallentamatta olevia tietoja
    private boolean muutettu = false;

 
    /**
     * Käyttäjän lisäys tietorakenteeseen
     * @param kayttaja lisättävä käyttäjä
     * @example
     * <pre name="test">
     * Kayttajat kayttajat = new Kayttajat();
     * Kayttaja kimi1 = new Kayttaja(), kimi2 = new Kayttaja();
     * kayttajat.getLkm() === 0;
     * kayttajat.lisaa(kimi1); kayttajat.getLkm() === 1;
     * kayttajat.lisaa(kimi2); kayttajat.getLkm() === 2;
     * kayttajat.getAlkiot() === 2;
     * kayttajat.lisaa(kimi1); kayttajat.getLkm() === 3;
     * kayttajat.getAlkiot() === 4;
     * kayttajat.tuoKayttaja(0) === kimi1;
     * kayttajat.tuoKayttaja(1) === kimi2;
     * kayttajat.tuoKayttaja(2) === kimi1;
     * kayttajat.tuoKayttaja(1) == kimi1 === false;
     * kayttajat.tuoKayttaja(1) == kimi2 === true; 
     * kayttajat.lisaa(kimi1); kayttajat.getLkm() === 4;
     * kayttajat.lisaa(kimi1); kayttajat.getLkm() === 5;
     * kayttajat.lisaa(kimi1); kayttajat.getLkm() === 6;
     * kayttajat.lisaa(kimi1); kayttajat.getLkm() === 7;
     * </pre>
     */
     public void lisaa(Kayttaja kayttaja){
         if (lkm >= alkiot.length) {
             Kayttaja[] temp_alkiot = alkiot.clone();
             alkiot = new Kayttaja[temp_alkiot.length + MAX_LKM];
             System.arraycopy(temp_alkiot, 0, alkiot, 0, temp_alkiot.length);
         }
         
         alkiot[lkm] = kayttaja;
         lkm++;
         muutettu = true;
     }
     
     
     /**
      * @param poistettava käyttäjä
      * @return merkkijono, jossa tekstiä jos virhe on tapahtunut
      */
     public String poista(int poistettava) {
         int indeksi = 0;
         for (int i = 0; i < lkm; i++) {
             if (alkiot[i].getID() == poistettava) {
                 alkiot[i] = null;
                 muutettu = true;
                 indeksi = i;
                 poistaValit(alkiot, indeksi);
                 lkm--;
                 return "";
             }
         }
         return "Käyttäjän poistaminen epäonnistui.";
     }
 

     /**
      * Poistaa null arvot käyttäjien taulukosta
      * @param alkioArray
      * @param indeksi
      */
    private void poistaValit(Kayttaja[] alkioArray, int indeksi) {
        if (indeksi == 0) {
            Kayttaja[] a = Arrays.copyOfRange(alkioArray, 1, lkm);
            setAlkiot(a);
            return;
        } 
        
        if (indeksi == lkm-1) {
            Kayttaja[] a = Arrays.copyOf(alkioArray, lkm-1);
            setAlkiot(a);
            return;
        } 
        
        Kayttaja[] a = Arrays.copyOfRange(alkioArray, 0, indeksi);
        Kayttaja[] b = Arrays.copyOfRange(alkioArray, indeksi+1, lkm);
        
        List<Kayttaja> lista = new ArrayList<Kayttaja>(Arrays.asList(a));
        lista.addAll(Arrays.asList(b));
        Kayttaja[] yhdistetty = new Kayttaja[lista.size()];
        yhdistetty = lista.toArray(yhdistetty);
        setAlkiot(yhdistetty);
    }


    /**
     * Asettaa käyttäjien listan
     * @param yhdistetty
     */
    private void setAlkiot(Kayttaja[] yhdistetty) {
        alkiot = yhdistetty;
    }


    /**
     * Palauttaa tallennettujen käyttäjien lukumäärän
     * @return käyttäjien lukumäärä
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
     * Tuo käyttäjän viitteen, joka on tallennettu parametrina tuodun indeksin paikalle 
     * @param i indeksi
     * @return olioviitteen indeksi
     * @throws IndexOutOfBoundsException Poikkeus väärälle indeksille
     */
    public Kayttaja tuoKayttaja(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Virheellinen indeksi: " + i);
        return alkiot[i];
    }
    
    
    /**
     * Tallentaa luodun käyttäjän tiedostoon
     * @throws SailoException Jos epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;
       
        try ( PrintWriter writer = new PrintWriter(new FileWriter(tiedosto)) ) {
            writer.print(";Käyttäjä");
            writer.print("\n" + ";kid|nimi|sAika|rAika");
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
     * Lukee käyttäjät tiedostosta Kayttaja-olioiksi
     * @throws SailoException Jos lukeminen ei onnistu
     */
    public void lueTiedostosta() throws SailoException {
        try (BufferedReader br = new BufferedReader(new FileReader(tiedosto))){
            String rivi;
            
            while ((rivi = br.readLine()) != null) {
                rivi = rivi.trim();
                if (rivi.charAt(0) != ';') {
                    Kayttaja kayttaja = new Kayttaja();
                    kayttaja.parse(rivi);
                    lisaa(kayttaja);
                }
            }
            muutettu = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Tiedostoa " + tiedosto + " ei voi avata!" );
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Ongelma tiedostoa luettaessa: " + e.getMessage());
        }
        
    }
    
    
    /**
     * Testiohjelma Kayttajat-luokalle
     * @param args ei käytössä
     */
    public static void main(String[] args) {

        Kayttajat kayttajat = new Kayttajat();
        Kayttaja kimi = new Kayttaja(), kimi2 = new Kayttaja(), kimi3 = new Kayttaja();
        kimi.rekisteroi();
        kimi.kimilleKalexia();
        kimi2.rekisteroi();
        kimi2.kimilleKalexia();
        kimi3.rekisteroi();
        kimi3.kimilleKalexia();
        kayttajat.lisaa(kimi);
        kayttajat.lisaa(kimi2);
        
        System.out.println("------------Testi------------");
 
        for (int i = 0; i < kayttajat.getLkm(); i++) {
             Kayttaja kayttaja = kayttajat.tuoKayttaja(i);
             System.out.println("Jäsenen indeksi: " + i);
             kayttaja.tulosta(System.out);
             System.out.println();
        }

    }
}

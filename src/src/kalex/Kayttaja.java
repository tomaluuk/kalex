/**
 * 
 */
package kalex;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 8.3.2018
 *
 */
public class Kayttaja {

    private int     kayttajaId;
    private String  nimi = "";
    private String  synt_aika = ""; //syntymäaika
    private String  rek_aika = ""; //rekisteröitymisaika

    private static int seuraavaId = 1;
    private static DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    
    /**
     * Rekisteröi käyttäjän
     * @return käyttäjälle osoitettu id-numero
     * @example
     * <pre name="test">
     *   Kayttaja kimi1 = new Kayttaja();
     *   kimi1.getID() === 0;
     *   kimi1.rekisteroi();
     *   Kayttaja kimi2 = new Kayttaja();
     *   kimi2.rekisteroi();
     *   int n1 = kimi1.getID();
     *   int n2 = kimi2.getID();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        this.kayttajaId = seuraavaId;
        seuraavaId++;
        setRekAika();
        return this.kayttajaId;
    }
    
    
    /**
     * Yhdistää käyttäjän etu- ja sukunimen
     * @return Käyttäjän koko nimi 
     */
    public String getNimi() {
        return this.nimi;
    }
    
    
    /**
     * Hakee käyttäjälle osoitetun ID:n
     * @return ID-numero
     */
    public int getID() {
        return kayttajaId;
    }
    
    
    /**
     * Asettaa id-numeron käyttäjälle
     * @param nro id-nro
     */
    public void setID(int nro) {
        this.kayttajaId = nro;
        if (this.kayttajaId >= seuraavaId) seuraavaId = this.kayttajaId + 1;
    }
    
    
    /**
     * Asettaa nimen käyttäjälle
     * @param nimi käyttäjän nimi
     * @return null, jos oikein muotoiltu. 
     *         merkkijono, jos virheellinen
     */
    public String setNimi(String nimi) {
        if (nimi.matches("[A-Za-z\\s]+")) {
            this.nimi = nimi.trim();
            return "";
        } 
        
        return "Nimessä virheellisiä merkkejä! Käytä vain aakkosia ja välilyöntejä.\n";
    }
    
    
    /**
     * Asettaa nimen käyttäjälle
     * @param synt_aika käyttäjän nimi
     * @return null, jos oikein muotoiltu. 
     *         merkkijono, jos virheellinen 
     */
    public String setSyntAika(String synt_aika) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false);
        String virheViesti = "";
        Date date = new Date();
        
        try {
            date = sdf.parse(synt_aika);
        } catch (ParseException e) {
            virheViesti = "Virheellinen päivämäärä! Anna syntymäaika muodossa \"pp.kk.vvv\".\n";
            e.printStackTrace();
            System.err.println(date);
        }
        
        if ("".equals(virheViesti)){
            this.synt_aika = synt_aika;
            return "";
        }
        
        return virheViesti;
    }
    
    /**
     * Asettaa rekisteröitymisajankohdan
     */
    public void setRekAika() {       
        LocalDate paivays = LocalDate.now();
        this.rek_aika = "" + fmt.format(paivays);
    }


    /**
     * Palauttaa käyttäjän syntymäajan
     * @return syntymäaika merkkijonona
     */
    public String getSyntymaAika() {
        return this.synt_aika;
    }
    
    
    /**
     * Palauttaa käyttäjän rekisteröitymisajan
     * @return rekisteröitymisaika merkkijonona
     */
    public String getRekAika() {
        return this.rek_aika;
    }    
    
    
    /**
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("ID: " + this.kayttajaId);
        out.println("Nimi: " + this.getNimi());
        out.println("Syntymäaika: " + synt_aika);
        out.println("Rekisteröityi: " + rek_aika);    
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Apumetodi, joka täyttää testikäyttäjälle tiedot
     */
    public void kimilleKalexia() {
        this.nimi = "Kimi Räikkönen";
        this.synt_aika = "19.4.1979";
        //this.rek_aika = "12.3.2018";
    }
    
    
    /**
     * Asettaa "|"-erotetun merkkijonon tiedot attribuuttien arvoiksi
     * @param data rivillinen käyttäjän dataa tiedostosta 
     * 
     */
    public void parse(String data) {
        StringBuffer sb = new StringBuffer(data);
        setID(Mjonot.erota(sb, '|', seuraavaId));
        nimi = Mjonot.erota(sb, '|', nimi);
        synt_aika = Mjonot.erota(sb, '|', synt_aika);
        rek_aika = Mjonot.erota(sb, '|', rek_aika);

        // Ettei kenoviivat pääse aiheuttamaan erroreita
        if(nimi.contains("\\")) nimi = nimi.replaceAll("\\", "");
        if(synt_aika.contains("\\")) synt_aika = synt_aika.replaceAll("\\", "");
        if(rek_aika.contains("\\")) rek_aika = rek_aika.replaceAll("\\", "");
    }
    
    
    
    @Override
    public String toString() {
        return "" + this.getID()   + "|" +
                    this.nimi      + "|" +
                    this.synt_aika + "|" +
                    this.rek_aika;
    }
    
    
    
    /**
     * Testiohjelma käyttäjälle
     * @param args ei käytössä
     */
    public static void main(String[] args) {       
        Kayttaja kimi = new Kayttaja();
        kimi.rekisteroi();
        kimi.setRekAika();
        kimi.kimilleKalexia();
        kimi.tulosta(System.out);
    }
}

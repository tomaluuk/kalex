/**
 * 
 */
package kalex;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 13.3.2018
 *
 */
public class KayttajanOlut {

    private int kayttajaId;
    private int olutId;
    private double arvosana;
    private String kommentti = "";
    
    
    /**
     * Alustusmetodi testiohjjelmalle
     */
    public KayttajanOlut() {
        
    }

    /**
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("Käyttäjän id: " + this.kayttajaId);
        out.println("Olut id: " + this.olutId);
        out.println("Arvosana: " + this.arvosana);
        out.println("Kommentti: " + this.kommentti);    
    }
    
    
    /**
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * Apumetodi testikäyttäjän kirjauksen täyttämiseksi.
     */
    public void kiminKarjala() {
        this.arvosana = 5;
        this.kommentti = "Tä ä onm ghyvöö";
    }
    
    
    /**
     * Asettaa testausta varten id:t
     * @param kId käyttäjä-id
     * @param oId olut-id
     */
    public void setTestIds(int kId, int oId) {
        this.kayttajaId = kId;
        this.olutId = oId;
    }
    
    /**
     * @return Käyttäjän id
     */
    public int getKayttajaId() {
        return this.kayttajaId;
    }
    
    
    /**
     * @return Oluen id
     */
    public int getOlutId() {
        return this.olutId;
    }
    
    
    /**
     * @param i 0, jos halutaan arvosana, 1 jos halutaan kommentti
     * @return kirjauksen olut-id, arvosana ja kommentti
     */
    public String anna(int i) {
        switch (i) {
        case 0:
            return "" + this.arvosana;
        case 1:
            return this.kommentti;
        default:
            return "Virhellinen indeksi: " + i + "\nKirjauksen tieojen hakeminen epäonnistui.";
        }

    }

    
    /**
     * @param kId käyttäjän id
     */
    public void setKayttajaId(int kId) {
        this.kayttajaId = kId;
    }
    
    
    /**
     * @param oId oluen id
     */
    public void setOlutId(int oId) {
        this.olutId = oId;
    }


    /**
     * Päivittää kirjauksen arvosanan
     * @param a arvosana
     * @return null, jos oikein muotoiltu. 
     *         merkkijono, jos virheellinen
     */
    public String setArvosana(double a) {
        if (a >= 0 && a <= 5) {
            this.arvosana = a;
            return "";
        }
        return "Anna arvosana väliltä 0 – 5. Käytä desimaalina pistettä.\n";
    }
    
    
    /**
     * Päivittää kirjauksen kommentin
     * @param k kommentti
     */
    public void setKommentti(String k) {
        this.kommentti = k;
    }
    
    
    /**
     * @param data rivillinen käyttäjän kirjauksen dataa
     */
    public void parse(String data) {
        StringBuffer sb = new StringBuffer(data);
        kayttajaId = (Mjonot.erota(sb, '|', kayttajaId));
        olutId = Mjonot.erota(sb, '|', olutId);
        arvosana = Mjonot.erota(sb, '|', arvosana);
        kommentti = Mjonot.erota(sb, '|', kommentti);

        // Ettei kenoviivat pääse aiheuttamaan erroreita
        if(Double.toString(arvosana).contains("\\")) {
            arvosana = Double.parseDouble(Double.toString(arvosana).replaceAll("\\", ""));
        }
        if(kommentti.contains("\\")) kommentti = kommentti.replaceAll("\\", "");       
    }
    
    
    /**
     * 
     */
    @Override
    public String toString() {
        return "" + kayttajaId   + "|" +
                    olutId      + "|" +
                    arvosana + "|" +
                    kommentti;
    }
    
    
    /**
     * Testiohjelma käyttäjän oluen kirjaamiselle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        KayttajanOlut kayttajanOlut = new KayttajanOlut();
        kayttajanOlut.kiminKarjala();
        kayttajanOlut.tulosta(System.out);
    }

}

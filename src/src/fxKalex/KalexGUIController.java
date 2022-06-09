package fxKalex;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import kalex.Kalex;
import kalex.Kayttaja;
import kalex.Olut;
import kalex.SailoException;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.*;

/**
 * Luokka kerhon käyttäliittymän tapahtumien hoitamiseksi.
 * @author Veikko Haakana
 * @version 18.1.2018
 */
public class KalexGUIController implements Initializable{

    @FXML private GridPane gridKayttaja;
    @FXML private ListChooser<Kayttaja> chooserKayttaja;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private TextField hakuehto;
    @FXML private Label labelVirhe;
    @FXML private ListChooser<Olut> chooserOlut;
    
    @FXML private TextField editNimi;
    @FXML private TextField editSynt;
    @FXML private TextField editRek;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        alusta();
    }

    /**
     * Käsitellään uuden jäsenen lisääminen
     */
    @FXML private void handleUusiKayttaja() {        
        Kayttaja uusiKayttaja = new Kayttaja();
        uusiKayttaja = KalexUusiKayttajaController.luoKayttaja(null, uusiKayttaja);
        if (uusiKayttaja != null) {
            lisaaKayttaja(uusiKayttaja);
            tallenna(); 
            tuoKayttajat();
        }
    }
    
    
    /**
     * Avaa käyttöliittymäikkunaan käyttäjän oluet
     */
    @FXML private void handleKayttajanOluet() {
        if (aktiivinenKayttaja == null) {
            Dialogs.showMessageDialog("Valitse ensin käyttäjä!");
            return;
        }
        
        KalexKayttajanOluetController.avaaKayttajanOluet(null, aktiivinenKayttaja, kalex);
    }
    
    @FXML private void handlePoistaKayttaja() throws SailoException {
        poistaKayttaja();
    }

    /**
     * Käsitellään tallennuskäsky
     */
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    /**
     * Käsitellään lopetuskäsky
     */
    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    }
    
    
    //Tilapäisratkaisu ennen kun osataan päivittää automaattisesti
    @FXML void handlePaivita() {
        tuoKayttajat();
    }

    
    @FXML void handleTietoja() {
        ModalController.showModal(KalexGUIController.class.getResource("KalexTietoja.fxml"), "Tietoja", null, "");
    }
    
    
    /* 
     * -------------------------------------------------------------------------
     * Tästä eteenpäin ei käyttöliittymään liittyvää koodia           
     * -------------------------------------------------------------------------
     */
    
    
    private Kalex kalex;
    private Kayttaja aktiivinenKayttaja;
    
    /**
     * Suorittaa tarvittavat alustukset. 
     * Luodaan tekstikenttä, johon tulostetaan 
     * käyttäjän tiedot
     */
    protected void alusta() {        
        chooserKayttaja.clear();
        chooserKayttaja.addSelectionListener(e -> tulostaKayttaja());
        
        editNimi.setDisable(true);
        editSynt.setDisable(true);
        editRek.setDisable(true); 
    }

    
    /**
     * Tietojen tallennus
     */
    private String tallenna() {
        try {
            kalex.tallenna();
            return null;
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tallennuksen yhteydessä oli onglemia! " + e.getMessage());
            return e.getMessage();
        }
    }


    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos saa sulkaa sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }


    /**
     * Tuo käyttäjän tiedot listasta tekstikenttään
     */
    protected void tulostaKayttaja() {
        aktiivinenKayttaja = chooserKayttaja.getSelectedObject();
        if (aktiivinenKayttaja != null) {
            editNimi.setText(aktiivinenKayttaja.getNimi());
            editSynt.setText(aktiivinenKayttaja.getSyntymaAika());
            editRek.setText(aktiivinenKayttaja.getRekAika());
        }
    }
    
    
    
    /**
     * Hakee käyttäjien tiedot listaan
     */
    protected void tuoKayttajat() {
      //Tyhjentää listan
        chooserKayttaja.clear(); 
        
        //hakee käyttäjät listaan uudestaan
        for (int i = 0; i < kalex.getKayttajaLkm(); i++) { 
            Kayttaja kayttaja = kalex.tuoKayttaja(i); 
            chooserKayttaja.add(kayttaja.getNimi(), kayttaja); 
        }
        chooserKayttaja.setSelectedIndex(0);

    }

    
    private String lisaaKayttaja(Kayttaja uusi) {          
        uusi.rekisteroi();
        
        try {
            kalex.lisaa(uusi);
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Käyttäjän lisääminen tietorakenteeseen epäonnistui!");
            e.printStackTrace();
        }
        return null;
    }
    
    
    /*
     * Poistetaan listalta valittu jäsen
     */
    private void poistaKayttaja() throws SailoException {
        if ( aktiivinenKayttaja == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko jäsen: " + aktiivinenKayttaja.getNimi(), "Kyllä", "Ei") )
            return;
            String err = "";
            err = kalex.poistaKayttaja(aktiivinenKayttaja);
            
            if (!err.equals("")) {
                Dialogs.showMessageDialog(err);
            } 
            tuoKayttajat();
            chooserKayttaja.setSelectedIndex(0);     
    }
    
    
    /**
     * @param kalex käyttöliittymän oluttietokanta
     */
    public void setKalex(Kalex kalex) {
        this.kalex = kalex;
    }

    
    /**
     * Lukee tiedot tiedostoista
     */
    public void lueTiedostot() {
        try {
            kalex.lueTiedostot();
            tuoKayttajat();
        } catch (SailoException e) {
            //System.err.println("Tiedostojen lukeminen ei onnistunut." + e.getMessage());
            Dialogs.showMessageDialog("Tiedostojen lukeminen ei onnistunut." + e.getMessage());
            //e.printStackTrace();
        }
        
    }
    
}
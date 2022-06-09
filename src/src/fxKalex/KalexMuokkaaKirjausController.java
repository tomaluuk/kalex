package fxKalex;

import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kalex.Kalex;
import kalex.KayttajanOlut;
import kalex.Olut;
import kalex.SailoException;

/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 23.2.2018
 *
 */
public class KalexMuokkaaKirjausController implements ModalControllerInterface<KayttajanOlut>{

    @FXML private TextField editNimi;
    @FXML private TextField editPanimo;
    @FXML private TextField editMaa;
    @FXML private TextField editTyyppi;
    @FXML private TextField editProsentit;
    @FXML private TextField editEbu;
    @FXML private TextField editArvosana;
    @FXML private TextArea areaKommentit;
    @FXML private Button btnTallennaMuokkaukset;
    
    @FXML void handlePeruuta() {
      //Sulkee ikkunan tallentamatta muutoksia
        suljeIkkuna();
    }

    @FXML void handleTallenna() {
        if (paivitaKirjaus().equals("")) {
            tallenna();        
            suljeIkkuna();
            
        }       
    }


    /* 
     * -------------------------------------------------------------------------
     * Tästä eteenpäin ei käyttöliittymään liittyvää koodia           
     * -------------------------------------------------------------------------
     */
    
    
    private Kalex kalex;
    private Olut valittuOlut;
    private int aktiivinenId;
    private KayttajanOlut kayttajanOlut;
    
    
    @Override
    public KayttajanOlut getResult() {
        return kayttajanOlut;
    }

    
    @Override
    public void handleShown() {
        valittuOlut = kalex.getOlutById(kayttajanOlut.getOlutId());
        aktiivinenId = kayttajanOlut.getKayttajaId();
        setTekstit();     
    }

    
    @Override
    public void setDefault(KayttajanOlut oletus) {
        kayttajanOlut = oletus;
    }
    
    
    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        try {
            kalex.tallenna();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Oluen tallennuksen yhteydessä oli onglemia! " + e.getMessage());
        }        
    }
    
    
    /**
     * Metodi luokan ulkopuolisille controller-kutsuille
     * @param modalityStage Ikkuna, jolle ollaan modaalisia
     * @param kayttajanOlut muokattava olut
     * @param kalex tietorakenne 
     */
    public static void avaaOluenMuokkaus(Stage modalityStage, KayttajanOlut kayttajanOlut, Kalex kalex) {       
        
                ModalController.<KayttajanOlut, KalexMuokkaaKirjausController>showModal(
                        KalexLisaaUusiOlutController.class.getResource
                ("KalexMuokkaaKirjausView.fxml"), 
                "Muokkaa oluen tietoja", 
                modalityStage, kayttajanOlut, ctrl -> ctrl.setKalex(kalex));
    }
    
    
    /**
     * Hakee valitun kirjauksen 
     * @return Olio käyttäjän oluesta
     */
    public KayttajanOlut getKayttajanOlut() {
        return kalex.getKirjaus(aktiivinenId, valittuOlut.getId());
    }
    
    
    /**
     * Päivittää muokatut tiedot
     */
    private String paivitaKirjaus() {
        String err = "";
        
        String kommentti = areaKommentit.getText();
        
        try {
            Double arvosana = Double.parseDouble(editArvosana.getText());
            err = kalex.paivitaKirjaus (aktiivinenId, valittuOlut.getId(), arvosana, kommentti);
        } catch (NumberFormatException e2) {
            Dialogs.showMessageDialog("Virheellisiä merkkejä arvosana-kentässä! " + e2.getMessage());
        }
        
        if (!err.equals("")) {
            Dialogs.showMessageDialog(err);
            return err;
        }
        
        return "";
    }
    
    
    /**
     * Metodi ikkunan sulkemiselle
     */
    private void suljeIkkuna() {
        ModalController.closeStage(btnTallennaMuokkaukset);
    }
    
    
    /**
     * Linkitetään käytössä olevaan Kalexiin
     * @param kalex Kalex
     */
    public void setKalex(Kalex kalex) {
        this.kalex = kalex;
    }
    
    
    /**
     * Kalexin metodit näkyviin
     * @param aktiivinenId valittu käyttäjä
     */
    public void setAktiivinen(int aktiivinenId) {
        this.aktiivinenId = aktiivinenId;
    }
    
    
    /**
     * Asetetaan listasta valittu käyttäjän olut
     * @param olut Käyttäjän valitsema olut listasta 
     */
    public void setValittuOlut(Olut olut) {
        this.valittuOlut = olut;
    }

    
    /**
     * Asettaa oluen tiedot kenttiin
     */
    public void setTekstit() {
        
        editNimi.setText(valittuOlut.getNimi());
        editPanimo.setText(valittuOlut.getPanimo());
        editMaa.setText(valittuOlut.getMaa());
        editTyyppi.setText(valittuOlut.getTyyppi());
        editProsentit.setText("" + valittuOlut.getProsentit());
        editEbu.setText("" + valittuOlut.getEbu());
        editArvosana.setText("" + kayttajanOlut.anna(0));
        areaKommentit.setText(kayttajanOlut.anna(1));
    }
}

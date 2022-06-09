package fxKalex;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import kalex.Kayttaja;

/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 23.2.2018
 *
 */
public class KalexUusiKayttajaController implements ModalControllerInterface<Kayttaja>{

    //Kalex kalex;
    private Kayttaja kayttaja = null;
    
    
    @FXML private TextField editNimi;
    @FXML private TextField editSynt;
    @FXML private Button btnLisaa;


    @FXML void handlePeruuta() {
      // Sulkee ikkunan tallentamatta muutoksia
        ModalController.closeStage(btnLisaa);
    }

    
    @FXML void handleLisaaKayttaja() {
        String err = "";
        err = kayttaja.setNimi(editNimi.getText());
        err += kayttaja.setSyntAika(editSynt.getText());
        
        if (!err.equals("")) {
            Dialogs.showMessageDialog(err);
            //return err;
        } else ModalController.closeStage(btnLisaa);
    }

   
    @Override
    public Kayttaja getResult() {
        return kayttaja;
    }

    
    @Override
    public void handleShown() {
        editNimi.requestFocus();
        
    }

    
    @Override
    public void setDefault(Kayttaja oletus) {
        kayttaja = null;        
    }
    
    
    /**
     * Metodi luokan ulkopuolisille controller-kutsuille
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param kayttaja luotava käyttäjä
     * @return käyttäjä
     */
    public static Kayttaja luoKayttaja(Stage modalityStage, Kayttaja kayttaja) {
        return ModalController.showModal(
                KalexUusiKayttajaController.class.getResource
                ("KalexUusiKayttajaView.fxml"), "Lisää uusi käyttäjä", modalityStage, kayttaja, null);
    }
    
    
}

package fxKalex;

import fi.jyu.mit.fxgui.ModalControllerInterface;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 23.2.2018
 *
 */
public class KalexMuokkaaOlutController implements ModalControllerInterface<String>{

    @FXML private Button btnTallennaMuokkaukset;
    
    @FXML void handlePeruuta() {
      //Sulkee ikkunan tallentamatta muutoksia
        suljeIkkuna();
    }

    @FXML void handleTallenna() {
        Dialogs.showMessageDialog("Tallennetaan! Mutta ei toimi vielä");
        suljeIkkuna();
     // TODO: tallentaa muokatut tiedot oikeaan tietorakenteeseen
    }

    @Override
    public String getResult() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void handleShown() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setDefault(String arg0) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * Metodi ikkunan sulkemiselle
     */
    private void suljeIkkuna() {
        ModalController.closeStage(btnTallennaMuokkaukset);
    }
}

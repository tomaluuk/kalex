
package fxKalex;

import fi.jyu.mit.fxgui.ModalControllerInterface;
import java.util.ArrayList;
import java.util.List;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
public class KalexLisaaUusiOlutController implements ModalControllerInterface<KayttajanOlut>{

    @FXML private Button btnTallennaOlut;
    @FXML private Button btnValitseOlut;

    @FXML private ListChooser<Olut> chooserOlut;
    @FXML private TextField hakuehto;
    @FXML private Label labelVirhe;
    @FXML private TextField editNimi;
    @FXML private TextField editPanimo;
    @FXML private TextField editMaa;
    @FXML private TextField editTyyppi;
    @FXML private TextField editProsentit;
    @FXML private TextField editEbu;
    @FXML private TextField editArvosana;
    @FXML private TextArea areaKommentit;
    
    
    @FXML void handlePeruuta() {
        suljeIkkuna();
    }

    @FXML void handleTallenna() {
        if (lisaaUusiOlut().equals("")) {
            tallenna();
            suljeIkkuna();
        }
    }

    @FXML void handleTyhjenna() {
        tyhjenna();
    }

    
    @FXML void handleHaku() {
        hae();
    }
    
    
    /* 
     * -------------------------------------------------------------------------
     * Tästä eteenpäin ei käyttöliittymään liittyvää koodia           
     * -------------------------------------------------------------------------
     */
    
    
    private Kalex kalex;
    private Olut valittuOlut;
    @SuppressWarnings("unused")
    private Olut olut;
    private KayttajanOlut kirjaus;
    private int aktiivinenId;
    
    
    /**
     * Oluen lisääminen tietokantaan
     */
    private String lisaaUusiOlut() {
        String nimi = editNimi.getText();
        String panimo = editPanimo.getText();
        String maa = editMaa.getText();
        String tyyppi = editTyyppi.getText();
        Double prosentit = 0.0;
        int ebu = 0;
        Double arvosana = 0.0;
        String kommentit = areaKommentit.getText();
        
        String err = "";
 
        try {
            ebu = Integer.parseInt(editEbu.getText());
        } catch (NumberFormatException e) {
            err += "Syötä Ebu-kenttään kokonaisluku\n";
        }
        
        try {
            prosentit = Double.parseDouble(editProsentit.getText());
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            err += "Syötä Prosentit-kenttään vain numeroita. Käytä desimaalina pistettä\n";
        }


        try {
            arvosana = Double.parseDouble(editArvosana.getText());
        } catch (NumberFormatException e) {
            //e.printStackTrace();
            err += "Syötä Arvosana-kenttään vain numeroita. Käytä desimaalina pistettä\n";
        }
        
        if (!err.equals("")) {
            Dialogs.showMessageDialog(err);
            return err;
        }

        String[] olutData = { nimi, panimo, maa, tyyppi, prosentit.toString(),
                "" + ebu, arvosana.toString(), kommentit };
        
        //Tarkastetaan löytyykö olut jo 
        int loytyyko = kalex.onkoOlemassa(olutData);

        if (loytyyko == -1) { // Mikäli olutta ei ole vielä kirjattu
            Olut uusiOlut = new Olut();
            err += uusiOlut.setProsentit(prosentit);
            err += uusiOlut.setEbu(ebu);
            
            if (!err.equals("")) {
                Dialogs.showMessageDialog(err);
                return err;
            }

            uusiOlut.rekisteroi();
            uusiOlut.setNimi(nimi);
            uusiOlut.setPanimo(panimo);
            uusiOlut.setMaa(maa);
            uusiOlut.setTyyppi(tyyppi);
            

            KayttajanOlut uusiKirjaus = new KayttajanOlut();
            err += uusiKirjaus.setArvosana(arvosana);
            if (!err.equals("")) {
                Dialogs.showMessageDialog(err);
                return err;
            }
            
            uusiKirjaus.setKayttajaId(aktiivinenId);
            uusiKirjaus.setOlutId(uusiOlut.getId());
            uusiKirjaus.setKommentti(kommentit);

            try {
                kalex.lisaa(uusiOlut);
            } catch (SailoException e) {
                Dialogs.showMessageDialog(
                        "Ongelma uutta olutta lisätessä " + e.getMessage());
            }

            tuoOluet();

            try {
                kalex.lisaa(uusiKirjaus);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SailoException e) {
                e.printStackTrace();
            }
            
        } else { //Jos olut löytyi tietorakenteesta
            KayttajanOlut uusiKirjaus = new KayttajanOlut();
            err += uusiKirjaus.setArvosana(arvosana);
            
            if (!err.equals("")) {
                Dialogs.showMessageDialog(err);
                return err;
            }

            uusiKirjaus.setKayttajaId(aktiivinenId);
            uusiKirjaus.setOlutId(loytyyko);
            uusiKirjaus.setKommentti(kommentit);
            
            try {
                kalex.lisaa(uusiKirjaus);
            } catch (NullPointerException e) {
                e.printStackTrace();
            } catch (SailoException e) {
                e.printStackTrace();
            }
        }
        return "";
        
    }
    
    
    /**
     * Tulostaa oluen tiedot tekstikenttään
     */
    protected void tulostaOlut() {
        valittuOlut = chooserOlut.getSelectedObject();
        if (valittuOlut != null) {
            editNimi.setText(valittuOlut.getNimi());     
            editPanimo.setText(valittuOlut.getPanimo());   
            editMaa.setText(valittuOlut.getMaa());
            editTyyppi.setText(valittuOlut.getTyyppi());
            editProsentit.setText(Double.toString( valittuOlut.getProsentit() ));
            editEbu.setText(Integer.toString(valittuOlut.getEbu()));
        }
    }
    
    
    /**
     * Hakee kaikkien oluiden tiedot listaan
     */
    protected void tuoOluet() {
        //Tyhjentää listan 
        chooserOlut.clear(); 
        
        // Hakee tiedot uudestaan listaan
        for (int i = 0; i < kalex.getOlutLkm(); i++) { 
            Olut olut1 = kalex.tuoOlut(i); 
            chooserOlut.add(olut1.getNimi(), olut1); 
        }
    }    
    
    
    
    @Override
    public KayttajanOlut getResult() { 
        return kirjaus;    
    }

    
    @Override
    public void handleShown() {
        olut = kalex.getOlutById(kirjaus.getOlutId());
        aktiivinenId = kirjaus.getKayttajaId();
        editNimi.requestFocus();
        setListener();
        tuoOluet();
    }

    
    @Override
    public void setDefault(KayttajanOlut oletus) {
        kirjaus = oletus;  
    }
    
    
    /**
     * Metodi ikkunan sulkemiselle
     */
    private void suljeIkkuna() {
        ModalController.closeStage(btnTallennaOlut);
    }

    
    
    /**
     * Metodi luokan ulkopuolisille controller-kutsuille
     * @param modalityStage ikkuna, jolle ollaan modaalisia
     * @param kirjaus käsiteltävä kirjaus
     * @param kalex tietorakenne
     */
    public static void avaaUusiOlut(Stage modalityStage, KayttajanOlut kirjaus, Kalex kalex) {
        ModalController.<KayttajanOlut, KalexLisaaUusiOlutController>showModal(
                KalexLisaaUusiOlutController.class.getResource
                ("KalexLisaaUusiOlutView.fxml"),
                "Lisää uusi olut", 
                modalityStage, kirjaus, ctrl -> ctrl.setKalex(kalex));
    }  
        
    
    /**
     * Kalexin metodit näkyviin
     * @param k Kalex
     */
    public void setKalex(Kalex k) {
        this.kalex = k;
    }
    
    
    /**
     * Kalexin metodit näkyviin
     * @param aktiivinenId valittu käyttäjä
     */
    public void setAktiivinen(int aktiivinenId) {
        this.aktiivinenId = aktiivinenId;
    }

    
    /**
     * Asettaa event listenerin olutlistalle
     */
    public void setListener() {
        chooserOlut.addSelectionListener(e -> tulostaOlut());
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
     * Asettaa oluen tiedot kenttiin
     * @param nimi oluen nimi
     * @param panimo oluen panimo
     * @param maa oluen valmistusmaa
     * @param tyyppi oluen tyyppi
     * @param prosentit oluen alkoholiprosentti
     * @param ebu oluen EBU-arvo
     * @param arvosana käyttäjän antama arvosana 
     * @param kommentit käyttäjän antama kommentti oluesta
     */
    public void setTekstit(String nimi, String panimo, String maa, 
                           String tyyppi, String prosentit, String ebu, 
                           String arvosana, String kommentit) {
        
        editNimi.setText(nimi);
        editPanimo.setText(panimo);
        editMaa.setText(maa);
        editTyyppi.setText(tyyppi);
        editProsentit.setText(prosentit);
        editEbu.setText(ebu);
        editArvosana.setText(arvosana);
        areaKommentit.setText(kommentit);
    }
    
    
    /**
     * Tyhjentään tekstikentät 
     */
    public void tyhjenna() {
        editNimi.setText("");
        editPanimo.setText("");
        editMaa.setText("");
        editTyyppi.setText("");
        editProsentit.setText("");
        editEbu.setText("");
        editArvosana.setText("");
        areaKommentit.setText("");
    }
    
    
    /**
     * Hakee hakuehtoa vastaavat oluet listaan
     */
    protected void hae() {
        chooserOlut.clear();
        String ehto = hakuehto.getText();
        
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";

        List<Olut> oluset = new ArrayList<Olut>();
        
        try {
            oluset = kalex.etsi(ehto);
        } catch (SailoException e) {
            e.printStackTrace();
        }
        
        if (oluset.size() < 1) return;
        
        for (Olut o : oluset) {
            chooserOlut.add(o.getNimi(), o);
        }

        chooserOlut.setSelectedIndex(0);
    }
    
}

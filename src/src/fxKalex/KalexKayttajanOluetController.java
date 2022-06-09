package fxKalex;

import java.util.ArrayList;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import kalex.Kalex;
import kalex.Kayttaja;
import kalex.KayttajanOlut;
import kalex.Olut;
import kalex.SailoException;

/**
 * @author Topi Luukkanen
 * @version 21.2.2018
 *
 */
public class KalexKayttajanOluetController implements ModalControllerInterface<Kayttaja> {
    
    @FXML private ListChooser<Olut> chooserKayttajanOluet;
    @FXML private TextArea areaKayttajanOlut;
    @FXML private Text textKayttajanNimi;
    @FXML private Button btnPaivita;
    
    @FXML private TextField editNimi;
    @FXML private TextField editPanimo;
    @FXML private TextField editMaa;
    @FXML private TextField editTyyppi;
    @FXML private TextField editProsentit;
    @FXML private TextField editEbu;
    @FXML private TextField editArvosana;
    @FXML private TextArea areaKommentit;
 
 
    /**
     * Käsitellään uuden oluen lisääminen tietokantaan
     */
    @FXML void handleLisaaUusiOlut() {        
        KayttajanOlut uusiKirjaus = new KayttajanOlut();
        uusiKirjaus.setKayttajaId(aktiivinenId);
        //uusiKirjaus.setOlutId(valittuOlut.getId());
        KalexLisaaUusiOlutController.avaaUusiOlut(null, uusiKirjaus, kalex);
        
        try {
            tuoKayttajanOluet();
        } catch (SailoException e) {
            System.err.println("Ongelma käyttäjän olutlistaa päivittäessä! (Uuden oluen lisäyksen jälkeen)");
            e.printStackTrace();
        }
    }
    
    
    /**
     * Käsitellään oluen tietojen muokkaaminen
     */
    @FXML void handleMuokkaaOlut() {
        muokkaaOlut();
    }

    
    /**
     * Käsitellään oluen tietojen muokkaaminen
     * @throws SailoException 
     */
    @FXML void handlePoistaOlut() throws SailoException {
        poistaOlut();
    }


    /**
     * Päivittää käyttäjän oluiden listan
     */
    @FXML void handlePaivita() {
        tuoOluet();
    }
    
    
    /* 
     * -------------------------------------------------------------------------
     * Tästä eteenpäin ei käyttöliittymään liittyvää koodia           
     * -------------------------------------------------------------------------
     */


    private Kalex kalex;
    private Olut valittuOlut;
    private int aktiivinenId;
    private Kayttaja kayttaja;

    
    /**
     * @see fi.jyu.mit.fxgui.ModalControllerInterface#getResult()
     */
    @Override
    public Kayttaja getResult() {
        return kayttaja;
    }
    
    
    /**
     * @see fi.jyu.mit.fxgui.ModalControllerInterface#handleShown()
     */
    @Override
    public void handleShown() {
        setListener();
        setAktiivinen(kayttaja.getID());
        try {
            tuoKayttajanOluet();
        } catch (SailoException e) {
            e.printStackTrace();
            Dialogs.showMessageDialog("Oluiden hakemmisessa ongelmia");
        }
        
        editNimi.setDisable(true); 
        editPanimo.setDisable(true);   
        editMaa.setDisable(true);      
        editTyyppi.setDisable(true);   
        editProsentit.setDisable(true);
        editEbu.setDisable(true);    
        editArvosana.setDisable(true);
        areaKommentit.setDisable(true);

    }
    
    
    /**
     * @see fi.jyu.mit.fxgui.ModalControllerInterface#setDefault(java.lang.Object)
     */
    @Override
    public void setDefault(Kayttaja kayt) {
        kayttaja = kayt;
    }
    
    
    /**
     * Lukee tiedot tiedostoista
     */
    public void lueTiedosto() {
        try {
            tuoKayttajanOluet();
        } catch (SailoException e) {
            System.err.println("Olut-tiedoston lukeminen ei onnistunut." + e.getMessage());
            e.printStackTrace();
        }
        
    }
    
    
    /**
     * Tuo käyttäjän tiedot listasta tekstikenttään
     * @throws SailoException Jos käyttäjän oluen tietojen tulostuksessa ongelmia
     */
    protected void tulostaKayttajanOlut() throws SailoException {
        valittuOlut = chooserKayttajanOluet.getSelectedObject();
        ArrayList<KayttajanOlut> kayttajanOluet = kalex.haeKayttajanOluet(aktiivinenId);

        if (valittuOlut != null) {
            for(int i = 0; i < kayttajanOluet.size(); i++) {
                if (kayttajanOluet.get(i).getOlutId() == valittuOlut.getId()) {
                   
                    editNimi.setText(valittuOlut.getNimi());     
                    editPanimo.setText(valittuOlut.getPanimo());   
                    editMaa.setText(valittuOlut.getMaa());
                    editTyyppi.setText(valittuOlut.getTyyppi());
                    editProsentit.setText( Double.toString( valittuOlut.getProsentit() ));
                    editEbu.setText( Integer.toString(valittuOlut.getEbu() ));
                    editArvosana.setText(kayttajanOluet.get(i).anna(0));
                    areaKommentit.setText(kayttajanOluet.get(i).anna(1));
                }
            }
        }
    }
    
    
    /**
     * Hakee oluiden tiedot listaan 
     * @throws SailoException Jos käyttäjän oluiden tuomisessa ongelmia
     */
    protected void tuoKayttajanOluet() throws SailoException {
        //Tyhjentää listan
        chooserKayttajanOluet.clear(); 
        
        int olutId = 0;

        ArrayList<KayttajanOlut> kayttajanOluet = kalex.haeKayttajanOluet(aktiivinenId);
        
        if (kayttajanOluet != null) {
        //hakee käyttäjän oluet uudestaan tietorakenteesta
            for (int i = 0; i < kayttajanOluet.size(); i++) { 
                olutId = kayttajanOluet.get(i).getOlutId();
                for (int j = 0; j < kalex.getOlutLkm(); j++) {
                    Olut olut = kalex.tuoOlut(j); 
                    if (olutId == olut.getId()) {
                        chooserKayttajanOluet.add(olut.getNimi(), olut); 
                    }
                }
            }
        }
        chooserKayttajanOluet.setSelectedIndex(0);        
    }
    
    
    /**
     * Metodi luokan ulkopuolisille controller-kutsuille
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param kayttaja luotava käyttäjä
     * @param kalex tietokanta
     */
    public static void avaaKayttajanOluet(Stage modalityStage, Kayttaja kayttaja, Kalex kalex) {
        ModalController.<Kayttaja, KalexKayttajanOluetController>showModal(
                KalexKayttajanOluetController.class.getResource
                ("KalexKayttajanOluetView.fxml"), 
                "Käyttäjän oluet", 
                modalityStage, kayttaja, ctrl -> ctrl.setKalex(kalex));
    }
    
    
    /**
     * Poistaa valitun oluen
     * @throws SailoException 
     */
    private void poistaOlut() throws SailoException {
        if ( valittuOlut == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko olut: " + valittuOlut.getNimi() + " kokoelmasta?", "Kyllä", "Ei") )
            return;
        String err = kalex.poistaOlut(valittuOlut, aktiivinenId);

        tuoOluet();
        
        if (!err.equals("")) {
            Dialogs.showMessageDialog(err);
            
        } 
            
        chooserKayttajanOluet.setSelectedIndex(0);
    }
     
    
    /**
     * Oluen tietojen muokkaaminen
     */
    private void muokkaaOlut() {
        KayttajanOlut kOlut = kalex.getKirjaus(aktiivinenId, valittuOlut.getId());
        KalexMuokkaaKirjausController.avaaOluenMuokkaus(null, kOlut, kalex);
        tuoOluet();
    }
    
    
    /**
     * Päivittää käyttäjän oluiden listan
     */
    private void tuoOluet() {
        int i = chooserKayttajanOluet.getSelectedIndex();
        try {
            tuoKayttajanOluet();
        } catch (SailoException e) {
            System.err.println("Ongelma olutlistan päivityksessä!");
            e.printStackTrace();
        }   
        chooserKayttajanOluet.setSelectedIndex(i);
        
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
     * Asettaa event listenerin käyttäjän olutlistalle
     */
    public void setListener() {
        chooserKayttajanOluet.addSelectionListener(e -> {
            try {
                tulostaKayttajanOlut();
            } catch (SailoException ex) {
                ex.printStackTrace();
            }
        });
    }

}

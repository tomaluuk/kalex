package fxKalex;
	
import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import kalex.Kalex;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;



/**
 * @author Topi Luukkanen & Veikko Haakana
 * @version 12.2.2018
 *
 */
public class KalexMain extends Application {
	@Override
	public void start(Stage primaryStage) {
	    try {
	        FXMLLoader ldr = new FXMLLoader(getClass().getResource("KalexGUIView.fxml")); // korjaa tiedostonimi
	        final Pane root = (Pane)ldr.load();
	        final KalexGUIController kalexCtrl = (KalexGUIController)ldr.getController(); // korjaa nimet
	        
	        final Scene scene = new Scene(root);
	        scene.getStylesheets().add(getClass().getResource("kalex.css").toExternalForm()); // korjaa tiedostonimi
	        primaryStage.setScene(scene);
	        primaryStage.setTitle("Kalex"); // korjaa title	        
	        
	        primaryStage.setOnCloseRequest((event) -> {
	            // Kutsutaan voikoSulkea-metodia
	            if ( !kalexCtrl.voikoSulkea() ) event.consume(); 
	        });
	        
	        Kalex kalex = new Kalex();
	        kalexCtrl.setKalex(kalex);
	        
	        primaryStage.show();
	        
	        kalexCtrl.lueTiedostot();
	        
	    } catch(Exception e) {
            e.printStackTrace();
        }
	    
	}


    /**
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        launch(args);
    }
}

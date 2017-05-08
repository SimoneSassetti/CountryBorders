package it.polito.tdp.country;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;

import it.polito.tdp.country.model.Country;
import it.polito.tdp.country.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class ContryController {
	private Model model;
	public void setModel(Model model){
		this.model=model;
		
		//rimpire la tendina con l'elenco completo delle nazioni
		boxPartenza.getItems().addAll(model.getCountries());
		
		
		
	}
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Country> boxPartenza;

    @FXML
    private ComboBox<Country> boxDestinazione;

    @FXML
    private TextArea txtResult;

    @FXML
    void handlePercorso(ActionEvent event) {
    	Country dest=boxDestinazione.getValue();
    	
    	List<Country> percorso=model.getPercorso(dest);
    	txtResult.setText(percorso.toString());
    	
    }

    @FXML
    void handleRaggiungibili(ActionEvent event) {
    	Country partenza=boxPartenza.getValue();
    	if(partenza==null){
    		txtResult.appendText("Errore! Devi selezionare lo stato di partenza");
    		return;
    	}
    	List<Country> raggiungibili=model.getRaggiungibili(partenza);
    	txtResult.setText(raggiungibili.toString());
    	
    	boxDestinazione.getItems().clear();//togliamo quelli trovati in precedenza dal menu a tendina del paese di destianazione
    	boxDestinazione.getItems().addAll(raggiungibili);
    	
    }

    @FXML
    void initialize() {
        assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Country.fxml'.";
        assert boxDestinazione != null : "fx:id=\"boxDestinazione\" was not injected: check your FXML file 'Country.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Country.fxml'.";

    }
}


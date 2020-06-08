package it.polito.tdp;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.model.Driver;
import it.polito.tdp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
    Model model;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Integer> boxAnno;

    @FXML
    private TextField textInputK;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    txtResult.clear();
    
    Integer anno = this.boxAnno.getValue();
    if(anno== null ) {
   	 txtResult.appendText("Seleziona Stagione");
   	 return;
   	}
    
    this.model.creaGrafo(anno);
    txtResult.appendText(String.format("Grafo creato con %d vertici e %d archi \n\n", this.model.nVertici(), this.model.nArchi()));
    Driver d = this.model.getGoldenWeel();
    txtResult.appendText(d.toString()+"\n");
    }

    @FXML
    void doTrovaDreamTeam(ActionEvent event) {
    txtResult.clear();
   
    Integer k ;
	try {
		k = Integer.parseInt(this.textInputK.getText());
	}catch(NumberFormatException e) {
		txtResult.appendText("Inserisci un numero tra 1 e 4");
	     return;
	}
	List<Driver> dreamTeam =this.model.getDremTeam(k);
    for(Driver d: dreamTeam) {
    	txtResult.appendText(d.toString()+"\n");
    }
   
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    public void setModel(Model model) {
    	this.model=model;
    	this.boxAnno.getItems().addAll(this.model.getAllYears());
    }
}

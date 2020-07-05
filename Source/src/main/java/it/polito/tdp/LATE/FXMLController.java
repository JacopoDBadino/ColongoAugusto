package it.polito.tdp.LATE;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

import it.polito.tdp.LATE.DAO.AirportSimDAO;
import it.polito.tdp.LATE.airportModel.Airport;
import it.polito.tdp.LATE.airportModel.TrafficFlow;
import it.polito.tdp.LATE.dataImport.AirportDataImport;
import it.polito.tdp.LATE.model.SimulationOutput;
import it.polito.tdp.LATE.model.Simulator;
import it.polito.tdp.LATE.planeModel.Plane;
import it.polito.tdp.LATE.planeModel.TablePlane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;

public class FXMLController {
	
	FileChooser fileChooser = new FileChooser();
	
	Airport airport = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnCaricaFile;
    
    @FXML
    private Text labelError;

    @FXML
    private Label labelNome;

    @FXML
    private Label labelICAO;

    @FXML
    private Label labelIATA;

    @FXML
    private Label labelElevazione;

    @FXML
    private ChoiceBox<TrafficFlow> choiceTrafficFlow;

    @FXML
    private Spinner<Integer> spinArrivi;
  
    @FXML
    private Spinner<Integer> spinIncremento;
  
    @FXML
    private Spinner<Integer> spinNumeroSim;

    @FXML
    private Slider sliderDistanza;

    @FXML
    private TableView<TablePlane> tableAerei;

    @FXML
    private Button btnReset;

    @FXML
    private Button btnAvvia;

    @FXML
    void doCarica(ActionEvent event) {
    	this.clearAll();
    	Stage newStage = new Stage();
		File selectedFile = this.fileChooser.showOpenDialog(newStage);
		newStage.close();
		try {
			String inputString = Files.readString(selectedFile.toPath());
			inputString = AirportDataImport.deleteDoubleSpaces(inputString);
			ArrayList<String> splitted = new ArrayList<String>(Arrays.asList(inputString.split("\n")));
			try {
				this.airport = AirportDataImport.importAirportFromXplane(splitted);
				
				if (this.airport != null && this.airport.isValid()) {
					this.labelNome.setText(this.airport.getName());
					this.labelICAO.setText(this.airport.getIcaoCode());
					this.labelIATA.setText(this.airport.getMetadata().get("iata_code"));
					this.labelElevazione.setText(String.valueOf(this.airport.getElevation()));
					this.choiceTrafficFlow.getItems().addAll(this.airport.getTrafficFlows());
					this.choiceTrafficFlow.setValue(this.airport.getTrafficFlows().get(0));
					
					ObservableList<TablePlane> planes = FXCollections.observableArrayList();
			        for (Plane p: AirportSimDAO.getRelevantPlanes(this.airport.getMaxWingspan())) {
			        	planes.add(new TablePlane(p));
			        }
			        Collections.sort(planes, new TablePlane.ReverseMTOWComparator());
			        tableAerei.getItems().addAll(planes);  
					
					this.setDisable(false);
				} else {
					this.errorInImport("Aeroporto non formattato correttamente");
				}
			} catch (Exception e) {
				this.errorInImport("Errore nell'importazione");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			this.errorInImport("Errore nella selezione del file");
			e.printStackTrace();
		}
		
    }
    
    void errorInImport(String error) {
    	//TODO: fai questo
    	this.labelError.setText(error);
    }

    @FXML
    void doReset(ActionEvent event) {
    	this.clearAll();
    }

    @FXML
    void doSimulazione(ActionEvent event) {
    	ArrayList<Plane> planeRoster = new ArrayList<Plane>();
    	ArrayList<SimulationOutput> output = new ArrayList<SimulationOutput>();
    	
    	for (TablePlane tp: this.tableAerei.getItems()) {
    		Plane p = tp.getPlane();
    		for (int i = 0; i<tp.getValue().getValue(); i++) {
    			planeRoster.add(p);
    		}
    	}
    	Simulator s = new Simulator(this.airport, planeRoster,this.choiceTrafficFlow.getValue().getName());
    	int arrivalsPerHour = this.spinArrivi.getValue();
    	s.setGoAroundThreshold(this.sliderDistanza.getValue());
    	
    	for (int i=0; i<this.spinNumeroSim.getValue(); i++) {
        	s.setArrivalsPerHour(arrivalsPerHour);
        	s.init();
        	s.run();
        	arrivalsPerHour += this.spinIncremento.getValue();
        	//System.out.println(s.getOutput());
        	output.add(s.getOutput());
        	
    	}
    	
    	try {
    	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ResultScene.fxml"));
    	    Parent root1 = (Parent) loader.load();
    	    ResultsFXMLController controller = loader.<ResultsFXMLController>getController();
    	    controller.setOutput(output);
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));  
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void clearAll() {
    	this.setDisable(true);
    	this.labelElevazione.setText("");
    	this.labelIATA.setText("");
    	this.labelICAO.setText("");
    	this.labelNome.setText("");
    	this.labelError.setText("");
    	this.choiceTrafficFlow.getItems().clear();
    	this.sliderDistanza.setValue(3.0);
    	this.spinArrivi.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 1));
    	this.spinIncremento.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 20, 1));
    	this.spinNumeroSim.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 1));
    	this.tableAerei.getItems().clear();
    }
    
    private void setDisable(boolean bool) {
    	this.btnAvvia.setDisable(bool);
    	this.btnReset.setDisable(bool);
    	this.sliderDistanza.setDisable(bool);
    	this.spinArrivi.setDisable(bool);
    	this.spinIncremento.setDisable(bool);
    	this.spinNumeroSim.setDisable(bool);
    	this.tableAerei.setDisable(bool);
    	this.choiceTrafficFlow.setDisable(bool);
    }

    @SuppressWarnings("unchecked")
	@FXML
    void initialize() {
        assert btnCaricaFile != null : "fx:id=\"btnCaricaFile\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert labelNome != null : "fx:id=\"labelNome\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert labelICAO != null : "fx:id=\"labelICAO\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert labelIATA != null : "fx:id=\"labelIATA\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert labelElevazione != null : "fx:id=\"labelElevazione\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert choiceTrafficFlow != null : "fx:id=\"choiceTrafficFlow\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert spinArrivi != null : "fx:id=\"spinArrivi\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert spinIncremento != null : "fx:id=\"spinIncremento\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert spinNumeroSim != null : "fx:id=\"spinNumeroSim\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert sliderDistanza != null : "fx:id=\"sliderDistanza\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert tableAerei != null : "fx:id=\"tableAerei\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnReset != null : "fx:id=\"btnReset\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert btnAvvia != null : "fx:id=\"btnAvvia\" was not injected: check your FXML file 'MainScene.fxml'.";
        assert labelError != null: "fx:id=\"labelError\" was not injected: check your FXML file 'MainScene.fxml'.";
        
        TableColumn<TablePlane, String> manufacturerColumn = new TableColumn<>("Costruttore");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<TablePlane, String>("manufacturer"));
        TableColumn<TablePlane, String> modelColumn = new TableColumn<>("Modello");
        modelColumn.setCellValueFactory(new PropertyValueFactory<TablePlane, String>("model"));
        TableColumn<TablePlane, Double> wingspanColumn = new TableColumn<>("Apertura Alare");
        wingspanColumn.setCellValueFactory(new PropertyValueFactory<TablePlane, Double>("wingspan"));
        TableColumn<TablePlane, Double> approachSpeedColumn = new TableColumn<>("Velocità di atterraggio");
        approachSpeedColumn.setCellValueFactory(new PropertyValueFactory<TablePlane, Double>("approachSpeed"));
        TableColumn<TablePlane, Double> mtowColumn = new TableColumn<>("MTOW");
        mtowColumn.setCellValueFactory(new PropertyValueFactory<TablePlane, Double>("MTOW"));
        TableColumn<TablePlane, Spinner<Integer>> valueColumn = new TableColumn<>("Valore");
        valueColumn.setCellValueFactory(new PropertyValueFactory<TablePlane, Spinner<Integer>>("value"));
        
        this.tableAerei.getColumns().addAll(manufacturerColumn, modelColumn, wingspanColumn, approachSpeedColumn, mtowColumn, valueColumn);
         
        

        this.fileChooser.setTitle("Importa Aeroporto");
        fileChooser.getExtensionFilters().add(new ExtensionFilter("File APT X-Plane", "*.dat"));
        
        this.clearAll();
        
    }
}

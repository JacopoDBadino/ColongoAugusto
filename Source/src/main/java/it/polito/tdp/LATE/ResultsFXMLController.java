package it.polito.tdp.LATE;

import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.LATE.airportModel.Gate;
import it.polito.tdp.LATE.airportModel.Runway;
import it.polito.tdp.LATE.model.SimulationOutput;
import it.polito.tdp.LATE.planeModel.Flight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ResultsFXMLController {

	private List<SimulationOutput> output;
	private int detailSelection = 0;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private LineChart<String, Integer> arrivalTaktLineChart;

	@FXML
	private LineChart<String, Integer> landingTaktLineChart;

	@FXML
	private LineChart<String, Integer> takeoffTaktLineChart;

	@FXML
	private LineChart<String, Integer> runwayTaktLineChart;

	@FXML
	private LineChart<String, Integer> goAroundLineChart;

	@FXML
	private LineChart<String, Long> timeWaitingLineChart;

	@FXML
	private LineChart<String, Long> cycleGroundLineChart;

	@FXML
	private LineChart<String, Long> totalCycleLineChart;

	@FXML
	private Button btnPrev;

	@FXML
	private Label labelThisNumber;

	@FXML
	//ok
	private Label labelMaxNumber;

	@FXML
	private Button btnNext;

	@FXML
	//ok
	private Label labelNumAerei;

	@FXML
	//ok
	private PieChart aereiFallitiPieChart;
	//ok
	@FXML
	private BarChart<String, Double> throughputFasiBarChart;
	//ok
	@FXML
	private BarChart<String, Long> taktTimeRwyBarChart;
	//ok
	@FXML
	private BarChart<String, Double> utilizzoRwyBarChart;

	@FXML
	private BarChart<String, Double> utilizzoGateBarChart;

	@FXML
	private BarChart<String, Long> tempoMedioGateBarChart;

	@FXML
	void initialize() {

		assert arrivalTaktLineChart != null : "fx:id=\"arrivalTaktLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert landingTaktLineChart != null : "fx:id=\"landingTaktLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert takeoffTaktLineChart != null : "fx:id=\"takeoffTaktLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert runwayTaktLineChart != null : "fx:id=\"runwayTaktLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert goAroundLineChart != null : "fx:id=\"goAroundLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert timeWaitingLineChart != null : "fx:id=\"timeWaitingLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert cycleGroundLineChart != null : "fx:id=\"cycleGroundLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert totalCycleLineChart != null : "fx:id=\"totalCycleLineChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert btnPrev != null : "fx:id=\"btnPrev\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert labelThisNumber != null : "fx:id=\"labelThisNumber\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert labelMaxNumber != null : "fx:id=\"labelMaxNumber\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert btnNext != null : "fx:id=\"btnNext\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert labelNumAerei != null : "fx:id=\"labelNumAerei\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert aereiFallitiPieChart != null : "fx:id=\"aereiFallitiPieChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert throughputFasiBarChart != null : "fx:id=\"throughputFasiBarChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert taktTimeRwyBarChart != null : "fx:id=\"taktTimeRwyBarChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert utilizzoRwyBarChart != null : "fx:id=\"utilizzoRwyBarChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert utilizzoGateBarChart != null : "fx:id=\"utilizzoGateBarChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		assert tempoMedioGateBarChart != null : "fx:id=\"tempoMedioGateBarChart\" was not injected: check your FXML file 'ResultScene.fxml'.";
		
		// complessivo
		
		this.arrivalTaktLineChart.getXAxis().setLabel("Numero Simulazione");
		this.arrivalTaktLineChart.getYAxis().setLabel("Takt Time (s)");
		this.arrivalTaktLineChart.setLegendVisible(false);

		this.landingTaktLineChart.getXAxis().setLabel("Numero Simulazione");
		this.landingTaktLineChart.getYAxis().setLabel("Takt Time (s)");
		this.landingTaktLineChart.setLegendVisible(false);

		this.takeoffTaktLineChart.getXAxis().setLabel("Numero Simulazione");
		this.takeoffTaktLineChart.getYAxis().setLabel("Takt Time (s)");
		this.takeoffTaktLineChart.setLegendVisible(false);

		this.runwayTaktLineChart.getXAxis().setLabel("Numero Simulazione");
		this.runwayTaktLineChart.getYAxis().setLabel("Takt Time (s)");
		this.runwayTaktLineChart.setLegendVisible(false);

		this.goAroundLineChart.getXAxis().setLabel("Numero Simulazione");
		this.goAroundLineChart.getYAxis().setLabel("Numero di Go Around");
		this.goAroundLineChart.setLegendVisible(false);

		this.timeWaitingLineChart.getXAxis().setLabel("Numero Simulazione");
		this.timeWaitingLineChart.getYAxis().setLabel("Tempo di attesa (s)");
		this.timeWaitingLineChart.setLegendVisible(false);

		this.cycleGroundLineChart.getXAxis().setLabel("Numero Simulazione");
		this.cycleGroundLineChart.getYAxis().setLabel("Cycle Time (s)");
		this.cycleGroundLineChart.setLegendVisible(false);

		this.totalCycleLineChart.getXAxis().setLabel("Numero Simulazione");
		this.totalCycleLineChart.getYAxis().setLabel("Cycle Time (s)");
		this.totalCycleLineChart.setLegendVisible(false);

		// dettaglio
		
		this.throughputFasiBarChart.getXAxis().setLabel("Fase");
		this.throughputFasiBarChart.getYAxis().setLabel("Throughput (aerei/h)");
		this.throughputFasiBarChart.setLegendVisible(false);

		
		this.taktTimeRwyBarChart.getXAxis().setLabel("Pista");
		this.taktTimeRwyBarChart.getYAxis().setLabel("Takt Time (s)");
		this.taktTimeRwyBarChart.setLegendVisible(false);

		this.utilizzoRwyBarChart.getXAxis().setLabel("Pista");
		this.utilizzoRwyBarChart.getYAxis().setLabel("Utilizzo (%)");
		this.utilizzoRwyBarChart.setLegendVisible(false);

		
		this.utilizzoGateBarChart.getXAxis().setLabel("Gate");
		this.utilizzoGateBarChart.getYAxis().setLabel("Utilizzo (%)");
		this.utilizzoGateBarChart.setLegendVisible(false);

		
		this.tempoMedioGateBarChart.getXAxis().setLabel("Gate");
		this.tempoMedioGateBarChart.getYAxis().setLabel("Tempo (s)");
		this.tempoMedioGateBarChart.setLegendVisible(false);
		
	}
	
	@FXML
	void doBtnPrev(){
		this.btnNext.setDisable(false);
		if (this.detailSelection > 0) {
			this.detailSelection --;
			this.plotDetailCharts(this.detailSelection);
		}
		
		if (this.detailSelection == 0) {
			this.btnPrev.setDisable(true);
		}
		
		this.labelThisNumber.setText(String.valueOf(this.detailSelection+1));
		
		
	}
	
	@FXML 
	void doBtnNext(){
		this.btnPrev.setDisable(false);
		if (this.detailSelection < this.output.size() - 1) {
			this.detailSelection ++;
			this.plotDetailCharts(this.detailSelection);
		}
		
		if (this.detailSelection >= this.output.size() - 2) {
			this.btnNext.setDisable(true);
		}
		this.labelThisNumber.setText(String.valueOf(this.detailSelection+1));

	}

	@SuppressWarnings("unchecked")
	public void plotOverviewCharts() {

		XYChart.Series<String, Integer> arrivalTaktSeries = new XYChart.Series<String, Integer>();
		XYChart.Series<String, Integer> landingTaktSeries = new XYChart.Series<String, Integer>();
		XYChart.Series<String, Integer> takeoffTaktSeries = new XYChart.Series<String, Integer>();
		XYChart.Series<String, Integer> runwayTaktSeries = new XYChart.Series<String, Integer>();

		XYChart.Series<String, Integer> goAroundSeries = new XYChart.Series<String, Integer>();
		XYChart.Series<String, Long> timeWaitingSeries = new XYChart.Series<String, Long>();
		XYChart.Series<String, Long> cycleGroundSeries = new XYChart.Series<String, Long>();
		XYChart.Series<String, Long> cycleTotalSeries = new XYChart.Series<String, Long>();

		for (int i = 0; i < this.output.size(); i++) {
			SimulationOutput so = this.output.get(i);
			int goAroundCount = 0;
			Duration timeWaited = Duration.ZERO;
			Duration cycleGround = Duration.ZERO;
			Duration cycleTotal = Duration.ZERO;

			List<LocalTime> arrivalList = new ArrayList<LocalTime>();
			List<LocalTime> landingList = new ArrayList<LocalTime>();
			List<LocalTime> takeoffList = new ArrayList<LocalTime>();

			int flightsCompleted = 0;
			for (Flight f : so.getFlights()) {
				arrivalList.add(f.getTimeArrival());
				landingList.add(f.getTimeLanding());
				takeoffList.add(f.getTimeTakeOff());

				goAroundCount += f.getNumGoAround();
				timeWaited = timeWaited.plus(f.getTimeWaitingOnGround());
				if (f.getTimeTakeOff() != null) {
					cycleGround = cycleGround.plus(f.getGroundCycleTime());
					cycleTotal = cycleTotal.plus(f.getTotalCycleTime());
					flightsCompleted++;
				}
			}
			arrivalList.removeAll(Collections.singleton(null));
			landingList.removeAll(Collections.singleton(null));
			takeoffList.removeAll(Collections.singleton(null));
			
			timeWaited = timeWaited.dividedBy(so.getFlights().size());
			cycleGround = cycleGround.dividedBy(flightsCompleted);
			cycleTotal = cycleTotal.dividedBy(flightsCompleted);

			long timeWaitedNum = timeWaited.toSeconds();
			long cycleGroundNum = cycleGround.toSeconds();
			long cycleTotalNum = cycleTotal.toSeconds();

			goAroundSeries.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), goAroundCount));
			timeWaitingSeries.getData().add(new XYChart.Data<String, Long>(String.valueOf(i), timeWaitedNum));
			cycleGroundSeries.getData().add(new XYChart.Data<String, Long>(String.valueOf(i), cycleGroundNum));
			cycleTotalSeries.getData().add(new XYChart.Data<String, Long>(String.valueOf(i), cycleTotalNum));

			List<LocalTime> runwayTimeList = new ArrayList<LocalTime>(takeoffList);
			runwayTimeList.addAll(takeoffList);

			Collections.sort(arrivalList);
			Collections.sort(landingList);
			Collections.sort(takeoffList);
			Collections.sort(runwayTimeList);
			if (i > 0) {
				int arrivalSum = 0;
				for (int j = 1; j < arrivalList.size(); j++) {
					arrivalSum += Duration.between(arrivalList.get(j), arrivalList.get(j - 1)).getSeconds();
				}
				arrivalSum = -arrivalSum / (arrivalList.size() - 1);
				arrivalTaktSeries.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), arrivalSum));

				int landingSum = 0;
				//System.out.println(landingList.size());
				for (int j = 1; j < landingList.size(); j++) {
					landingSum += Duration.between(landingList.get(j), landingList.get(j - 1)).getSeconds();
				}
				landingSum = -landingSum / (landingList.size() - 1);
				landingTaktSeries.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), landingSum));

				int takeoffSum = 0;
				for (int j = 1; j < takeoffList.size(); j++) {
					takeoffSum += Duration.between(takeoffList.get(j), takeoffList.get(j - 1)).getSeconds();
				}
				takeoffSum = -takeoffSum / (takeoffList.size() - 1);
				takeoffTaktSeries.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), takeoffSum));

				int runwaySum = 0;
				for (int j = 1; j < runwayTimeList.size(); j++) {
					runwaySum += Duration.between(runwayTimeList.get(j), runwayTimeList.get(j - 1)).getSeconds();
				}
				runwaySum = -runwaySum / (runwayTimeList.size() - 1);
				runwayTaktSeries.getData().add(new XYChart.Data<String, Integer>(String.valueOf(i), runwaySum));
			}
		}

		this.goAroundLineChart.getData().addAll(goAroundSeries);
		this.timeWaitingLineChart.getData().addAll(timeWaitingSeries);
		this.cycleGroundLineChart.getData().addAll(cycleGroundSeries);
		this.totalCycleLineChart.getData().addAll(cycleTotalSeries);
		this.arrivalTaktLineChart.getData().addAll(arrivalTaktSeries);
		this.landingTaktLineChart.getData().addAll(landingTaktSeries);
		this.takeoffTaktLineChart.getData().addAll(takeoffTaktSeries);
		this.runwayTaktLineChart.getData().addAll(runwayTaktSeries);

	}


	
	public void plotDetailCharts(int simNum) {
		
		this.labelNumAerei.setText(String.valueOf(this.output.get(simNum).getFlights().size()));
		int failedFlights = 0;
		int okFlights = 0;
		for (Flight f: this.output.get(simNum).getFlights()) {
			// piechart
			if (f.getTimeTakeOff() == null) {
				failedFlights++;
			} else {
				okFlights++;
			}
			
		}	
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Falliti", failedFlights),
				new PieChart.Data("Riusciti", okFlights));
		this.aereiFallitiPieChart.setData(pieChartData);
		
		//throughput comp
		int arrivalTakt = this.arrivalTaktLineChart.getData().get(0).getData().get(simNum).getYValue();
		double arrivalTH = 3600.0/arrivalTakt;
		int landingTakt = this.landingTaktLineChart.getData().get(0).getData().get(simNum).getYValue();
		double landingTH = 3600.0/landingTakt;
		int takeoffTakt = this.takeoffTaktLineChart.getData().get(0).getData().get(simNum).getYValue();
		double takeoffTH = 3600.0/takeoffTakt;
		XYChart.Series<String, Double> seriesTH = new XYChart.Series<String, Double>();
        seriesTH.getData().add(new XYChart.Data<String, Double>("Arrival", arrivalTH));
        seriesTH.getData().add(new XYChart.Data<String, Double>("Landing", landingTH));
        seriesTH.getData().add(new XYChart.Data<String, Double>("TakeOff", takeoffTH));
        this.throughputFasiBarChart.getData().clear();
        this.throughputFasiBarChart.getData().add(seriesTH);
        
        
        //RUNWAYS
		XYChart.Series<String, Double> seriesRwyUsage = new XYChart.Series<String, Double>();
		XYChart.Series<String, Long> seriesRwyTakt = new XYChart.Series<String, Long>();
        for(Runway r: this.output.get(simNum).getRunwayUsage().keySet()) {
        	int countMovements = 0;
        	for (Flight f: this.output.get(simNum).getFlights()) {
        		if (f.getArrivalRunway().equals(r)) {
        			countMovements++;
        		} else if (f.getDepartureRunway().equals(r)) {
        			countMovements++;
        		}
        	}
        	long runwayTaktTime = 0;
        	if (countMovements != 0) {
        		runwayTaktTime = this.output.get(simNum).getRunwayUsage().get(r).getSeconds()/countMovements;
        	}
        	double runwayPercUsage =  100.0 * (this.output.get(simNum).getRunwayUsage().get(r).getSeconds()) / (Duration.between(LocalTime.MIDNIGHT, this.output.get(simNum).getLastAction()).getSeconds());
            seriesRwyUsage.getData().add(new XYChart.Data<String, Double>(r.getNum(), runwayPercUsage));
            seriesRwyTakt.getData().add(new XYChart.Data<String, Long>(r.getNum(), runwayTaktTime));
        }
        this.taktTimeRwyBarChart.getData().clear();
        this.taktTimeRwyBarChart.getData().add(seriesRwyTakt);
        this.utilizzoRwyBarChart.getData().clear();
        this.utilizzoRwyBarChart.getData().add(seriesRwyUsage);
        
        
        //GATES
		XYChart.Series<String, Double> seriesGateUsage = new XYChart.Series<String, Double>();
		XYChart.Series<String, Long> seriesGateTime = new XYChart.Series<String, Long>();
		
		for (Gate g: this.output.get(simNum).getGatesUsage().keySet()) {
			long numOfPlanesAtGate = 0;
			for (Flight f: this.output.get(simNum).getFlights()) {
				if (f.getGateUsed().equals(g)) {
					numOfPlanesAtGate++;
				} 
			}
			long gateMeanTime = 0;
			if (numOfPlanesAtGate != 0) {
				gateMeanTime = this.output.get(simNum).getGatesUsage().get(g).getSeconds()/numOfPlanesAtGate;
				double gatePercUsage = 100.0 * this.output.get(simNum).getGatesUsage().get(g).getSeconds() / Duration.between(LocalTime.MIDNIGHT, this.output.get(simNum).getLastAction()).getSeconds();
				seriesGateUsage.getData().add(new XYChart.Data<String, Double>(g.getId(), gatePercUsage));
				seriesGateTime.getData().add(new XYChart.Data<String, Long>(g.getId(), gateMeanTime));
			}	
		}
		this.tempoMedioGateBarChart.getData().clear();
		this.tempoMedioGateBarChart.getData().add(seriesGateTime);
		this.utilizzoGateBarChart.getData().clear();
		this.utilizzoGateBarChart.getData().add(seriesGateUsage);
	}

	public void setOutput(List<SimulationOutput> output) {
		this.detailSelection = 0;
		this.output = output;
		this.plotOverviewCharts();
		this.plotDetailCharts(this.detailSelection);
		this.labelMaxNumber.setText(String.valueOf(this.output.size()));
		this.labelThisNumber.setText(String.valueOf(this.detailSelection+1));
		this.btnPrev.setDisable(true);
	}

}

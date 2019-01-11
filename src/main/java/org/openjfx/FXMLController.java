package org.openjfx;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.chart.LineChart;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;

public class FXMLController implements Initializable {

    private Client client;
    private XYChart.Series temperature_line = new XYChart.Series();
    private XYChart.Series humidity_line = new XYChart.Series();
    private XYChart.Series luminosity_line = new XYChart.Series();
    private XYChart.Series pressure_line = new XYChart.Series();


    @FXML
    private TextField dateField;

    @FXML
    private CheckBox checkBox_t, checkBox_h, checkBox_l, checkBox_p;

    @FXML
    private LineChart lineChart_T, lineChart_H, lineChart_L, lineChart_P;

    @FXML
    private TextField deviceName_text, temperature_text, luminosity_text, humidity_text, pressure_text;


    public void setModel(Client client){
        this.client = client;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lineChart_T.setLegendVisible(false);
        lineChart_L.setLegendVisible(false);
        lineChart_H.setLegendVisible(false);
        lineChart_P.setLegendVisible(false);
        dateField.setText(getDate());


        //dummy data
        temperature_line.getData().add(new XYChart.Data( 05, 18));
        lineChart_T.getData().add(temperature_line);

        humidity_line.getData().add(new XYChart.Data( 05, 14));
        lineChart_H.getData().add(humidity_line);

        luminosity_line.getData().add(new XYChart.Data( 05, 10));
        lineChart_L.getData().add(luminosity_line);

        pressure_line.getData().add(new XYChart.Data( 05, 7000));
        lineChart_P.getData().add(pressure_line);
    }

    @FXML
    private void temperature(){
        temperature_text.setText("");
        temperature_text.setText(Double.toString(getValue("Temperature")));
    }

    @FXML
    private void luminosity(){
        luminosity_text.setText("");
        luminosity_text.setText(Double.toString(getValue("Luminosity")));
    }

    @FXML
    private void humidity(){
        humidity_text.setText("");
        humidity_text.setText(Double.toString(getValue("Humidity")));
    }

    @FXML
    private void pressure(){
        pressure_text.setText("");
        pressure_text.setText(Double.toString(getValue("Pressure")));
    }

    @FXML
    private void getDeviceNames(){
        String deviceNames= client.processRequest("DeviceNames");
        deviceName_text.setText("");
        deviceName_text.setText(deviceNames);
    }

    @FXML
    private void refreshData(){
        temperature();
        humidity();
        luminosity();
        pressure();
        if(checkBox_t.isSelected())temperatureLineChart();
        if(checkBox_h.isSelected())humidityLineChart();
        if(checkBox_l.isSelected())luminosityLineChart();
        if(checkBox_p.isSelected())pressureLineChart();
    }

    @FXML
    private void temperatureLineChart(){

        if(checkBox_t.isSelected()){
            temperature_line.getData().add(new XYChart.Data( getTime(), getValue("Temperature")));

        }
        else {
            lineChart_T.getData().remove(temperature_line);
        }
    }
    @FXML
    private void humidityLineChart(){
        if(checkBox_h.isSelected()){
            humidity_line.getData().add(new XYChart.Data( getTime(), getValue("Humidity")));
        }
        else {
            lineChart_H.getData().remove(humidity_line);
        }
    }
    @FXML
    private void luminosityLineChart(){
        if(checkBox_l.isSelected()){
            luminosity_line.getData().add(new XYChart.Data( getTime(), getValue("Luminosity")));
        }
        else {
            lineChart_L.getData().remove(luminosity_line);
        }
    }
    @FXML
    private void pressureLineChart(){
        if(checkBox_p.isSelected()){
            pressure_line.getData().add(new XYChart.Data( getTime(), getValue("Pressure")));
        }
        else {
            lineChart_P.getData().remove(pressure_line);
        }
    }

    private double getTime(){
        double time_d= 0.0;
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date();
        Matcher time_m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(dateFormat.format(date));

        while (time_m.find())
        {
            time_d = Double.parseDouble(time_m.group(1));
        }
        return time_d;
    }

    private double getValue(String Request){
        double value_d=0.0;
        String value= client.processRequest(Request);
        Matcher value_m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(value);
        while (value_m.find())
        {
            value_d = Double.parseDouble(value_m.group(1));
        }
        return value_d;
    }

    private String getDate(){
        String date;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime now = LocalDateTime.now();
        date =dtf.format(now);
        return date;
    }
}




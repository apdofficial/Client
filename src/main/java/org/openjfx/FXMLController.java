package org.openjfx;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXMLController class.
 *
 * Has multiple methods that receives data from the Client and shows it in the GUI
 *
 * @author Group 3
 * @version 0.2
 */

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

    // initializing graphs
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //disables legends for the graphs
        lineChart_T.setLegendVisible(false);
        lineChart_L.setLegendVisible(false);
        lineChart_H.setLegendVisible(false);
        lineChart_P.setLegendVisible(false);
        dateField.setText(getDate());


        //dummy data
        /*temperature_line.getData().add(new XYChart.Data( 05, 18));
        humidity_line.getData().add(new XYChart.Data( 05, 14));
        luminosity_line.getData().add(new XYChart.Data( 05, 10));
        pressure_line.getData().add(new XYChart.Data( 05, 7000));*/

    }

    // requesting temperature from the MQTT
    @FXML
    private void temperature(){
        temperature_text.setText("");
        temperature_text.setText(Double.toString(getValue("Temperature")));
    }

    // requesting luminosity from the MQTT
    @FXML
    private void luminosity(){
        luminosity_text.setText("");
        luminosity_text.setText(Double.toString(getValue("Luminosity")));
    }

    // requesting humidity from the MQTT
    @FXML
    private void humidity(){
        humidity_text.setText("");
        humidity_text.setText(Double.toString(getValue("Humidity")));
    }

    // requesting pressure from the MQTT
    @FXML
    private void pressure(){
        pressure_text.setText("");
        pressure_text.setText(Double.toString(getValue("Pressure")));
    }

    // requesting DeviceName from the MQTT
    @FXML
    private void getDeviceName(){
        String deviceNames= client.processRequest("DeviceNames");
        deviceName_text.setText("");
        deviceName_text.setText(deviceNames);
    }

    // refresh the data from the activated graphs
    @FXML
    private void refreshData(){
        getDeviceName();
        temperature();
        humidity();
        luminosity();
        pressure();
        if(checkBox_t.isSelected())temperatureLineChart();
        if(checkBox_h.isSelected())humidityLineChart();
        if(checkBox_l.isSelected())luminosityLineChart();
        if(checkBox_p.isSelected())pressureLineChart();

    }

    // update the temperature line chart
    @FXML
    private void temperatureLineChart(){

        if(checkBox_t.isSelected()){
            // add a new line to the existing graph
            temperature_line.getData().add(new XYChart.Data( getTime(), getValue("Temperature")));
            if(lineChart_T.getData().isEmpty()){
                // create a new dot in the graph
                lineChart_T.getData().add(temperature_line);
            }
        }
        else {
            lineChart_T.getData().remove(temperature_line);
        }
    }

    // update the humidity line chart
    @FXML
    private void humidityLineChart(){
        if(checkBox_h.isSelected()){
            // add a new line to the existing graph
            humidity_line.getData().add(new XYChart.Data( getTime(), getValue("Humidity")));
            if(lineChart_H.getData().isEmpty()){
                // create a new dot in the graph
                lineChart_H.getData().add(humidity_line);
            }
        }
        else {
            lineChart_H.getData().remove(humidity_line);
        }
    }

    // update the luminosity line chart
    @FXML
    private void luminosityLineChart(){
        if(checkBox_l.isSelected()){
            // add a new line to the existing graph
            luminosity_line.getData().add(new XYChart.Data( getTime(), getValue("Luminosity")));
            if(lineChart_L.getData().isEmpty()){
                // create a new dot in the graph
                lineChart_L.getData().add(luminosity_line);
            }
        }
        else {
            lineChart_L.getData().remove(luminosity_line);
        }
    }

    // update the pressure line chart
    @FXML
    private void pressureLineChart(){
        if(checkBox_p.isSelected()){
            // add a new line to the existing graph
            pressure_line.getData().add(new XYChart.Data( getTime(), getValue("Pressure")));
            if(lineChart_P.getData().isEmpty()){
                // create a new dot in the graph
                lineChart_P.getData().add(pressure_line);
            }
        }
        else {
            lineChart_P.getData().remove(pressure_line);
        }
    }

    // get the time and return the date format
    private double getTime(){
        double time_d= 0.0;
        // Retrieve actual date. dd:days    HH:hours    mm:minutes
        DateFormat dateFormat = new SimpleDateFormat("mm");
        Date date = new Date();
        Matcher time_m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(dateFormat.format(date));

        while (time_m.find())
        {
            time_d = Double.parseDouble(time_m.group(1));
        }
        return time_d;
    }

    // get the data in a double from the server in respect of doing a request
    private double getValue(String Request){
        double value_d=0.0;
        // processing the request and forwarding it to server
        String value= client.processRequest(Request);
        // deleting the brackets from the answer
        Matcher value_m = Pattern.compile("(?!=\\d\\.\\d\\.)([\\d.]+)").matcher(value);
        while (value_m.find())
        {
            value_d = Double.parseDouble(value_m.group(1));
        }
        return value_d;
    }

    // get the local date in a string (Year/Month/Day) format
    private String getDate(){
        String date;
        // create a data format
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        // get the local time
        LocalDateTime now = LocalDateTime.now();
        // convert data to the data format
        date =dtf.format(now);
        return date;
    }
    @FXML
    private void terminateProgram(){
        System.exit(0);
    }


}





/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartgrid.elements;

import java.io.IOException;
import javafx.beans.property.IntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import org.smartgrid.SmartGrid;
import org.smartgrid.SmartGridElement;
import org.smartgrid.events.SwapRowEvent;

/**
 * FXML Controller class
 *
 * @author rrrt3491
 */
public class MoveButtons extends HBox implements SmartGridElement {
 
    @FXML private Button buttonUp;
    @FXML private Button buttonDown;
    
    private final SmartGridProperties properties;
    private EventHandler moveHandler;
    
    public MoveButtons() {
        properties = new SmartGridProperties();
        setAlignment(Pos.CENTER);
        loadFXML();
        loadBindings();
        moveHandler = null;
    }
    
    private void loadFXML(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
        "MoveButtons.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    
    private void loadBindings(){
        
        properties.parentProperty().addListener((observable, oldGrid, grid) -> {
            if(grid != null && grid instanceof SmartGrid){
                bindButtons(grid);
            } else {
                unbindButtons();
            }
        });
    }

    private void bindButtons(SmartGrid grid){
        buttonUp.setOnAction((event -> moveUp()));
        buttonDown.setOnAction((event -> moveDown()));
        buttonUp.disableProperty().bind(properties.rowProperty().lessThanOrEqualTo(1));
        buttonDown.disableProperty().bind(properties.rowProperty().greaterThanOrEqualTo(grid.nbRowsProperty().subtract(1)));
    }
    
    private void unbindButtons(){
        buttonUp.setOnAction((event) -> {});
        buttonDown.setOnAction((event) -> {});
        buttonUp.disableProperty().unbind();
        buttonDown.disableProperty().unbind();
    }
    
    @FXML void moveUp(){
        Parent grid = getParent();
        if( grid instanceof SmartGrid){
            int row = SmartGrid.row(this);
            ((SmartGrid) grid).switchRows(row, row-1);
            moveHandler.handle(new SwapRowEvent(row, row-1));
        }
    }
    
    @FXML void moveDown(){
        Parent grid = getParent();
        if( grid instanceof SmartGrid){
            int row = SmartGrid.row(this);
            ((SmartGrid) grid).switchRows(row, row+1);
            moveHandler.handle(new SwapRowEvent(row, row+1));
        }
    }
    
    public Button getButtonUp() {
        return buttonUp;
    }

    public Button getButtonDown() {
        return buttonDown;
    }
    
    public void setOnMove(EventHandler<SwapRowEvent> handler){
        moveHandler = handler;
    }
    
    @Override
    public SmartGridProperties properties() {
        return properties;
    }
    
    public IntegerProperty rowProperty(){
        return properties.rowProperty();
    }
    
    public IntegerProperty colProperty(){
        return properties.colProperty();
    }
}

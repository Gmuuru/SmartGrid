/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartgrid;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextFlow;

/**
 *
 * @author rrrt3491
 */
public class SmartGrid extends GridPane {
    
    private final IntegerProperty nbColsProperty;
    private final IntegerProperty nbRowsProperty;
    
    private final IntegerProperty nbHeaderRows;
    
    public SmartGrid(){
        super();
        nbColsProperty = new SimpleIntegerProperty();
        nbRowsProperty = new SimpleIntegerProperty();
        nbHeaderRows = new SimpleIntegerProperty();
        
        getChildren().addListener((ListChangeListener.Change<? extends Node> c) -> {
            c.next();
            if(c.wasAdded() || c.wasRemoved()){
                if(getGridWidth() != nbColsProperty.get()){
                    nbColsProperty.set(getGridWidth());
                }
                if(getGridHeight() != nbColsProperty.get()){
                    nbRowsProperty.set(getGridHeight());
                }
            }
        });
                
    }
    
    @Override
    public void add(Node node, int colIndex, int rowIndex){
        super.add(node, colIndex, rowIndex);
        updateNodeAfterAdd(node, colIndex, rowIndex);
    }
    
    @Override
    public void add(Node node, int colIndex, int rowIndex, int colSpan, int rowSpan){
        super.add(node, colIndex, rowIndex, colSpan, rowSpan);
        updateNodeAfterAdd(node, colIndex, rowIndex);
    }
    
    private void updateNodeAfterAdd(Node node, int colIndex, int rowIndex){
        row(node, rowIndex);
        col(node, colIndex);
        if(node instanceof SmartGridElement){
            ((SmartGridElement) node).properties().parentProperty().set(this);
        }
    }
    
    public void add(Node node, int colIndex, int rowIndex, HPos alignment){
        add(node, colIndex, rowIndex);
        if(alignment != null){
            GridPane.setHalignment(node, alignment); // To align horizontally in the cell
        } else {
            GridPane.setHalignment(node, HPos.CENTER);
        }
        GridPane.setValignment(node, VPos.CENTER);
    }
    
    /**
     * Tries to add the Node in the last row of the grid. Places it on new line if cell already occupied
     * 
     * @param node
     * @param colIndex
     * @param alignment
     * @return the rowIndex of the line on which the object was inserted
     */
    public int addOnNewLineIfNecessary(Node node, int colIndex, HPos alignment){
        int rowIndex = getMaxRowIndex();
        if(get(colIndex, rowIndex) != null){
            rowIndex++;
        }
        add(node, colIndex, rowIndex, alignment);
        updateNodeAfterAdd(node, colIndex, rowIndex);
        return rowIndex;
    }
    
    /**
     * ajoute un noeud
     * @param node
     * @param colIndex
     * @param rowIndex
     * @param colSpan
     * @param rowSpan
     * @param alignment 
     */
    public void add(Node node, int colIndex, int rowIndex, int colSpan, int rowSpan, HPos alignment){
        add(node, colIndex, rowIndex, colSpan, rowSpan);
        if(alignment != null){
            GridPane.setHalignment(node, alignment); // To align horizontally in the cell
        } else {
            GridPane.setHalignment(node, HPos.CENTER);
        }
        GridPane.setValignment(node, VPos.CENTER);
    }
    
    public int getGridWidth(){
        int width = -1;
        for(Node c : getChildren()){
            width = Math.max(width, col(c));
        }
        return width+1;
    }
    
    public int getMaxRowIndex(){
        int maxRow = 0;
        for(Node c : getChildren()){
            maxRow = Math.max(maxRow, row(c));
        }
        return maxRow;
    }
    
    public int getGridHeight(){
        int height = -1;
        for(Node c : getChildren()){
            height = Math.max(height, row(c));
        }
        return height+1;
    }
    
    public Node get(int col, int row) {
        for (Node node : getChildren()) {
            if (col(node) == col && row(node) == row) {
                return node;
            }
        }
        return null;
    }
    
    public void remove(int col, int row){
        remove(get(col, row));
        if(getRowConstraints() != null && getRowConstraints().size() > row){
            getRowConstraints().remove(row);
        }
    }
    
    public void remove(Node node){
        if(node == null){
            return;
        }
        getChildren().remove(node);
        if(node instanceof SmartGridElement){
            ((SmartGridElement) node).properties().parentProperty().set(null);
        }
    }
    
    public List<Node> getRow(int rowIndex){
        return getChildren().stream()
            .filter((node) -> node != null && row(node) == rowIndex)
            .collect(Collectors.toList());
    }
    
    public List<List<Node>> getDataRows(){
        List<List<Node>> res = new ArrayList<>();
        for(int row = getNbHeaderRows(); row <= getMaxRowIndex(); row++){
            res.add(getRow(row));
        }
        return res;
    }
    
    public void deleteRow(int rowIndex){
        getChildren().stream()
            .filter((node) -> node != null && row(node) == rowIndex)
            .collect(Collectors.toList())
            .forEach(node -> remove(node));
        getChildren().stream()
             .filter((node) -> node != null && row(node) > rowIndex)
             .forEach((node) -> row(node, row(node) -1));
    }
    
    public void switchRows(int sourceIndex, int destIndex){
        getChildren().stream()
                .filter((node) -> node != null && (row(node) == sourceIndex || row(node) == destIndex))
                .forEach(node -> {
                    if(row(node) == sourceIndex) {
                        row(node, destIndex);
                    } else {
                        row(node, sourceIndex);
                    }
                });
    }
    
    public void clear(){
        
        int nbheaders = nbHeaderRows.get();
        getChildren().stream()
            .filter((node) -> node != null && row(node) >= nbheaders)
            .collect(Collectors.toList())
            .forEach(node -> remove(node));
        int j = getRowConstraints() == null ? -1 : getRowConstraints().size()-1;
        while(j >= nbheaders){
            getRowConstraints().remove(j);
            j--;
        }
    }
    
    public void drawNoDataInGrid(String text){
        clear();int width = nbColsProperty.get();
        if(width == 0){
             width = getColumnConstraints().size();
        }
        
        add(new Label(text), 0, nbHeaderRows.get(), width, 1, HPos.CENTER);
    }
    
    /**
     * clears the grid and writes each message on a row, starting at first non header row 
     * @param messageNodes 
     */
    public void drawMessage(Node... messageNodes){
        drawMessage(Arrays.asList(messageNodes));
    }
    
    /**
     * clears the grid and writes each message on a row, starting at first non header row 
     * @param messageNodes 
     */
    public void drawMessage(List<Node> messageNodes){
        clear();
        int width = nbColsProperty.get();
        if(width == 0){
             width = getColumnConstraints().size();
        }
        for(int i = 0; i < messageNodes.size(); i++){
            add(messageNodes.get(i), 0, nbHeaderRows.get()+i, width, 1, HPos.LEFT);
        }
    }
    
    public void drawNoClasseAlert(EventHandler<ActionEvent> handler){
        Label alert = new Label("Aucune classe définie");
        Hyperlink openMenu = new Hyperlink("Menu de gestion des classes");
        openMenu.setOnAction(handler);
        Label indication = new Label(". Ajoutez une classe dans le ");
        TextFlow flow = new TextFlow(alert, indication, openMenu);
        drawMessage(flow);
    }
    
    public void drawNoEleveAlert(String classeName, EventHandler<ActionEvent> handler){
        Label alert = new Label("Aucun élève dans la classe "+classeName);
        Hyperlink openMenu = new Hyperlink("Menu de gestion des élèves");
        openMenu.setOnAction(handler);
        Label indication = new Label(". Ajoutez un élève dans le ");
        TextFlow flow = new TextFlow(alert, indication, openMenu);
        drawMessage(flow);
    }
    
    public void drawNoSeanceAlert(String currentDay, EventHandler<ActionEvent> handler){
        
        Label alert = new Label("Aucune séance le "+currentDay);
        
        Hyperlink openMenu = new Hyperlink("Emploi du temps");
        openMenu.setOnAction(handler);
        Label indication = new Label(". Ajoutez un cours dans l' ");
        Label indication2 = new Label("Vous pouvez aussi utiliser le bouton d'ajout de cours ponctuel");
        TextFlow flow = new TextFlow(alert, indication, openMenu);
        drawMessage(flow, indication2);
    }
    
    public void drawNoCoursAlert(String currentDay, String classeName, EventHandler<ActionEvent> handler){
        Label alert = new Label("Aucun cours défini pour la "+classeName
                +" le "+currentDay);
        
        Hyperlink openMenu = new Hyperlink("Emploi du temps");
        openMenu.setOnAction(handler);
        Label indication = new Label(". Ajoutez un cours dans l' ");
        Label indication2 = new Label("Vous pouvez aussi utiliser le bouton 'Ajouter cours' pour ajouter un cours ponctuel");
        TextFlow flow = new TextFlow(alert, indication, openMenu);
        drawMessage(flow, indication2);
    }
    
    public void drawNoTrimestreAlert(LocalDate date, EventHandler<ActionEvent> handler){
        clear();
        int width = nbColsProperty.get();
        if(width == 0){
            width = getColumnConstraints().size();
        }
        Label alert = new Label("Aucun trimestre défini"+ ((date != null)?" pour le "+date:""));
        Hyperlink openMenu = new Hyperlink("Menu de gestion des trimestres");
        openMenu.setOnAction(handler);
        Label indication = new Label(". Ajoutez un trimestre dans le ");
        TextFlow flow = new TextFlow(alert, indication, openMenu);
        add(flow, 0, nbHeaderRows.get(), width, 1, HPos.LEFT);
    }

    public IntegerProperty nbColsProperty() {
        return nbColsProperty;
    }

    public IntegerProperty nbRowsProperty() {
        return nbRowsProperty;
    }
    
    public static Integer getColumnIndex(Node node){
        return col(node);
    }
    
    public static int col(Node node){
        return GridPane.getColumnIndex(node) == null ? -1 : GridPane.getColumnIndex(node);
    }
    
    public static Integer getRowIndex(Node node){
        return row(node);
    }
            
    public static int row(Node node){
        return GridPane.getRowIndex(node) == null ? -1 : GridPane.getRowIndex(node);
    }
    
    public static void setColumnIndex(Node node, Integer index){
        col(node, index);
    }
    
    public static Node col(Node node, int index){
        GridPane.setColumnIndex(node, index);
        if(node instanceof SmartGridElement){
            ((SmartGridElement) node).properties().colProperty().set(index);
        }
        return node;
    }
    
    public static void setRowIndex(Node node, Integer index){
        row(node, index);
    }
    
    public static Node row(Node node, int index){
        GridPane.setRowIndex(node, index);
        if(node instanceof SmartGridElement){
            ((SmartGridElement) node).properties().rowProperty().set(index);
        }
        return node;
    }

    public Integer getNbHeaderRows() {
        return nbHeaderRows.get();
    }
    
    public void setNbHeaderRows(Integer nbHeaderRows){
        this.nbHeaderRows.set(nbHeaderRows);
    }
    
    
}

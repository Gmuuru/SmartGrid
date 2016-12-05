/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartgrid.elements;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.smartgrid.SmartGrid;

/**
 *
 * @author rrrt3491
 */
public class SmartGridProperties {
    
    IntegerProperty rowProperty;
    IntegerProperty colProperty;
    ObjectProperty<SmartGrid> parentProperty;
    
    public SmartGridProperties(){
        rowProperty = new SimpleIntegerProperty();
        colProperty = new SimpleIntegerProperty();
        parentProperty = new SimpleObjectProperty<>();
    }
    
    public IntegerProperty rowProperty(){
        return rowProperty;
    }
    
    public IntegerProperty colProperty(){
        return colProperty;
    }
    
    public ObjectProperty<SmartGrid> parentProperty(){
        return parentProperty;
    }

    public void setRowProperty(int row) {
        this.rowProperty.set(row);
    }

    public void setColProperty(int col) {
        this.colProperty.set(col);
    }

    public void setParentProperty(SmartGrid grid) {
        this.parentProperty.set(grid);
    }
    
}

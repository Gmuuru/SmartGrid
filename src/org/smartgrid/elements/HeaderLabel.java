/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartgrid.elements;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;
import org.smartgrid.SmartGrid;
import org.smartgrid.SmartGridElement;

/**
 *
 * @author rrrt3491
 */
public class HeaderLabel extends Label implements SmartGridElement{

    private int fixedHeight = 35;
    
    private final SmartGridProperties properties;
    
    public HeaderLabel(){
        super();
        properties = new SmartGridProperties();
        initRowIndex();
        setTextAlignment(TextAlignment.CENTER);
        setAlignment(Pos.CENTER);
        setWrapText(true);
        setMaxWidth(Double.MAX_VALUE);
        setBounds();
        setText("");
    }
    
    private void initRowIndex(){
        SmartGrid.row(this, 0);
    }
    
    private void setBounds(){
        setMinSize(getMinWidth(), fixedHeight);
        setPrefSize(getPrefWidth(), fixedHeight);
        setMaxSize(getMaxWidth(), fixedHeight);
    }

    public int getFixedHeight() {
        return fixedHeight;
    }

    public void setFixedHeight(int fixedHeight) {
        this.fixedHeight = fixedHeight;
        this.setBounds();
    }

    @Override
    public SmartGridProperties properties() {
        return properties;
    }
    
}

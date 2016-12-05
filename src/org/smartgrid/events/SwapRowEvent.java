/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smartgrid.events;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author rrrt3491
 */
public class SwapRowEvent extends Event {

    private int sourceRow;
    private int destRow;

    public static final EventType<SwapRowEvent> CUSTOM = new EventType(ANY, "CUSTOM");
    
    public SwapRowEvent(int source, int dest) {
        super(SwapRowEvent.CUSTOM);
        this.sourceRow = source;
        this.destRow = dest;
    }

    public int getSourceRow() {
        return sourceRow;
    }

    public int getDestRow() {
        return destRow;
    }
    

}


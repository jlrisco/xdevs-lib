/*
 * Copyright (C) 2014-2016 José Luis Risco Martín <jlrisco@ucm.es>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package xdevs.lib.general.sources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.OutPort;

/**
 *
 * @author jlrisco
 */
public class StimulusFile extends Atomic {
    
    public class Event {

        protected Double time;
        protected String portName;
        protected Object value;
    }

    private static final Logger logger = Logger.getLogger(StimulusFile.class.getName());
    protected HashMap<String, OutPort> myOutPorts = new HashMap<>();
    protected HashMap<String, Class> myValueTypes = new HashMap<>();

    protected BufferedReader stimulusFile;
    protected Event currentEvent = null;
    protected Event nextEvent = null;
    protected LinkedList<Event> events = new LinkedList<>();

    public StimulusFile(String name, String stimulusFilePath) {
        super(name);
        try {
            stimulusFile = new BufferedReader(new FileReader(new File(stimulusFilePath)));
            // First we read all the output ports
            String portInfo;
            while ((portInfo = stimulusFile.readLine()) != null) {
                if (portInfo.startsWith("# END PORTS")) {
                    break;
                } else if (!portInfo.startsWith("#")) {
                    String[] portNameAndType = portInfo.split(":");
                    Class valueType = Class.forName(portNameAndType[1]);
                    OutPort port = new OutPort(portNameAndType[0]);
                    super.addOutPort(port);
                    myOutPorts.put(portNameAndType[0], port);
                    myValueTypes.put(portNameAndType[0], valueType);
                }
            }
        } catch (IOException ex) {
            stimulusFile = null;
            logger.log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize() {
        currentEvent = readNextEvent();
        if (currentEvent != null) {
            events.add(currentEvent);
            nextEvent = readNextEvent();
            while (nextEvent != null && !(nextEvent.time > currentEvent.time)) {
                events.add(nextEvent);
                nextEvent = readNextEvent();
            }
            super.holdIn("active", currentEvent.time);
        } else {
            super.passivate();
        }
    }

    @Override
    public void exit() {
        try {
            stimulusFile.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deltint() {
        events.clear();
        if (nextEvent != null) {
            double elapsedTime = nextEvent.time - currentEvent.time;
            events.add(nextEvent);
            currentEvent = nextEvent;
            nextEvent = readNextEvent();
            while (nextEvent != null && !(nextEvent.time > currentEvent.time)) {
                events.add(nextEvent);
                nextEvent = readNextEvent();
            }
            super.holdIn("active", elapsedTime);
        } else {
            super.passivate();
        }
    }

    @Override
    public void deltext(double e) {
    }

    @Override
    public void lambda() {
        for (Event event : events) {
            myOutPorts.get(event.portName).addValue(event.value);
        }
    }

    public OutPort<Number> getPortByName(String portName) {
        OutPort<Number> port = myOutPorts.get(portName);
        return port;
    }

    private Event readNextEvent() {
        Event event = null;
        // We take the next event
        try {
            String currentLine = stimulusFile.readLine();
            while (currentLine != null) {
                if (!currentLine.startsWith("#")) {
                    event = new Event();
                    String[] parts = currentLine.split(";");
                    String[] timeParts = parts[0].split(":");
                    String[] millisParts = timeParts[2].split("\\.");
                    event.time = 60.0 * 60 * Integer.parseInt(timeParts[0]);
                    event.time += 60 * Integer.parseInt(timeParts[1]);
                    event.time += Integer.parseInt(millisParts[0]);
                    event.time += Integer.parseInt(millisParts[1]) / 1000.0;
                    event.portName = parts[1].replaceAll(" ", "");
                    Class valueType = myValueTypes.get(event.portName);
                    event.value = valueType.getConstructor(String.class).newInstance(parts[2]);
                    return event;
                }
                currentLine = stimulusFile.readLine();
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(StimulusFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(StimulusFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(StimulusFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(StimulusFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(StimulusFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(StimulusFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return event;
    }

}

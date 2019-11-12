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
package xdevs.lib.examples.performance;

import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Level;
import xdevs.core.modeling.Coupled;
import xdevs.core.modeling.Port;
import xdevs.core.simulation.Coordinator;
import xdevs.core.util.DevsLogger;
import java.util.logging.Logger;
import xdevs.core.modeling.Component;
import xdevs.core.modeling.Coupling;

/**
 * Coupled model to study the performance using DEVStone
 *
 * @author José Luis Risco Martín
 */
public abstract class DevStoneCoupled extends Coupled {

    private static final Logger LOGGER = Logger.getLogger(DevStoneCoupled.class.getName());

    public static enum BenchmarkType {
        LI, HI, HO, HOmem, HOmod
    };

    public Port<Integer> iIn = new Port<>("in");
    public Port<Integer> oOut = new Port<>("out");

    public DevStoneCoupled(String name) {
        super(name);
        super.addInPort(iIn);
        super.addOutPort(oOut);
    }

    public abstract int getNumDeltExts(int maxEvents, int width, int depth);

    public abstract int getNumDeltInts(int maxEvents, int width, int depth);

    public abstract long getNumOfEvents(int maxEvents, int width, int depth);

    public static void main(String[] args) {
        // Benchmark type and parameters
        BenchmarkType benchmarkType = BenchmarkType.HO;
        int width = 5;
        int depth = 5;
        double intDelayTime = 0.1;
        double extDelayTime = 0.1;

        // Generator parameters:
        double preparationTime = 0.0;
        double period = 1.0;
        int maxEvents = 1;

        // Atomic number of internal and external transitions, as well as number of events
        DevStoneAtomic.NUM_DELT_INTS = 0;
        DevStoneAtomic.NUM_DELT_EXTS = 0;
        DevStoneAtomic.NUM_OF_EVENTS = 0;

        Coupled framework = new Coupled("DevStone" + benchmarkType.toString());
        DevStoneGenerator generator = new DevStoneGenerator("Generator", preparationTime, period, maxEvents);
        framework.addComponent(generator);

        DevStoneCoupled stoneCoupled;
        switch (benchmarkType) {
            case LI:
                stoneCoupled = new DevStoneCoupledLI("C", width, depth, preparationTime, intDelayTime, extDelayTime);
                break;
            case HI:
                stoneCoupled = new DevStoneCoupledHI("C", width, depth, preparationTime, intDelayTime, extDelayTime);
                break;
            case HO:
                stoneCoupled = new DevStoneCoupledHO("C", width, depth, preparationTime, intDelayTime, extDelayTime);
                break;
            case HOmem:
                stoneCoupled = new DevStoneCoupledHOmem("C", width, depth, preparationTime, intDelayTime, extDelayTime);
                break;
            case HOmod:
                stoneCoupled = new DevStoneCoupledHOmod("C", width, depth, preparationTime, intDelayTime, extDelayTime);
                break;
            default:
                stoneCoupled = new DevStoneCoupledLI("C", width, depth, preparationTime, intDelayTime, extDelayTime);
                break;
        }

        // Theoretical values
        int numDeltInts = stoneCoupled.getNumDeltInts(maxEvents, width, depth);
        int numDeltExts = stoneCoupled.getNumDeltExts(maxEvents, width, depth);
        long numOfEvents = stoneCoupled.getNumOfEvents(maxEvents, width, depth);

        framework.addComponent(stoneCoupled);
        framework.addCoupling(generator.oOut, stoneCoupled.iIn);
        switch (benchmarkType) {
            case HO:
                framework.addCoupling(generator.oOut, ((DevStoneCoupledHO) stoneCoupled).iInAux);
                break;
            case HOmem:
                framework.addCoupling(generator.oOut, ((DevStoneCoupledHOmem) stoneCoupled).iInAux);
                break;
            case HOmod:
                framework.addCoupling(generator.oOut, ((DevStoneCoupledHOmod) stoneCoupled).iInAux);
                break;
            default:
                break;
        }

        Coordinator coordinator = new Coordinator(framework, false);
        coordinator.initialize();
        long start = System.currentTimeMillis();
        DevsLogger.setup(Level.INFO);
        coordinator.simulate(Long.MAX_VALUE);
        long end = System.currentTimeMillis();
        double time = (end - start) / 1000.0;
        LOGGER.info("MAXEVENTS;WIDTH;DEPTH;NUM_DELT_INTS;NUM_DELT_EXTS;NUM_OF_EVENTS;TIME");
        String stats;
        if (DevStoneAtomic.NUM_DELT_INTS == numDeltInts && DevStoneAtomic.NUM_DELT_EXTS == numDeltExts && DevStoneAtomic.NUM_OF_EVENTS == numOfEvents) {
            stats = maxEvents + ";" + width + ";" + depth + ";" + DevStoneAtomic.NUM_DELT_INTS + ";" + DevStoneAtomic.NUM_DELT_EXTS + ";" + DevStoneAtomic.NUM_OF_EVENTS + ";" + time;
        } else {
            stats = "ERROR: NumDeltInts or NumDeltExts or NumOfEvents do not match the theoretical values (between brackets): " + DevStoneAtomic.NUM_DELT_INTS + ";[" + numDeltInts + "];" + DevStoneAtomic.NUM_DELT_EXTS + ";[" + numDeltExts + "];" + DevStoneAtomic.NUM_OF_EVENTS + ";[" + numOfEvents + "];" + time;
        }
        LOGGER.info(stats);
    }
    
    public String toXML(int level) {
        StringBuilder tabs = new StringBuilder();
        for(int i=0; i<level; ++i) {
            tabs.append("\t");
        }
        StringBuilder builder = new StringBuilder();
        if(level==0) {
            builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
        }
        builder.append(tabs).append("<coupled name=\"").append(super.getName()).append("\">\n");
        // Components
        Collection<Component> components = super.getComponents();
        components.forEach((component) -> {
            if(component instanceof Coupled) {
                int levelAux = level + 1;
                builder.append(((DevStoneCoupled)component).toXML(levelAux));
            }
            else {
                builder.append(tabs).append("\t<atomic name=\"").append(component.getName()).append("\"/>\n");
            }
        });
        // Couplings
        LinkedList<Coupling<?>> couplings = super.getEIC();
        couplings.forEach((coupling) -> {
            builder.append(tabs).append("\t<connection ");
            builder.append("componentFrom=\"").append(coupling.getPortFrom().getParent().getName()).append("\" portFrom=\"").append(coupling.getPortFrom().getName());
            builder.append("\" componentTo=\"").append(coupling.getPortTo().getParent().getName()).append("\" portTo=\"").append(coupling.getPortTo().getName()).append("\"/>\n");
        });
        couplings = super.getIC();
        couplings.forEach((coupling) -> {
            builder.append(tabs).append("\t<connection ");
            builder.append("componentFrom=\"").append(coupling.getPortFrom().getParent().getName()).append("\" portFrom=\"").append(coupling.getPortFrom().getName());
            builder.append("\" componentTo=\"").append(coupling.getPortTo().getParent().getName()).append("\" portTo=\"").append(coupling.getPortTo().getName()).append("\"/>\n");
        });
        couplings = super.getEOC();
        couplings.forEach((coupling) -> {
            builder.append(tabs).append("\t<connection ");
            builder.append("componentFrom=\"").append(coupling.getPortFrom().getParent().getName()).append("\" portFrom=\"").append(coupling.getPortFrom().getName());
            builder.append("\" componentTo=\"").append(coupling.getPortTo().getParent().getName()).append("\" portTo=\"").append(coupling.getPortTo().getName()).append("\"/>\n");
        });
        builder.append(tabs).append("</coupled>\n");
        return builder.toString();
    }
}

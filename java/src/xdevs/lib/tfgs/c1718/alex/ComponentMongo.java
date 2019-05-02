package xdevs.lib.tfgs.c1718.alex;

import java.util.ArrayList;
import java.util.Collection;

public abstract class ComponentMongo {

    // Component attributes
    protected ComponentMongo parent = null;
    protected String name;
    protected ArrayList<PortMongo<?>> inPorts = new ArrayList<>();
    protected ArrayList<PortMongo<?>> outPorts = new ArrayList<>();

    public ComponentMongo(String name) {
        this.name = name;

    }

    public ComponentMongo() {
        this(ComponentMongo.class.getSimpleName());
    }

    public String getName() {
        return name;
    }

    public abstract void initialize();

    public abstract void exit();

    public boolean isInputEmpty() {
        for (PortMongo<?> port : inPorts) {
            if (!port.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void addInPort(PortMongo<?> port) {
        inPorts.add(port);
        port.parent = this;
    }

    public PortMongo<?> getInPort(String portName) {
        for (PortMongo<?> port : inPorts) {
            if (port.name.equals(portName)) {
                return port;
            }
        }
        return null;
    }

    public Collection<PortMongo<?>> getInPorts() {
        return inPorts;
    }

    public void addOutPort(PortMongo<?> port) {
        outPorts.add(port);
        port.parent = this;
    }

    public PortMongo<?> getOutPort(String portName) {
        for (PortMongo<?> port : outPorts) {
            if (port.name.equals(portName)) {
                return port;
            }
        }
        return null;
    }

    public Collection<PortMongo<?>> getOutPorts() {
        return outPorts;
    }

    public ComponentMongo getParent() {
        return parent;
    }

    public void setParent(ComponentMongo parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name + " :");
        sb.append(" Inports[ ");
        for (PortMongo<?> p : inPorts) {
            sb.append(p).append(" ");
        }
        sb.append("]");
        sb.append(" Outports[ ");
        for (PortMongo<?> p : outPorts) {
            sb.append(p).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}

package xdevs.lib.tfgs.c1718.alex;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Logger;

public class CoupledMongo extends ComponentMongo {

    private static final Logger LOGGER = Logger.getLogger(CoupledMongo.class.getName());

    // Coupled attributes
    protected LinkedList<ComponentMongo> components = new LinkedList<>();
    protected LinkedList<CouplingMongo<?>> ic = new LinkedList<>();
    protected LinkedList<CouplingMongo<?>> eic = new LinkedList<>();
    protected LinkedList<CouplingMongo<?>> eoc = new LinkedList<>();

    public CoupledMongo(String name) {
        super(name);
    }

    public CoupledMongo() {
        this(CoupledMongo.class.getSimpleName());
    }

    @Override
    public void initialize() {
    }

    @Override
    public void exit() {
    }

    @Override
    public ComponentMongo getParent() {
        return parent;
    }

    @Override
    public void setParent(ComponentMongo parent) {
        this.parent = parent;
    }

    /**
     * This method add a connection to the DEVS component.
     *
     * @param cFrom      Component at the beginning of the connection
     * @param oPortIndex Index of the source port in cFrom, starting at 0
     * @param cTo        Component at the end of the connection
     * @param iPortIndex Index of the destination port in cTo, starting at 0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void addCoupling(ComponentMongo cFrom, int oPortIndex, ComponentMongo cTo, int iPortIndex) {
        if (cFrom == this) { // EIC
            PortMongo portFrom = cFrom.inPorts.get(oPortIndex);
            PortMongo portTo = cTo.inPorts.get(iPortIndex);
            CouplingMongo coupling = new CouplingMongo(portFrom, portTo);
            eic.add(coupling);
        } else if (cTo == this) { // EOC
            PortMongo portFrom = cFrom.outPorts.get(oPortIndex);
            PortMongo portTo = cTo.outPorts.get(iPortIndex);
            CouplingMongo coupling = new CouplingMongo(portFrom, portTo);
            eoc.add(coupling);
        } else { // IC
            PortMongo portFrom = cFrom.outPorts.get(oPortIndex);
            PortMongo portTo = cTo.inPorts.get(iPortIndex);
            CouplingMongo coupling = new CouplingMongo(portFrom, portTo);
            ic.add(coupling);
        }
    }

    /**
     * @param cFrom Component at the beginning of the connection
     * @param pFrom Port at the beginning of the connection
     * @param cTo   Component at the end of the connection
     * @param pTo   Port at the end of the connection
     * @deprecated This method add a connection to the DEVS component. This
     * method is deprecated because since the addition of the
     * <code>parent</code> attribute, both components <code>cFrom</code> and
     * <code>cTo</code> are no longer needed inside the Coupling class.
     */
    @SuppressWarnings("unchecked")
    public void addCoupling(ComponentMongo cFrom, PortMongo<?> pFrom, ComponentMongo cTo, PortMongo<?> pTo) {
        @SuppressWarnings({"rawtypes"})
        CouplingMongo coupling = new CouplingMongo(pFrom, pTo);
        // Add to connections
        if (cFrom == this) {
            eic.add(coupling);
        } else if (cTo == this) {
            eoc.add(coupling);
        } else {
            ic.add(coupling);
        }
    }

    public final void addCoupling(String cFromName, String pFromName, String cToName, String pToName) {
        ComponentMongo cFrom = this.getComponentByName(cFromName);
        ComponentMongo cTo = this.getComponentByName(cToName);
        if (cFrom == null) {
            LOGGER.severe(cFromName + " does not exist");
            return;
        }
        if (cTo == null) {
            LOGGER.severe(cToName + " does not exist");
            return;
        }
        PortMongo<?> pFrom;
        PortMongo<?> pTo;
        // Add to connections
        if (cFrom == this) {
            pFrom = cFrom.getInPort(pFromName);
            pTo = cTo.getInPort(pToName);
        } else if (cTo == this) {
            pFrom = cFrom.getOutPort(pFromName);
            pTo = cTo.getOutPort(pToName);
        } else {
            pFrom = cFrom.getOutPort(pFromName);
            pTo = cTo.getInPort(pToName);
        }
        if (pFrom == null) {
            LOGGER.severe(cFrom.getName() + "::" + pFromName + "->" + cTo.getName() + "::" + pToName + " --> port " + pFromName + " at component " + cFrom.getName() + " does not exist");
            return;
        }
        if (pTo == null) {
            LOGGER.severe(cFrom.getName() + "::" + pFromName + "->" + cTo.getName() + "::" + pToName + " --> port " + pToName + " at component " + cTo.getName() + " does not exist");
            return;
        }
        this.addCoupling(pFrom, pTo);
    }

    /**
     * This member adds a connection between ports pFrom and pTo
     *
     * @param pFrom Port at the beginning of the connection
     * @param pTo   Port at the end of the connection
     */
    @SuppressWarnings("unchecked")
    public void addCoupling(PortMongo<?> pFrom, PortMongo<?> pTo) {
        @SuppressWarnings({"rawtypes"})
        CouplingMongo coupling = new CouplingMongo(pFrom, pTo);
        // Add to connections
        if (pFrom.getParent() == this) {
            eic.add(coupling);
        } else if (pTo.getParent() == this) {
            eoc.add(coupling);
        } else {
            ic.add(coupling);
        }
    }

    public Collection<ComponentMongo> getComponents() {
        return components;
    }

    /**
     * Get the first component (including this coupled model) whose name match
     * with name argument.
     *
     * @param name The name of the component to find
     * @return The component, which name is equal to the argument. If no
     * component is found, null is returned.
     */
    public ComponentMongo getComponentByName(String name) {
        if (this.name.equals(name)) {
            return this;
        }

        for (ComponentMongo component : components) {
            if (component.name.equals(name)) {
                return component;
            }
        }

        return null;
    }

    public final void addComponent(ComponentMongo component) {
        component.setParent(this);
        components.add(component);
    }

    public LinkedList<CouplingMongo<?>> getIC() {
        return ic;
    }

    public LinkedList<CouplingMongo<?>> getEIC() {
        return eic;
    }

    public LinkedList<CouplingMongo<?>> getEOC() {
        return eoc;
    }

    /**
     * @return The new DEVS coupled model
     */
    public CoupledMongo flatten() {
        for (int i = 0; i < components.size(); ++i) {
            ComponentMongo component = components.get(i);
            if (component instanceof CoupledMongo) {
                ((CoupledMongo) component).flatten();
                removePortsAndCouplings(component);
                components.remove(i--);
            }
        }

        if (parent == null) {
            return this;
        }

        // Process if parent ...
        // First, we store all the parent ports connected to input ports
        HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> leftBridgeEIC = createLeftBrige(((CoupledMongo) parent).getEIC());
        HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> leftBridgeIC = createLeftBrige(((CoupledMongo) parent).getIC());
        // The same with the output ports
        HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> rightBridgeEOC = createRightBrige(((CoupledMongo) parent).getEOC());
        HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> rightBridgeIC = createRightBrige(((CoupledMongo) parent).getIC());

        completeLeftBridge(eic, leftBridgeEIC, ((CoupledMongo) parent).getEIC());
        completeLeftBridge(eic, leftBridgeIC, ((CoupledMongo) parent).getIC());
        completeRightBridge(eoc, rightBridgeEOC, ((CoupledMongo) parent).getEOC());
        completeRightBridge(eoc, rightBridgeIC, ((CoupledMongo) parent).getIC());

        for (ComponentMongo component : components) {
            ((CoupledMongo) parent).addComponent(component);
        }

        for (CouplingMongo<?> cIC : ic) {
            ((CoupledMongo) parent).getIC().add(cIC);
        }
        return this;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void completeLeftBridge(LinkedList<CouplingMongo<?>> couplings,
                                    HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> leftBridge,
                                    LinkedList<CouplingMongo<?>> pCouplings) {
        for (CouplingMongo<?> c : couplings) {
            LinkedList<PortMongo<?>> list = leftBridge.get(c.portFrom);
            if (list != null) {
                for (PortMongo<?> port : list) {
                    pCouplings.add(new CouplingMongo(port, c.portTo));
                }
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void completeRightBridge(LinkedList<CouplingMongo<?>> couplings,
                                     HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> rightBridge,
                                     LinkedList<CouplingMongo<?>> pCouplings) {
        for (CouplingMongo<?> c : couplings) {
            LinkedList<PortMongo<?>> list = rightBridge.get(c.portTo);
            if (list != null) {
                for (PortMongo<?> port : list) {
                    pCouplings.add(new CouplingMongo(c.portFrom, port));
                }
            }
        }
    }

    private HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> createLeftBrige(LinkedList<CouplingMongo<?>> couplings) {
        HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> leftBridge = new HashMap<>();
        for (PortMongo<?> iPort : this.inPorts) {
            for (CouplingMongo<?> c : couplings) {
                if (c.portTo == iPort) {
                    LinkedList<PortMongo<?>> list = leftBridge.get(iPort);
                    if (list == null) {
                        list = new LinkedList<>();
                        leftBridge.put(iPort, list);
                    }
                    list.add(c.portFrom);
                }
            }
        }
        return leftBridge;
    }

    private HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> createRightBrige(LinkedList<CouplingMongo<?>> couplings) {
        HashMap<PortMongo<?>, LinkedList<PortMongo<?>>> rightBridge = new HashMap<>();
        for (PortMongo<?> oPort : this.outPorts) {
            for (CouplingMongo<?> c : couplings) {
                if (c.portFrom == oPort) {
                    LinkedList<PortMongo<?>> list = rightBridge.get(oPort);
                    if (list == null) {
                        list = new LinkedList<>();
                        rightBridge.put(oPort, list);
                    }
                    list.add(c.portTo);
                }
            }
        }
        return rightBridge;
    }

    private void removePortsAndCouplings(ComponentMongo child) {
        Collection<PortMongo<?>> inPorts = child.getInPorts();
        for (PortMongo<?> iport : inPorts) {
            for (int j = 0; j < eic.size(); ++j) {
                CouplingMongo<?> c = eic.get(j);
                if (c.portTo == iport) {
                    eic.remove(j--);
                }
            }
            for (int j = 0; j < ic.size(); ++j) {
                CouplingMongo<?> c = ic.get(j);
                if (c.portTo == iport) {
                    ic.remove(j--);
                }
            }
        }
        Collection<PortMongo<?>> outPorts = child.getOutPorts();
        for (PortMongo<?> oport : outPorts) {
            for (int j = 0; j < eoc.size(); ++j) {
                CouplingMongo<?> c = eoc.get(j);
                if (c.portFrom == oport) {
                    eoc.remove(j--);
                }
            }
            for (int j = 0; j < ic.size(); ++j) {
                CouplingMongo<?> c = ic.get(j);
                if (c.portFrom == oport) {
                    ic.remove(j--);
                }
            }
        }
    }

}

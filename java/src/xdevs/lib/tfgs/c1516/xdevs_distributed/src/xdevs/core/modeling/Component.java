/*
 * Copyright (C) 2014-2015 José Luis Risco Martín <jlrisco@ucm.es> and 
 * Saurabh Mittal <smittal@duniptech.com>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, see
 * http://www.gnu.org/licenses/
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package xdevs.core.modeling;

import java.util.LinkedList;

import xdevs.core.modeling.api.ComponentInterface;

/**
 * @author smittal
 *
 */
public abstract class Component extends Entity implements ComponentInterface {

	private static final long serialVersionUID = -2665060621560114744L;
	// Component attributes
	protected ComponentInterface parent = null;
	protected LinkedList<InPort<?>> inPorts = new LinkedList<>();
	protected LinkedList<OutPort<?>> outPorts = new LinkedList<>();

	public Component(){
		this(Component.class.getSimpleName());
	}
	
	public Component(String name){
		super(name);
	}

        @Override
	public boolean isInputEmpty() {
		for (InPort<?> port : inPorts) {
			if (!port.isEmpty()) {
				return false;
			}
		}
		return true;
	}

        @Override
	public void addInPort(InPort<?> port) {
		inPorts.add(port);
		port.parent = this;
	}

        @Override
	public LinkedList<InPort<?>> getInPorts() {
		return inPorts;
	}
    public void setInPorts(LinkedList<InPort<?>> in) {
		this.inPorts = in;
	}
        @Override
	public void addOutPort(OutPort<?> port) {
		outPorts.add(port);
		port.parent = this;
	}

        @Override
	public LinkedList<OutPort<?>> getOutPorts() {
		return outPorts;
	}
    
    public void setOutPorts(LinkedList<OutPort<?>> out) {
		this.outPorts = out;
	}

	@Override
	public ComponentInterface getParent() {
		return parent;
	}

	@Override
	public void setParent(ComponentInterface parent) {
		this.parent = parent;
	}

	public String toString(){
		StringBuilder sb = new StringBuilder(name + " :");
		sb.append(" Inports[ ");
		for(InPort<?> p : inPorts){
			sb.append(p + " ");
		}
		sb.append("]");
		sb.append(" Outports[ ");
		for(OutPort<?> p: outPorts){
			sb.append(p+" ");
		}
		sb.append("]");
		return sb.toString();
	}
	
	@Override
	public String getQualifiedName() {
		if (parent == null) {
			return name;
		}
		return parent.getQualifiedName() + "." + name;
	}

}

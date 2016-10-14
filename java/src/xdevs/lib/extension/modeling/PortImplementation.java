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
package xdevs.lib.extension.modeling;

import java.util.Collection;
import xdevs.core.modeling.Port;
import xdevs.lib.extension.modeling.api.ComponentInterface;
import xdevs.lib.extension.modeling.api.PortInterface;

/**
 * @author smittal
 *
 */
public class PortImplementation<E> extends EntityImplementation implements PortInterface<E> {

    protected ComponentInterface parent;
    protected Port<E> port;

    public PortImplementation(String name) {
        super(name);
        port = new Port<>(name);
    }

    public PortImplementation() {
        this(PortInterface.class.getSimpleName());
    }

    @Override
    public String toString() {
        return getQualifiedName();
    }

    // Port members
    @Override
    public void clear() {
        port.clear();
    }

    @Override
    public boolean isEmpty() {
        return port.isEmpty();
    }

    @Override
    public E getSingleValue() {
        return port.getSingleValue();
    }

    @Override
    public Collection<E> getValues() {
        return port.getValues();
    }

    @Override
    public void addValue(E value) {
        port.addValue(value);
    }

    @Override
    public void addValues(Collection<E> values) {
        port.addValues(values);
    }

    @Override
    public ComponentInterface getParent() {
        return parent;
    }
    
    @Override
    public String getQualifiedName() {
        if (parent == null) {
            return name;
        }
        return parent.getName() + "." + name;
    }
}

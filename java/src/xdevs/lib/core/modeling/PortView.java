/*
 * Copyright (C) 2014-2019 José Luis Risco Martín <jlrisco@ucm.es>
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
package xdevs.lib.core.modeling;

import xdevs.core.modeling.Port;

/**
 *
 * @author jlrisco
 */
public class PortView {
    protected Port port;
    
    public PortView(Port port) {
        this.port = port;
    }
    
    public Port getPort() {
        return port;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(port.getName()).append("={");
        int numElements = 0;
        for(Object obj : port.getValues()) {
            if(numElements>0)
                sb.append(",");
            sb.append(obj.toString());
            numElements++;
        }
        sb.append("}");
        return sb.toString();
    }
}

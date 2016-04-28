/*
 * Copyright (C) 2015-2016 GreenDisc research group <http://greendisc.dacya.ucm.es/>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contributors:
 *  - José Luis Risco Martín
 */
package xdevs.lib.projects.slide.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author José Luis Risco Martín <jlrisco at ucm.es>
 */
public class WorkloadTrace {

    protected LinkedList<Double> times = new LinkedList<>();
    protected LinkedList<Double> values = new LinkedList<>();

    public WorkloadTrace(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(" ");
            times.add(Double.parseDouble(parts[0]));
            values.add(Double.parseDouble(parts[1]));
        }
        reader.close();
    }

    public Double getNextTime() {
        if (!times.isEmpty()) {
            return times.remove();
        } else {
            return null;
        }
    }

    public Double getNextValue() {
        if (!values.isEmpty()) {
            return values.remove();
        } else {
            return null;
        }
    }
}

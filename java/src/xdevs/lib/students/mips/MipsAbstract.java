/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xdevs.lib.students.mips;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import xdevs.core.modeling.Coupled;

/**
 * Clase abstracta del modelo acoplado MIPS. De esta clase derivarán todas las
 * demás (MipsMonocycle, MipsMulticycle, ...).
 * La funcionalidad principal de esta clase es cargar el programa ensamblador a
 * ejecutar, transformándolo a código del simulador.
 * @author José L. Risco-Martín
 */
public abstract class MipsAbstract extends Coupled {
    public MipsAbstract(String name) {
        super(name);
    }

    /**
     * Función que, dado un archivo binario MIPS32, retorna un array de cadenas.
     * Cada elemento del array contiene una instrucción ensamblador, codificada
     * con enteros (en lugar de binario).
     * @param filePath Ruta completa o relativa del archivo.
     * @return Array con las instrucciones del archivo ensamblador en codificación entera.
     * @throws java.lang.Exception
     */
    public ArrayList<String> loadDisassembledFile(String filePath) throws Exception {
        ArrayList<String> result = new ArrayList<String>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line = null;
        // Primer bucle, buscando la entrada al programa "<main>".
        boolean found = false;
        while((line = reader.readLine())!=null) {
            if(line.indexOf("<main>")>=0) {
                found = true;
                break;
            }
        }

        if(!found) {
            System.err.println("Bad file format.");
            return null;
        }

        // Segundo bucle, carga del programa en binario:
        String instructionInHex = null;
        StringBuffer instructionInBin = null;
        String[] parts = null;
        while((line = reader.readLine())!=null) {
            line = line.replaceAll(" ", "");
            parts = line.split("\t");
            instructionInHex = parts[1];
            instructionInBin = new StringBuffer();
            for(int i=0; i<instructionInHex.length(); ++i) {
                instructionInBin.append(HexToBCD(instructionInHex.charAt(i)));
            }
            instructionInBin.append(":");
            instructionInBin.append(parts[2] + " ");
            for(int i=3; i<parts.length; ++i) {
                instructionInBin.append(parts[i]);
            }
            result.add(instructionInBin.toString());
        }

        return result;
    }

    private String HexToBCD(char c) {
        int d = Character.digit(c, 16);
        StringBuffer s = new StringBuffer(Integer.toBinaryString(d));
        while(s.length()<4) {
            s.insert(0, '0');
        }
        return s.toString();
    }

    public static void main(String[] args) {
        try {
        //MipsAbstract mips = new MipsAbstract("mips");
        //ArrayList<String> result = mips.loadDisassembledFile("D:\\jlrisco\\Trabajo\\Docencia\\SSII\\DevsMips\\foo.dis");
        //for(String ins : result)
        //    System.out.println(ins);
        }
        catch(Exception ee) {
            ee.printStackTrace();
        }

        
    }
}

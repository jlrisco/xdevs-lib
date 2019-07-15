package xdevs.lib.students.mips;

import java.util.logging.Logger;
import xdevs.core.modeling.Atomic;
import xdevs.core.modeling.Port;

public class Clock extends Atomic {

    private static final Logger LOGGER = Logger.getLogger(Clock.class.getName());
    // Nombres de los puertos de entrada/salida. En este caso, al tratarse de
    // un reloj, sólo tenemos un puerto de salida.
    protected Port<Integer> out = new Port<>("out");
    // Estado. El único estado que tiene un reloj es el periodo del mismo,
    // así como el valor actual a lanzar (0 ó 1).
    // El periodo lo damos en unidades de tiempo, da igual si la escala es
    // pico-segundos o nano-segundos.
    protected Double period;
    protected Integer value;
    // Información adicional para contar el número de ciclos de reloj.
    protected Long count;

    /**
     * Constructor completo.
     * @param name Nombre de este modelo atómico.
     * @param period Periodo de reloj.
     * @param initialValue Valor inicial del reloj, puede ser 0 ó 1.
     */
    public Clock(String name, Double period, Integer initialValue) {
        super(name);
        super.addOutPort(out);
        this.period = period;
        this.value = 0;
        this.count = Long.valueOf(0);
        if (initialValue != 0) {
            this.value = 1;
        }
    }

    /**
     * Constructor por defecto:
     * @param name Nombre del modelo atómico
     * @param period Periodo de reloj
     */
    public Clock(String name, double period) {
        this(name, period, 0);
    }
    
    @Override
    public void initialize() {
        // Lo primero que hará este modelo atómico es lanzar value por el 
        // puerto de salida.
        super.holdIn("active", 0);        
    }
    
    @Override
    public void exit() {}

    @Override
    public void deltint() {
        // Nuevo semiperiodo:
        count++;
        // Nuevo valor:
        value = 1 - value;
        // La próxima vez que lancemos un valor será en un semiperiodo:
        super.holdIn("active", period / 2.0);
    }

    @Override
    public void lambda() {
        out.addValue(value);
    }

    // La función deltext no es necesaria porque este elemento no tiene
    // puertos de entrada.
    @Override
    public void deltext(double e) {
    }
}


package src.p03.c01;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ActividadSalidaPuerta
 * Clase que gestiona la salida al parque por una de las puertas.
 * 
 * @author Ahmad Mareie Pascual
 * @version 1.1
 * Práctica 3 de la asignatura de Programación Concurrente
 * 11/03/2024
 */
public class ActividadSalidaPuerta implements Runnable {

	/**
	 * Número máximo de salidas
	 */
    private static final int NUMSALIDAS = 20;
	/**
	 * Puerta por la que se realiza la entrada/salida
	 */
	private String puerta;
	/**
	 * parque al que se entra o del que se sale.
	 */
    private IParque parque;

    /**
     * Constructor de clase
     * @param puerta
     * @param parque
     */
    public ActividadSalidaPuerta(String puerta, IParque parque) {
        this.puerta = puerta;
        this.parque = parque;
    }

	/**
	 * Método que se ejecuta al lanzar el hilo. Recorre el bucle para realizar las salidas del parque.
	 */
	@Override
	public void run() {
		for (int i = 0; i < NUMSALIDAS; i++) {
			try {
				parque.salirDelParque(puerta);
				// Duerme al hilo durante un tiempo aleatorio entre 0 y 5 segundos.
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5) * 1000);
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.SEVERE, "Excepción durante el sleep en la puerta: " + puerta);
				Logger.getGlobal().log(Level.SEVERE, e.toString());
				return;
			}
		}
	}
}

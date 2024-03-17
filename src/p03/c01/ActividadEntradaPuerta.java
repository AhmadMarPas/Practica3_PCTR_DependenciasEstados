package src.p03.c01;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ActividadEntradaPuerta
 * Clase que gestiona la entrada al parque por una de las puertas.
 * 
 * @author Ahmad Mareie Pascual
 * @version 1.1
 * Práctica 3 de la asignatura de Programación Concurrente
 * 11/03/2024
 */
public class ActividadEntradaPuerta implements Runnable {

	/**
	 * Número máximo de entradas
	 */
	private static final int NUMENTRADAS = 20;
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
	public ActividadEntradaPuerta(String puerta, IParque parque) {
		this.puerta = puerta;
		this.parque = parque;
	}

	/**
	 * Método que se ejecuta al lanzar el hilo. Recorre el bucle para realizar las entradas al parque.
	 */
	@Override
	public void run() {
		for (int i = 0; i < NUMENTRADAS; i++) {
			try {
				parque.entrarAlParque(puerta);
				// Duerme al hilo durante un tiempo aleatorio entre 0 y 5 segundos.
				TimeUnit.MILLISECONDS.sleep(new Random().nextInt(5) * 1000);
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.INFO, "Entrada interrumpida");
				Logger.getGlobal().log(Level.INFO, e.toString());
				return;
			}
		}
	}
}

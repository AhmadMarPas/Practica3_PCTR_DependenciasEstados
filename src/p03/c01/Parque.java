package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase Parque que implementa los métodos para entrar al parque y para salir del parque declarados en la interfaz IParque
 * 
 * Se registran las entradas y salidas del parque por cada una de las puertas y se calcula el tiempo medio de estancia.
 * Cada puerta de entrada y/o de salida se realiza de forma concurrente a través de la ejecución de varios hilos.
 * Cada hilo se corresponde con una puerta de entrada o salida.
 * 
 * @author Ahmad Mareie Pascual
 * @version 1.1
 * Práctica 3 de la asignatura de Programación Concurrente
 * 11/03/2024
 */
public class Parque implements IParque {

	/** Máxima y Mínima capacidad de personas en el parque. */
	private final int MIN_PARQUE = 0;
	private final int AFORO_PARQUE = 20;
	private int contadorPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	/** Para calcular los tiempos medios de estancia. */
	private double tInicial;
	private double tMedio;

	/**
	 * Constructor de clase.
	 */
	public Parque() {
		contadorPersonasTotales = MIN_PARQUE;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		tInicial = System.currentTimeMillis();
		tMedio = 0;
	}

	/**
	 * Método sincronizado que regista la entrada al parque
	 * Registra el acceso al parque
	 * @param puerta por la que entra
	 */
	@Override
	public synchronized void entrarAlParque(String puerta) {
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}

		comprobarAntesDeEntrar();

		//Cálculo de tiempos
		long tActual = System.currentTimeMillis();
		tMedio = (tMedio + (tActual - tInicial)) / 2.0;

		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) + 1);

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");

		// Comprobamos que se cumple el Invariante
		checkInvariante();

		// Notificamos al resto de hilos
		notifyAll();
	}

	/**
	 * Método sincronizado que regista la salida del parque
	 * @param puerta por la que sale
	 */
	@Override
	public synchronized void salirDelParque(String puerta) {
		// Si no hay entradas, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null) {
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Comprobamos que queda gente en el parque
		comprobarAntesDeSalir();

		// Decrementamos el contador total
		contadorPersonasTotales--;
		// Decrementamos contador por puerta
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta) - 1);

		//Cálculo de tiempos
		long tActual = System.currentTimeMillis();
		tMedio = (tMedio + (tActual - tInicial)) / 2.0;

		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Salida");

		// Comprobamos que cumple invariante: personas totales = suma de personas por puerta
		checkInvariante();

		// Se notifica el cambio de estado
		notifyAll();
	}
	
	/**
	 * Muestra por consola el registro de personas al parque 
	 * @param puerta
	 * @param movimiento
	 */
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales + " tiempo medio de estancia: " + obtenerTmedio());
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	/**
	 * Método que calcula la suma de los contadores de cada puerta del parque
	 * @return int. Devuelve la suma de los contadores parciales
	 */
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
		Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
		while (iterPuertas.hasMoreElements()) {
			sumaContadoresPuerta += iterPuertas.nextElement();
		}
		return sumaContadoresPuerta;
	}

	/**
	 * Método que comprueba que siempre se cumple el Invariante.
	 * En este caso el invariante es
	 * La suma total de personas en el parque es igual a la suma parcial de entradas por las puertas del parque.
	 * El total de personas dentro del parque debe estar entre 0 y AFORO_PARQUE.
	 * */
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		assert contadorPersonasTotales >= MIN_PARQUE : "INV: El valor de personas en el parque no puede ser negativo";
		assert contadorPersonasTotales <= AFORO_PARQUE : "INV: Sobrepasado el Aforo máximo del parque";
	}

	/**
	 * Método que comprueba si se cumplen las condiciones para entrar al parque
	 * Si no se supera el aforo del parque podrá entrar, de lo contrario tendrá que esperar
	 */
	protected void comprobarAntesDeEntrar() {
		// Hay que comprobar que no se ha llegado al máximo de gente permitida en el parque
		while (contadorPersonasTotales >= AFORO_PARQUE) {
			try {
				System.out.println("PRE-WAIT");
				wait();
				System.out.println("POST-WAIT");
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.SEVERE, "Excpeción durante la espera para entrar al parque");
			}
		}
	}

	/**
	 * Método que comprueba si hay gente que pued salir de lo contrario se queda en espera.
	 */
	protected void comprobarAntesDeSalir() {
		while (contadorPersonasTotales <= MIN_PARQUE) {
			try {
				wait();
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.SEVERE, "Excpeción durante la espera para salir al parque");
			}
		}
	}
	
	/**
	 * Métdo que calcula el tiempo medio de espera en el parque.
	 */
	private synchronized double obtenerTmedio() {
		long tActual = System.currentTimeMillis();
		return (tActual - tInicial) / 1000.0;
	}

}

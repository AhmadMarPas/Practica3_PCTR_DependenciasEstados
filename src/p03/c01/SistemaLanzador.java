package src.p03.c01;

/**
 * SistemaLanzador
 * Clase con el main para realizar las llamadas de entradas y salidas al parque.
 * 
 * @author Ahmad Mareie Pascual
 * @version 1.1
 * Práctica 3 de la asignatura de Programación Concurrente
 * 11/03/2024
 */
public class SistemaLanzador {
	public static void main(String[] args) {

		IParque parque = new Parque();
		char letra_puerta = 'A';
		// final int NUM_PUERTAS = 2;
		int NumPuertas = 3;
		System.out.println("¡Parque abierto!");

		for (int i = 0; i < NumPuertas; i++) { // numero de puertas definido por parámetro

			String puerta = "" + ((char) (letra_puerta++));

			// Creación de hilos de entrada
			ActividadEntradaPuerta entradas = new ActividadEntradaPuerta(puerta, parque);
			new Thread(entradas).start();

			// Creación de hilos de salida
			ActividadSalidaPuerta salidas = new ActividadSalidaPuerta(puerta, parque);
			new Thread(salidas).start();
		}

	}
}
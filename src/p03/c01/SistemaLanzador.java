package src.p03.c01;

import java.util.logging.Level;
import java.util.logging.Logger;

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

		/**
		 * Simula el parque del caso de estudio
		 */
		IParque parque = new Parque();
		/**
		 * puerta por la que se entra/sale al/del parque
		 */
		char letra_puerta = 'A';
		/**
		 * Número de puertas que tiene el parque y determina el número de hilos que se lanzarán.
		 */
		final int NumPuertas = 5;

		System.out.println("¡Parque abierto!");

		Thread hilosEntrada[] = new Thread[NumPuertas];
		Thread hilosSalida[] = new Thread[NumPuertas];
		
		for (int i = 0; i < NumPuertas; i++) {
			String puerta = "" + ((char) (letra_puerta++));

			// Creación de hilos de entrada
			ActividadEntradaPuerta entradas = new ActividadEntradaPuerta(puerta, parque);
			hilosEntrada[i] = new Thread(entradas);
			hilosEntrada[i].start();

			// Creación de hilos de salida
			ActividadSalidaPuerta salidas = new ActividadSalidaPuerta(puerta, parque);
			hilosSalida[i] = new Thread(salidas);
			hilosSalida[i].start();
		}
		// Recorremos el bucle para realizar el join de los hilos
		for (int i = 0; i < NumPuertas; i++) {
			try {
				hilosEntrada[i].join();
				hilosSalida[i].join();
			} catch (InterruptedException e) {
				Logger.getGlobal().log(Level.SEVERE, "Excpeción durante el join de entrada");
			}
		}
		System.out.println("¡Parque cerrado!");
	}
}
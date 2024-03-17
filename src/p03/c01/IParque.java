package src.p03.c01;

/**
 * IParque
 * Iterfaz que declara lass signaturas de los métodos para entrar al parque y para salir del parque
 * 
 * @author Ahmad Mareie Pascual
 * @version 1.1
 * Práctica 3 de la asignatura de Programación Concurrente
 * 11/03/2024
 */
public interface IParque {
	
	/**
	 * Registra el acceso al parque
	 * @param puerta por la que entra
	 * */
	public abstract void entrarAlParque(String puerta);
	
	/**
	 * Registra la salida del parque
	 * @param puerta por la que sale
	 * */
	public abstract void salirDelParque(String puerta);
}

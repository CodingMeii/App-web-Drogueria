package Modelo;

import java.io.IOException;

public interface IArchivo {

	public abstract String crearFichero(String nombreArchivo) throws IOException;
	public abstract boolean buscarFichero(String nombreArchivo);
	public abstract String leerArchivo(String nombreArchivo);
}

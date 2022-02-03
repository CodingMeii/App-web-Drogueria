package Controlador;

import java.util.List;

public interface IDrogueria {
	
	//Metodo que crea el recibo teniendo en cuenta las compras realizadas.
	public abstract void comprar(List<Integer> cantidades, List<String> produc, List<Integer> canti, List<Double> precio, List<Integer> iden, int size);
	
	//Metodo que agrega un nuevo producto a la base de datos.
	public abstract void agregar(int id, String producto, int cantidad, double valorUnidad);
	
	//Metodo que elimina un producto de la base de datos.
	public abstract void eliminar(int id);
	
	//Metodo que modifica algún producto de la base de datos.
	public abstract void modificar(int id, String producto, int cantidad, double valorUnidad);
}

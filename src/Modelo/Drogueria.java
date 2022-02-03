package Modelo;

//Esta clase contiene todos los setters y getters de los atributos del objeto droguería.

public class Drogueria {
	
	public int id, cantidad;
	public String producto;
	public double valorUnidad;
	
	public Drogueria() {}
	
	public Drogueria(int id, String producto, int cantidadProducto, double valorUnidad) {
		this.id = id;
		this.producto = producto;
		this.cantidad = cantidadProducto;
		this.valorUnidad = valorUnidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getProducto() {
		return producto;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public double getValorUnidad() {
		return valorUnidad;
	}

	public void setValorUnidad(double valorUnidad) {
		this.valorUnidad = valorUnidad;
	}
}

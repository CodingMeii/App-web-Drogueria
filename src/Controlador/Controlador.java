package Controlador;

import Modelo.Conexion;
import Vista.InterfazDrogueria;
import java.sql.*;
import java.util.List;
//Esta clase permite realizar todas las acciones necesarias para cada botón de la interfaz.

public class Controlador implements IDrogueria { 

	
	Conexion con = new Conexion();
	Connection cn;
	Statement st;
	ResultSet rs;
	int[] posicion;
	int numSelec;
	String[] productos;
	int[] cantidadesCompra;
	double[] precios;
	boolean validar = true, validar1;
	public double resultado = 0;
	
	@Override
	public void comprar(List<Integer> cantidades, List<String> produc, List<Integer> canti, List<Double> precio, 
			List<Integer> iden, int size) {
		
		for (int i = 0; i < size; i++) {
			if (cantidades.get(i) != 0) {
				numSelec++;
			}
		}
		
		posicion = new int[numSelec];
		
		int j = 0;
		
		for(int i = 0; i < size; i++) {
			if (cantidades.get(i) != 0) {
				posicion[j] = i;
				j++;
			}
		}
		
		productos = new String[posicion.length];
		cantidadesCompra = new int[posicion.length];
		precios = new double[posicion.length];
		
		if (posicion.length-1 >= 0) {
			for(int i = 0; i < posicion.length; i++) {
				int pos = posicion[i];
				
				productos[i] = produc.get(posicion[i]);
				
				if (canti.get(posicion[i]) < cantidades.get(posicion[i])) {
					validar = false;
					InterfazDrogueria l = new InterfazDrogueria();
					l.mensajeError("No se tiene la cantidad\ndeseada.",2);
				}else {
					cantidadesCompra[i] = cantidades.get(posicion[i]);
					
					int cantidad = canti.get(posicion[i]) - cantidades.get(posicion[i]);
					int id = iden.get(posicion[i]);
					
					String sql = "UPDATE productos SET cantidad='"+cantidad+"' WHERE id=("+id+");";
					
					try {
						cn = con.getConexion();
						st = cn.createStatement();
						st.executeUpdate(sql);
					}catch (Exception e) {
						System.out.println("Error: "+e.getMessage());
					}
				}
				precios[i] = precio.get(posicion[i]);
			}
		}
		
		if (validar1 && validar) {
			InterfazDrogueria d = new InterfazDrogueria();
			resultado = totalPagar(cantidadesCompra, precios, posicion.length);
		}
	}
	
	public boolean validarInfo(boolean datosCompletos) {
		if (datosCompletos == true) {
			validar1 = true;
		}else {
			validar1 = false;;
		}
		return validar1;
	}
	
	public double totalPagar(int[] cantidades, double[] precio, int tamanio) {
		double resultado = 0;
		
		for (int i = 0; i < tamanio; i++) {
			resultado += (precio[i]*cantidades[i]);
		}
		return resultado;
	}

	@Override
	public void agregar(int id, String producto, int cantidad, double valorUnidad) {
		String sql = "INSERT INTO productos (id, producto, cantidad, valorUnidad) VALUES ('"+id+"', '"+producto+"', "
				+ "'"+cantidad+"', '"+valorUnidad+"');";
		
		try {
			cn = con.getConexion();
			st = cn.createStatement();
			st.executeUpdate(sql);
		}catch (Exception e){
			System.out.println("Error: "+e.getMessage());
		}
	}

	@Override
	public void eliminar(int id) {
		String sql = "DELETE FROM productos WHERE id="+id;
		
		try {
			cn = con.getConexion();
			st = cn.createStatement();
			st.executeUpdate(sql);
		}catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
	}

	@Override
	public void modificar(int id, String producto, int cantidad, double valorUnidad) {
		String sql = "UPDATE productos SET producto='"+producto+"', cantidad='"+cantidad+"', valorUnidad='"+valorUnidad+"'"
				+ " WHERE id=("+id+");";
		
		try {
			cn = con.getConexion();
			st = cn.createStatement();
			st.executeUpdate(sql);
		}catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
	}
	
}

package Modelo;

import java.io.IOException;
import java.net.URL;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Vista.InterfazDrogueria;

//Esta clase permite crear, buscar, eliminar los ficheros que representan los recibos cuya información es todas las compras
//hechas por un cliente, el numero del recibo y el valor total que el cliente pagó.

public class Fichero implements IArchivo{
	
	InterfazDrogueria ID = new InterfazDrogueria();
	public String[] producto;
	public int[] cantComprada; 
	//cantidad comprada es la cantidad de productos de la misma referencia comprada
	
	List<String> productos = new ArrayList<String>();
	List<Integer> cantidades = new ArrayList<Integer>();
	List<Double> precios = new ArrayList<Double>();
	
	public int cantProductos, valorTotal, numCompra;
	//Cantidad productos de distinta referencia comprados. 

	public Fichero(List<String> productos, List<Integer> cantidades, List<Double> precios) {
		this.productos = productos;
		this.cantidades = cantidades;
		this.precios = precios;
	}

	public String toString(String numeroRecibo) {
		StringBuilder sb = new StringBuilder();
		sb.append(" Teka's Pharmacy");
		sb.append("\n\n\n Recibo No. "+numeroRecibo+"\n ------------------------------\n Producto     Cantidad  Precio\n");
		for(int i=0;i<productos.size();i++) {
			sb.append("\n "+productos.get(i));
			for(int j=0;j<16-productos.get(i).length();j++){
				sb.append(" ");
			}
			sb.append(cantidades.get(i));
			int cant = cantidades.get(i).toString().length();
			for(int k=0;k<7-cant;k++){
				sb.append(" ");
			}
			sb.append(precios.get(i));
		
		}
		sb.append("\n ------------------------------");
		sb.append("\n        MARCA REGISTRADA       ");
		sb.append("\n              2020             ");
		return sb.toString();
	}
	
	@Override
	public String crearFichero(String nombreArchivo) throws IOException {
		URL rutaFichero = getClass().getResource("/Ficheros/");
		File archivo = new File (rutaFichero.toString().substring(6,rutaFichero.toString().length())+nombreArchivo+".txt");
        FileWriter TextOut = new FileWriter(archivo, true);
        TextOut.write(toString(archivo.getName().substring(0,archivo.getName().length()-4)));
        TextOut.close();
        return null;
	}
	
	@Override
	public String leerArchivo(String nombreArchivo) {
		URL rutaFichero = getClass().getResource("/Ficheros/");
		File archivo = new File (rutaFichero.toString().substring(6,rutaFichero.toString().length())+nombreArchivo+".txt");
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        if(archivo.exists()){
            try {
                fr = new FileReader (archivo);
                br = new BufferedReader(fr);
                String linea;
                int contador=0;
            
                while((linea=br.readLine())!=null){
                	if(linea.isEmpty()){
                		sb.append("\n");
                	}else {
                		sb.append(linea+"\n");
                	}
                }       
            }catch(Exception e){
                e.printStackTrace();
            }finally{
                try{  
                	if( null != fr ){   
                		fr.close();     
                	}
                }catch (Exception e2){ 
                   e2.printStackTrace();
                }
            }    
        
        }
        else{         
            System.out.println("Lamento decir que la tabla aun esta vacia :c ");       
        }
        return sb.toString();
	}
	
	@Override
	public boolean buscarFichero(String nombreArchivo) {
		URL rutaFichero = getClass().getResource("/Ficheros/");
		File archivo = new File (rutaFichero.toString().substring(6,rutaFichero.toString().length())+nombreArchivo+".txt");
		if(archivo.exists()) {
			return true;
		}else {
			return false;
		}
	}
}
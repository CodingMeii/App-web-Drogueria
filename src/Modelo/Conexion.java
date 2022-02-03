package Modelo;

import java.sql.*;

//Esta clase permite la conexión de la tabla de productos a la base de datos.

public class Conexion {

	Connection con;
	
	public Conexion() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/drogueria?characterEncoding=utf8", "root", "randy");
		}catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
	}
	
	public Connection getConexion() {
		return con;
	}
}

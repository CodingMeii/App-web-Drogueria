/* Nombre del proyecto: Drogueria
 * Descripción del proyecto: Programa que simula una droguería cuyo uso es una simulación de cajero, donde, 
 * el tipo de usuario empleado puede acceder a las bases de datos de la droguería y modificar los productos existentes, 
 * y tambien contará con el acceso a la base de datos donde se encuentre todas las compas realizadas. 
 * El usuario cliente puede realizar compras de los diferentes productos existentes y puede realizar reclamaciones
 * teniendo en cuenta el número del recibo y verificar la compra.
 * Autores: Caballero López Randy y Gómez Agudelo Laura Andrea.
 * Fecha: 03 / 12 / 2020
 * Versión: 1.0 */

package Vista;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import Modelo.Conexion;
import Modelo.Drogueria;
import Modelo.Fichero;
import Controlador.Controlador;

//Esta clase contará con toda la interfaz gráfica a utilizar en el proyecto.

public class InterfazDrogueria extends Application {
	
	//Button btnImagen;
	Stage stage;
	public Button btnComprar;
	TableView tvTabla = new TableView();
	CheckBox cbProductos;
	Conexion con = new Conexion();
	Connection cn;
	Statement st;
	ResultSet rs;
	int size = 0, id, cantidad;
	double valorUnidad;
	String producto;
	boolean datosCompletos = true;
	public List<String> produc;
	public List<Integer> canti;
	public List<Double> precio;
	public List<Integer> cantidadSeleccionada; 
	public List<Integer> iden;
	boolean close = true;
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage ventanaInicial) {
		ventanaInicial();
	}
	
	public void ventanaInicial() {
		Button btnFondo = new Button();
		btnFondo.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnFondo.setTranslateX(20);
		btnFondo.setTranslateY(60);
		asignarImg("/Imagenes/iconoInicio.png",btnFondo, 100);
		
		Label lblFondo = new Label();
		lblFondo.setMinWidth(155);
		lblFondo.setMinHeight(230);
		lblFondo.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		lblFondo.setTranslateX(0);
		lblFondo.setTranslateY(0);
		
		Label lblTitulo = new Label("Ingresar");
		lblTitulo.setTextFill(Color.CADETBLUE);
		lblTitulo.setMinHeight(40);
		lblTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 26));
		lblTitulo.setTranslateX(185);
		lblTitulo.setTranslateY(20);
		
		ToggleGroup grpIngreso = new ToggleGroup();
		
		//Cliente
		RadioButton rbnCliente = new RadioButton("Cliente");
		rbnCliente.setTextFill(Color.CADETBLUE);
		rbnCliente.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		rbnCliente.setTranslateX(185);
		rbnCliente.setTranslateY(120);
		rbnCliente.setToggleGroup(grpIngreso);
		
		//Empleado
		RadioButton rbnEmpleado = new RadioButton("Empleado");
		rbnEmpleado.setTextFill(Color.CADETBLUE);
		rbnEmpleado.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		rbnEmpleado.setTranslateX(185);
		rbnEmpleado.setTranslateY(90);
		rbnEmpleado.setToggleGroup(grpIngreso);
		rbnEmpleado.setSelected(true);
		
		Button btnIngresar = new Button("Aceptar");
		btnIngresar.setTextFill(Color.LAVENDER);//Put a text color
		btnIngresar.setFont(Font.font("Calibri Light", FontWeight.NORMAL, 14));
		btnIngresar.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, new CornerRadii(8), Insets.EMPTY)));
		btnIngresar.setTranslateX(225);
		btnIngresar.setTranslateY(170);
		btnIngresar.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		Pane contenedor = new Pane();
		contenedor.getChildren().addAll(lblFondo, btnFondo, lblTitulo, rbnCliente, rbnEmpleado, btnIngresar);
		contenedor.setBackground(new Background(new BackgroundFill(Color.LAVENDER, CornerRadii.EMPTY, Insets.EMPTY)));
		btnIngresar.setOnAction(event -> {
			if(grpIngreso.selectedToggleProperty().isNull().getValue()) {
				mensajeError("Faltan datos por llenar.",0);
			}else{
				RadioButton textoIngreso = (RadioButton)grpIngreso.getSelectedToggle();
				if(textoIngreso.getText()=="Cliente") {
					ventanaCliente();
				}else if(textoIngreso.getText()=="Empleado"){
					ventanaEmpleado();
				}
			}
		});
		
		Scene cuentaExistente = new Scene(contenedor);
		
		stage = new Stage();
		stage.setTitle("Ingresar");
		stage.getIcons().add(new Image("/Imagenes/icono.png"));
		stage.setMinWidth(370);
		stage.setMinHeight(230);
		stage.setScene(cuentaExistente);
		stage.show();
	}
	
	public void ventanaCliente() {
		//Text
		Label lblTitulo = new Label("Selecciona");
		lblTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 26));
		lblTitulo.setTranslateX(110);
		lblTitulo.setTextFill(Color.CADETBLUE);
		lblTitulo.setTranslateY(15);
		lblTitulo.setMaxWidth(180);
		lblTitulo.setMaxHeight(20);
		lblTitulo.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		lblTitulo.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		ToggleGroup grpIngresoEmpleado = new ToggleGroup();
		
		//Cliente
		RadioButton rbnHC = new RadioButton("Hacer una compra");
		rbnHC.setTextFill(Color.CADETBLUE);
		rbnHC.setFont(Font.font("Calibri Light", FontWeight.THIN, 15));
		rbnHC.setTranslateX(110);
		rbnHC.setTranslateY(85);
		rbnHC.setToggleGroup(grpIngresoEmpleado);
		
		//Empleado
		RadioButton rbnCP = new RadioButton("Comprobar recibo");
		rbnCP.setTextFill(Color.CADETBLUE);
		rbnCP.setFont(Font.font("Calibri Light", FontWeight.THIN, 15));
		rbnCP.setTranslateX(110);
		rbnCP.setTranslateY(60);
		rbnCP.setToggleGroup(grpIngresoEmpleado);
		rbnCP.setSelected(true);
		
		Circle circulo = new Circle(40);
		circulo.setFill(Color.CADETBLUE);
		circulo.setTranslateX(40);
		circulo.setTranslateY(40);
		
		Rectangle rectangulo = new Rectangle(40, 80);
		rectangulo.setFill(Color.CADETBLUE);
		rectangulo.setTranslateX(0);
		rectangulo.setTranslateY(0);
				
		//Image
		Button btnFondo = new Button();
		btnFondo.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnFondo.setTranslateX(0);
		btnFondo.setTranslateY(4);
		asignarImg("/Imagenes/cliente.png",btnFondo,60);
		
		Button btnAceptar = new Button("Aceptar");
		btnAceptar.setTextFill(Color.LAVENDER);
		btnAceptar.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		btnAceptar.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, new CornerRadii(5), Insets.EMPTY)));
		btnAceptar.setTranslateX(130);
		btnAceptar.setTranslateY(120);
		
		btnAceptar.setOnAction(event -> {
			if(grpIngresoEmpleado.selectedToggleProperty().isNull().getValue()) {
				mensajeError("Faltan datos por llenar.",0);
			}else{
				RadioButton textoIngreso = (RadioButton)grpIngresoEmpleado.getSelectedToggle();
				if(textoIngreso.getText()=="Comprobar recibo") {
					ventanaComprobarCompra();
				}else if(textoIngreso.getText()=="Hacer una compra"){
					ventanaComprar();
				}
			}
		});
		
		Pane contenedor = new Pane();
		contenedor.getChildren().addAll(circulo, rectangulo, btnFondo, lblTitulo, btnAceptar, rbnHC, rbnCP);

		Scene cuentaExistente = new Scene(contenedor);
		
		stage = new Stage();
		
		stage.setTitle("Menu cliente");
		stage.getIcons().add(new Image("/Imagenes/icono.png"));
		stage.setMinWidth(280);
		stage.setMinHeight(210);
		stage.setScene(cuentaExistente);
		stage.show();
	}
	
	public void ventanaComprar() {
		listar();
		//Title
		Label lblTitulo = new Label("Teka's Pharmacy");
		lblTitulo.setTextFill(Color.WHITE);
		lblTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 30));
		lblTitulo.setTranslateX(100);
		lblTitulo.setTranslateY(60);

		Button btnImagenProductos = new Button();
		btnImagenProductos.setShape(new Circle(85));
		btnImagenProductos.setTranslateY(40);
		btnImagenProductos.setBackground(new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
		btnImagenProductos.setTranslateX(450);
		asignarImg("/Imagenes/iconoInicioa.png", btnImagenProductos, 80);
		
		//Subtittle
		Label lblSubTitulo = new Label("�Exelentes precios!");
		lblSubTitulo.setTextFill(Color.WHITE);
		lblSubTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblSubTitulo.setTranslateX(150);
		lblSubTitulo.setTranslateY(105);
		
		Line lTitulo = new Line(120, 100, 320, 100);
		lTitulo.setStroke(Color.WHITE);
		lTitulo.setStrokeWidth(1);
		
		Pane contenedor = new Pane();
		contenedor.setMinSize(600, 200);
		contenedor.setTranslateX(0);
		contenedor.setTranslateY(0);
		contenedor.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		contenedor.getChildren().addAll(lblTitulo, lTitulo, lblSubTitulo, btnImagenProductos);
		
		//Product tittle
		Label lblProducto = new Label("Productos");
		lblProducto.setTextFill(Color.CADETBLUE);
		lblProducto.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblProducto.setTranslateX(60);
		lblProducto.setTranslateY(10);
		
		//Quantity tttle
		Label lblCantidad = new Label("Cantidad");
		lblCantidad.setTextFill(Color.CADETBLUE);
		lblCantidad.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblCantidad.setTranslateX(270);
		lblCantidad.setTranslateY(10);
		
		//Price tittle
		Label lblPrecios = new Label("Precio u/c");
		lblPrecios.setTextFill(Color.CADETBLUE);
		lblPrecios.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblPrecios.setTranslateX(455);
		lblPrecios.setTranslateY(10);
		
		int x = 10, y = 40;
		
		List<CheckBox> productos = new ArrayList<>();
		List<TextField> cantidades = new ArrayList<>();
		List<Label> precios = new ArrayList<>();
		
		for (int i = 0; i < size; i++) {
			String productoBase = produc.get(i);
			
			CheckBox cbProductos = new CheckBox(productoBase);
			cbProductos.setTranslateX(x);
			cbProductos.setTranslateY(y);
			productos.add(cbProductos);
			
			TextField txtCantidad = new TextField("0");
			//txtCantidad.setPromptText("0");
			txtCantidad.setTranslateX(x+200);
			txtCantidad.setTranslateY(y);
			txtCantidad.setAlignment(Pos.CENTER);
			cantidades.add(txtCantidad);
			
			String valorPorUnidades = String.valueOf(precio.get(i));
			
			Label lblPrecio = new Label(valorPorUnidades);
			lblPrecio.setTranslateX(x+450);
			lblPrecio.setTranslateY(y);
			precios.add(lblPrecio);
			y += 30;
		}
		
		Pane contenedorAux = new Pane();
		contenedorAux.setMinSize(560, 30*size);
		contenedorAux.getChildren().addAll(productos);
		contenedorAux.getChildren().addAll(cantidades);
		contenedorAux.getChildren().addAll(precios);
		contenedorAux.getChildren().addAll(lblProducto, lblCantidad, lblPrecios);
		contenedorAux.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		ScrollPane spCompras = new ScrollPane();
		spCompras.setPrefSize(560, 200);
		spCompras.setTranslateX(20);
		spCompras.setTranslateY(150);
		spCompras.setContent(contenedorAux);
		spCompras.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		spCompras.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		spCompras.setPannable(true);
		
		//Id label
		Label lblRegistro = new Label("Identificacion: ");
		lblRegistro.setTextFill(Color.BLACK);
		lblRegistro.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblRegistro.setTranslateX(100);
		lblRegistro.setTranslateY(380);
		TextField txtRegistro = new TextField();
		txtRegistro.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		txtRegistro.setBorder(new Border(new BorderStroke(Color.CADETBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		txtRegistro.setTranslateX(220);
		txtRegistro.setTranslateY(375);
		txtRegistro.setPromptText("No. Registro");
		
		//Total 
		Label lblTotal = new Label("Total a pagar: ");
		lblTotal.setTextFill(Color.BLACK);
		lblTotal.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblTotal.setTranslateX(100);
		lblTotal.setTranslateY(410);
		TextField txtTotal = new TextField();
		txtTotal.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		txtTotal.setBorder(new Border(new BorderStroke(Color.CADETBLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		txtTotal.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(8), Insets.EMPTY)));
		txtTotal.setTranslateX(220);
		txtTotal.setTranslateY(405);
		txtTotal.setEditable(false);
		txtTotal.setPromptText("0.0");
		
		//Buy Button
		btnComprar = new Button("Comprar");
		btnComprar.setTextFill(Color.WHITE);
		btnComprar.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		btnComprar.setTranslateX(450);
		btnComprar.setTranslateY(380);
		btnComprar.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, new CornerRadii(8), Insets.EMPTY)));
		btnComprar.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		cantidadSeleccionada = new ArrayList<>();
		Stage ventanaComprar = new Stage();
		Fichero f = new Fichero(produc, canti, precio);
		btnComprar.setOnAction(e -> {
			try {
				for (int k = 0; k < size; k++) {
					int aux = 0;
					aux = Integer.parseInt(cantidades.get(k).getText());
					cantidadSeleccionada.add(aux);
				}
	
				int incompleto = 0;
				
				for (int g = 0; g < size; g++) {
					if (cantidadSeleccionada.get(g) == 0) {
						incompleto++;
					}
				}
				
				if (incompleto == size && txtRegistro.getText().isEmpty()) {
					datosCompletos = false;
				}
				f.crearFichero(txtRegistro.getText());
			} catch (Exception error) {
				mensajeError(error.getMessage(),0);
			}			
			Controlador d = new Controlador();
			if (d.validarInfo(datosCompletos) && txtRegistro.getText().isEmpty() == false) {
				d.comprar(cantidadSeleccionada, produc, canti, precio, iden, size);
				txtTotal.setText(String.valueOf(d.resultado));
				compraRealizada(txtRegistro.getText());
				ventanaComprar.close();
			}else {
				mensajeError("Faltan datos por llenar.",0);
			}
		});
		
		//Container
		Pane superContenedor = new Pane();
		superContenedor.setBackground(new Background(new BackgroundFill(Color.GAINSBORO, CornerRadii.EMPTY, Insets.EMPTY)));
		superContenedor.getChildren().addAll(contenedor, spCompras, lblRegistro, txtRegistro, btnComprar, lblTotal, txtTotal);
		
		//Show the stage   
		Scene micompraRealizada = new Scene(superContenedor);
		
		ventanaComprar.setTitle("Comprar productos");
		ventanaComprar.getIcons().add(new Image("/Imagenes/icono.png"));
		ventanaComprar.setMinWidth(600);
		ventanaComprar.setMinHeight(490);
		ventanaComprar.setScene(micompraRealizada);
		ventanaComprar.show();
	}
	
	public void compraRealizada(String numeroRecibo) {
		//Text
		Label lblNumero = new Label("�Compra exitosa!\nTu numero de recibo es: "+numeroRecibo);
		lblNumero.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		lblNumero.setTranslateX(15);
		lblNumero.setTextFill(Color.CADETBLUE);
		lblNumero.setTranslateY(25);
		lblNumero.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		lblNumero.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		lblNumero.setAlignment(Pos.CENTER);
		
		Line linea = new Line(300,0,300,100);
		linea.setStroke(Color.CADETBLUE);
		linea.setStrokeWidth(100);
		
		//Image
		Button btnImg = new Button();
		btnImg.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnImg.setTranslateX(172);
		btnImg.setTranslateY(2);
		btnImg.setMinWidth(240);
		btnImg.setMinHeight(100);
		asignarImg("/Imagenes/exito.png",btnImg,50);
		
		Button btnAceptar = new Button("Ok");
		btnAceptar.setTextFill(Color.CADETBLUE);
		btnAceptar.setFont(Font.font("Calibri", FontWeight.NORMAL, 18));
		btnAceptar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnAceptar.setTranslateX(100);
		btnAceptar.setTranslateY(70);
		
		btnAceptar.setOnAction(e ->{
			stage.close();
			ventanaInicial();
		});
		
		Pane contenedor = new Pane();
		contenedor.getChildren().addAll(linea, btnImg, lblNumero,  btnAceptar);
		Scene cuentaExistente = new Scene(contenedor);
		
		stage = new Stage();
		
		stage.setTitle("Mensaje");
		stage.getIcons().add(new Image("/Imagenes/icono.png"));
		stage.setMinWidth(350);
		stage.setMaxWidth(350);
		stage.setMinHeight(150);
		stage.setMaxHeight(150);
		stage.setScene(cuentaExistente);
		stage.show();	
	}
	
	public void ventanaComprobarCompra() {
		Fichero f = new Fichero(produc, canti, precio);
		//Text
		TextField txtNumero = new TextField();
		txtNumero.setFont(Font.font("Calibri Light", FontWeight.THIN, 18));
		txtNumero.setTranslateX(30);
		txtNumero.setStyle("-fx-text-inner-color: lavender;"+"-fx-prompt-text-fill: lavender;");
		txtNumero.setPromptText("No. de recibo");
		txtNumero.setTranslateY(32);
		txtNumero.setMaxWidth(180);
		txtNumero.setMaxHeight(20);
		txtNumero.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		txtNumero.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		Line linea = new Line(45, 63, 240, 63);
		linea.setStroke(Color.LAVENDER);
		linea.setStrokeWidth(1.5);
				
		//Image
		Button btnError = new Button();
		btnError.setBackground(new Background(new BackgroundFill(Color.rgb(238, 236, 236, 0.2), CornerRadii.EMPTY, Insets.EMPTY)));
		btnError.setTranslateX(22);
		btnError.setTranslateY(20);
		btnError.setMinWidth(240);
		btnError.setMinHeight(100);
		
		Button btnAceptar = new Button("Buscar");
		btnAceptar.setTextFill(Color.LAVENDER);
		btnAceptar.setFont(Font.font("Calibri", FontWeight.NORMAL, 18));
		btnAceptar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnAceptar.setTranslateX(110);
		btnAceptar.setTranslateY(80);
		
		btnAceptar.setOnAction(e ->{
			if(txtNumero.getText().isEmpty()) {
				mensajeError("Datos incompletos.",0);
			}else{
				if(f.buscarFichero(txtNumero.getText())){
					ventanaRecibo(txtNumero.getText());
				}else {
					mensajeError("El recibo digitado\nno existe.",2);
				}
			}
		});
		
		Pane contenedor = new Pane();
		contenedor.getChildren().addAll(btnError, txtNumero, linea, btnAceptar);
		contenedor.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		Scene cuentaExistente = new Scene(contenedor);
		
		stage = new Stage();
		stage.setTitle("Buscar fichero");
		stage.getIcons().add(new Image("/Imagenes/icono.png"));
		stage.setMinWidth(300);
		stage.setMinHeight(180);
		stage.setScene(cuentaExistente);
		stage.show();	
	}
	
	public void ventanaRecibo(String nombreArchivo) {
		Fichero f = new Fichero(produc, canti, precio);
		Label lblRecibo = new Label(f.leerArchivo(nombreArchivo));
		lblRecibo.setFont(Font.font("Consolas", FontWeight.THIN, 20));
		lblRecibo.setTranslateX(0);
		lblRecibo.setTranslateY(0);
		
		Button btnImg = new Button();
		btnImg.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnImg.setTranslateX(235);
		btnImg.setTranslateY(15);
		asignarImg("/Imagenes/recibo.png",btnImg, 70);
		
		Pane contenedor = new Pane();
		contenedor.getChildren().addAll(lblRecibo, btnImg);
		contenedor.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		Scene cuentaExistente = new Scene(contenedor);
		
		stage = new Stage();
		
		stage.setTitle("Recibo");
		stage.getIcons().add(new Image("/Imagenes/icono.png"));
		stage.setMinWidth(350);
		stage.setMinHeight(150);
		stage.setScene(cuentaExistente);
		stage.show();
	}
	
	public void ventanaEmpleado() {
		//Text
		Label lblTitulo = new Label("Selecciona");
		lblTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 26));
		lblTitulo.setTranslateX(110);
		lblTitulo.setTextFill(Color.CADETBLUE);
		lblTitulo.setTranslateY(15);
		lblTitulo.setMaxWidth(180);
		lblTitulo.setMaxHeight(20);
		lblTitulo.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		lblTitulo.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		ToggleGroup grpIngresoEmpleado = new ToggleGroup();
		
		//Cliente
		RadioButton rbnBD = new RadioButton("Base de Datos");
		rbnBD.setTextFill(Color.CADETBLUE);
		rbnBD.setFont(Font.font("Calibri Light", FontWeight.THIN, 15));
		rbnBD.setTranslateX(110);
		rbnBD.setTranslateY(85);
		rbnBD.setToggleGroup(grpIngresoEmpleado);
		
		//Empleado
		RadioButton rbnLP = new RadioButton("Lista de productos");
		rbnLP.setTextFill(Color.CADETBLUE);
		rbnLP.setFont(Font.font("Calibri Light", FontWeight.THIN, 15));
		rbnLP.setTranslateX(110);
		rbnLP.setTranslateY(60);
		rbnLP.setToggleGroup(grpIngresoEmpleado);
		rbnLP.setSelected(true);
		
		Circle circulo = new Circle(40);
		circulo.setFill(Color.CADETBLUE);
		circulo.setTranslateX(40);
		circulo.setTranslateY(40);
		
		Rectangle rectangulo = new Rectangle(40, 80);
		rectangulo.setFill(Color.CADETBLUE);
		rectangulo.setTranslateX(0);
		rectangulo.setTranslateY(0);
				
		//Image
		Button btnFondo = new Button();
		btnFondo.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnFondo.setTranslateX(-5);
		btnFondo.setTranslateY(4);
		asignarImg("/Imagenes/empleado.png",btnFondo,60);
		
		Button btnAceptar = new Button("Buscar");
		btnAceptar.setTextFill(Color.LAVENDER);
		btnAceptar.setFont(Font.font("Calibri", FontWeight.NORMAL, 16));
		btnAceptar.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, new CornerRadii(5), Insets.EMPTY)));
		btnAceptar.setTranslateX(130);
		btnAceptar.setTranslateY(120);
		
		btnAceptar.setOnAction(event -> {
			if(grpIngresoEmpleado.selectedToggleProperty().isNull().getValue()) {
				mensajeError("Faltan datos por llenar.",0);
			}else{
				RadioButton textoIngreso = (RadioButton)grpIngresoEmpleado.getSelectedToggle();
				if(textoIngreso.getText()=="Base de Datos") {
					baseDeDatos();
				}else if(textoIngreso.getText()=="Lista de productos"){
					ventanaComprar();
				}
			}
		});
		
		Pane contenedor = new Pane();
		contenedor.getChildren().addAll(circulo, rectangulo, btnFondo, lblTitulo, btnAceptar, rbnBD, rbnLP);

		Scene cuentaExistente = new Scene(contenedor);
		
		stage = new Stage();
		
		stage.setTitle("Menu empleado");
		stage.getIcons().add(new Image("/Imagenes/icono.png"));
		stage.setMinWidth(280);
		stage.setMinHeight(210);
		stage.setScene(cuentaExistente);
		stage.show();
	}
	
	public void baseDeDatos() {
		//Title
		Label lblTitulo = new Label("Teka's Pharmacy");
		lblTitulo.setTextFill(Color.WHITE);
		lblTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 25));
		lblTitulo.setTranslateX(175);
		lblTitulo.setTranslateY(10);
		
		//Image
		Button btnImagen = new Button();
		btnImagen.setShape(new Circle(85));
		btnImagen.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnImagen.setTranslateX(460);
		btnImagen.setTranslateY(65);
		asignarImg("/Imagenes/pharmacy.png", btnImagen, 100);
		
		//Subtittle
		Label lblSubTitulo = new Label("Base de datos");
		lblSubTitulo.setTextFill(Color.WHITE);
		lblSubTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblSubTitulo.setTranslateX(225);
		lblSubTitulo.setTranslateY(40);
		
		Line lTitulo = new Line(175, 40, 375, 40);
		lTitulo.setStroke(Color.WHITE);
		lTitulo.setStrokeWidth(1);
		
		//Circle
		Circle circulo = new Circle(60);
		circulo.setFill(Color.LAVENDER);
		circulo.setTranslateX(500);
		circulo.setTranslateY(120);
		
		//Rectangle
		Rectangle rectangulo = new Rectangle(200, 120);
		rectangulo.setFill(Color.LAVENDER);
		rectangulo.setTranslateX(500);
		rectangulo.setTranslateY(60);
		
		//Data
		Label lblDatos = new Label("Datos");
		lblDatos.setTextFill(Color.WHITE);
		lblDatos.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblDatos.setTranslateX(20);
		lblDatos.setTranslateY(60);
		
		int tamanioTexto = 150, tamanioX = 30, tamanioY = 30, ancho = 90;
		
		//ID
		Label lblId = new Label("ID: ");
		lblId.setTextFill(Color.WHITE);
		lblId.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblId.setTranslateX(tamanioX);
		lblId.setTranslateY(tamanioY*3);
		lblId.setMinWidth(ancho);
		lblId.setAlignment(Pos.BASELINE_RIGHT);
		TextField txtId = new TextField();
		txtId.setStyle("-fx-text-inner-color: lavender;"+"-fx-prompt-text-fill: lavender;");
		txtId.setPromptText("No. de referencia");
		txtId.setTranslateX(tamanioTexto);
		txtId.setTranslateY(tamanioY*3);
		txtId.setMinSize(250, 20);
		txtId.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		txtId.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		Line lineaId = new Line(150, tamanioY*4, 400, tamanioY*4);
		lineaId.setStroke(Color.LAVENDER);
		lineaId.setStrokeWidth(1.5);
		
		//Nombre del producto
		Label lblProducto = new Label("Producto: ");
		lblProducto.setTextFill(Color.WHITE);
		lblProducto.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblProducto.setTranslateX(tamanioX);
		lblProducto.setTranslateY(tamanioY*4);
		lblProducto.setMinWidth(ancho);
		lblProducto.setAlignment(Pos.BASELINE_RIGHT);
		
		TextField txtProducto = new TextField();
		txtProducto.setTranslateX(tamanioTexto);
		txtProducto.setTranslateY(tamanioY*4);
		txtProducto.setMinSize(250, 20);
		txtProducto.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		txtProducto.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		txtProducto.setStyle("-fx-text-inner-color: lavender;"+"-fx-prompt-text-fill: lavender;");
		txtProducto.setPromptText("Nombre del producto");
		
		Line lineaProducto = new Line(150, tamanioY*5, 400, tamanioY*5);
		lineaProducto.setStroke(Color.LAVENDER);
		lineaProducto.setStrokeWidth(1.5);
		
		//Cantidad
		Label lblCantidad = new Label("Cantidad: ");
		lblCantidad.setTextFill(Color.WHITE);
		lblCantidad.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblCantidad.setTranslateX(tamanioX);
		lblCantidad.setTranslateY(tamanioY*5);
		lblCantidad.setMinWidth(ancho);
		lblCantidad.setAlignment(Pos.BASELINE_RIGHT);
		
		TextField txtCantidad = new TextField();
		txtCantidad.setTranslateX(tamanioTexto);
		txtCantidad.setTranslateY(tamanioY*5);
		txtCantidad.setMinSize(250, 20);
		txtCantidad.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		txtCantidad.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		txtCantidad.setStyle("-fx-text-inner-color: lavender;"+"-fx-prompt-text-fill: lavender;");
		txtCantidad.setPromptText("Cantidad de unidades");
		
		Line lineaCantidad = new Line(150, tamanioY*6, 400, tamanioY*6);
		lineaCantidad.setStroke(Color.LAVENDER);
		lineaCantidad.setStrokeWidth(1.5);
		
		//Valor por unidad
		Label lblValorUnidad = new Label("Valor u/c: ");
		lblValorUnidad.setTextFill(Color.WHITE);
		lblValorUnidad.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblValorUnidad.setTranslateX(tamanioX);
		lblValorUnidad.setTranslateY(tamanioY*6);
		lblValorUnidad.setMinWidth(ancho);
		lblValorUnidad.setAlignment(Pos.BASELINE_RIGHT);
		
		TextField txtValorUnidad = new TextField();
		txtValorUnidad.setTranslateX(tamanioTexto);
		txtValorUnidad.setTranslateY(tamanioY*6);
		txtValorUnidad.setMinSize(250, 20);
		txtValorUnidad.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		txtValorUnidad.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		txtValorUnidad.setStyle("-fx-text-inner-color: lavender;"+"-fx-prompt-text-fill: lavender;");
		txtValorUnidad.setPromptText("Precio u/c");
		
		Line lineaValorUnidad = new Line(150, tamanioY*7, 400, tamanioY*7);
		lineaValorUnidad.setStroke(Color.LAVENDER);
		lineaValorUnidad.setStrokeWidth(1.5);
		
		//Tittle pane
		Pane tContenedor = new Pane();
		tContenedor.setMinSize(600, 200);
		tContenedor.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		tContenedor.getChildren().addAll(lblTitulo, lblDatos, lblId, txtId, lblProducto, txtProducto, lblCantidad, txtCantidad, 
				lblValorUnidad, txtValorUnidad, circulo, rectangulo, lTitulo, lblSubTitulo, lineaId, lineaProducto,
				lineaCantidad, lineaValorUnidad);
		
		//Operations
		Label lblOper = new Label("Operaciones");
		lblOper.setTextFill(Color.WHITE);
		lblOper.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblOper.setTranslateX(20);
		lblOper.setTranslateY(220);
		
		//Buttons
		//Button add
		Button btnAgregar = new Button("Agregar");
		btnAgregar.setTextFill(Color.BLACK);//Put a text color
		btnAgregar.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		btnAgregar.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(8), Insets.EMPTY)));
		btnAgregar.setTranslateX(20);
		btnAgregar.setTranslateY(260);
		btnAgregar.setMinSize(100, 30);
		btnAgregar.setMaxSize(100, 30);
		btnAgregar.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		btnAgregar.setOnAction(e -> {
			if(txtId.getText().isEmpty() || txtValorUnidad.getText().isEmpty() || txtCantidad.getText().isEmpty() || txtProducto.getText().isEmpty()) {
				mensajeError("Faltan datos por llenar.",0);
			}else {
				id = Integer.parseInt(txtId.getText());
				valorUnidad = Double.parseDouble(txtValorUnidad.getText());
				cantidad = Integer.parseInt(txtCantidad.getText());
				producto = txtProducto.getText();
				
				Controlador c = new Controlador();
				limpiar();
				c.agregar(id, producto, cantidad, valorUnidad);
				txtId.setText("");
				txtProducto.setText("");
				txtCantidad.setText("");
				txtValorUnidad.setText("");
				txtId.requestFocus();
				listar();
			}

		});
		
		//Button change
		Button btnModificar = new Button("Modificar");
		btnModificar.setTextFill(Color.BLACK);//Put a text color
		btnModificar.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(8), Insets.EMPTY)));
		btnModificar.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		btnModificar.setTranslateX(170);
		btnModificar.setTranslateY(260);
		btnModificar.setMinSize(100, 30);
		btnModificar.setMaxSize(100, 30);
		btnModificar.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		btnModificar.setOnAction(e -> {
			if(tvTabla.getSelectionModel().getSelectedIndex()!=-1) {
				id = Integer.parseInt(txtId.getText());
				valorUnidad = Double.parseDouble(txtValorUnidad.getText());
				cantidad = Integer.parseInt(txtCantidad.getText());
				producto = txtProducto.getText();
				
				Controlador c = new Controlador();
				limpiar();
				c.modificar(id, producto, cantidad, valorUnidad);
				txtId.setText("");
				txtProducto.setText("");
				txtCantidad.setText("");
				txtValorUnidad.setText("");
				txtId.requestFocus();
				listar();
			}else {
				mensajeError("Selecciona una fila.",0);
			}			
		});
		
		//Button delete
		Button btnEliminar = new Button("Eliminar");
		btnEliminar.setTextFill(Color.BLACK);//Put a text color
		btnEliminar.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(8), Insets.EMPTY)));
		btnEliminar.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		btnEliminar.setTranslateX(320);
		btnEliminar.setTranslateY(260);
		btnEliminar.setMinSize(100, 30);
		btnEliminar.setMaxSize(100, 30);
		btnEliminar.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		btnEliminar.setOnAction(e -> {
			if(tvTabla.getSelectionModel().getSelectedIndex()!=-1) {
				id = Integer.parseInt(txtId.getText());
				valorUnidad = Double.parseDouble(txtValorUnidad.getText());
				cantidad = Integer.parseInt(txtCantidad.getText());
				producto = txtProducto.getText();
				
				Controlador c = new Controlador();
				limpiar();
				c.eliminar(id);
				txtId.setText("");
				txtProducto.setText("");
				txtCantidad.setText("");
				txtValorUnidad.setText("");
				txtId.requestFocus();
				listar();
			}else {
				mensajeError("Selecciona una fila.",0);
			}
			
		});
		
		//Button new
		Button btnNuevo = new Button("Nuevo");
		btnNuevo.setTextFill(Color.BLACK);//Put a text color
		btnNuevo.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		btnNuevo.setBackground(new Background(new BackgroundFill(Color.LAVENDER, new CornerRadii(8), Insets.EMPTY)));
		btnNuevo.setTranslateX(470);
		btnNuevo.setTranslateY(260);
		btnNuevo.setMinSize(100, 30);
		btnNuevo.setMaxSize(100, 30);
		btnNuevo.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		
		btnNuevo.setOnAction(e -> {
			txtId.setText("");
			txtProducto.setText("");
			txtCantidad.setText("");
			txtValorUnidad.setText("");
			txtId.requestFocus();
		});
		
		//Operations
		Label lblLista = new Label("Lista");
		lblLista.setTextFill(Color.WHITE);
		lblLista.setFont(Font.font("Calibri Light", FontWeight.THIN, 14));
		lblLista.setTranslateX(20);
		lblLista.setTranslateY(310);
		
		btnNuevo.setOnAction(e -> {
			txtId.setText("");
			txtProducto.setText("");
			txtCantidad.setText("");
			txtValorUnidad.setText("");
			txtId.requestFocus();
		});
		
		tvTabla = new TableView();
		tvTabla.setBorder(new Border(new BorderStroke(Color.CADETBLUE,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
		tvTabla.setTranslateX(20);
		tvTabla.setTranslateY(350);
		tvTabla.setMinWidth(560);
		tvTabla.setMinHeight(10);
		tvTabla.setMaxHeight(150);
		
		tvTabla.setOnMouseClicked(e -> {
			int fila = tvTabla.getSelectionModel().getSelectedIndex();
			
			Drogueria d = (Modelo.Drogueria) tvTabla.getSelectionModel().getSelectedItem();
			int i = d.getId();
			txtId.setText(String.valueOf(i));
			String p = d.getProducto();
			txtProducto.setText(p);
			int c = d.getCantidad();
			txtCantidad.setText(String.valueOf(c));
			double v = d.getValorUnidad();
			txtValorUnidad.setText(String.valueOf(v));
		});
		
		//Columns
		TableColumn tcDatabase = new TableColumn("Base de datos");
		tcDatabase.setStyle("-fx-text-background-color: cadetblue");
		/*tcDatabase.setStyle("-fx-background-color: cadetblue;"+ //Changes the backgroung of the column
					"-fx-font-size: 10pt;"+ //changes the font size
					"-fx-text-background-color: white;"); //Changes the text color*/
		
		//Column Id
		TableColumn<InterfazDrogueria, Integer> tcId = new TableColumn<>("ID");
		tcId.setMinWidth(100);
		
		//Column product
		TableColumn<InterfazDrogueria, String> tcProducto = new TableColumn<>("Producto");
		tcProducto.setMinWidth(260);
		
		//Columna cantidad
		TableColumn<InterfazDrogueria, Integer> tcCantidad = new TableColumn<>("Cantidad");
		tcCantidad.setMinWidth(100);
		
		//Columna valor por unidad
		TableColumn<InterfazDrogueria, Float> tcValorUnidad = new TableColumn<>("Precio c/u");
		tcValorUnidad.setMinWidth(100);
		
		tcDatabase.getColumns().addAll(tcId, tcProducto, tcCantidad, tcValorUnidad);
		tvTabla.getColumns().add(tcDatabase);
		
		tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tcProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
		tcCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
		tcValorUnidad.setCellValueFactory(new PropertyValueFactory<>("valorUnidad"));
		
		//List all the elements on database
		listar();
		
		//Container
		Pane contenedor = new Pane();
		contenedor.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
		contenedor.getChildren().addAll(tContenedor, lblOper, btnAgregar, btnModificar, btnEliminar, btnNuevo, lblLista, tvTabla,
				btnImagen);
		
		//Show the stage   
		Scene mibaseDeDatos = new Scene(contenedor);
		Stage baseDeDatos = new Stage();
		baseDeDatos.setTitle("Base de datos");
		baseDeDatos.getIcons().add(new Image("/Imagenes/icono.png"));
		baseDeDatos.setMinWidth(600);
		baseDeDatos.setMinHeight(520);
		baseDeDatos.setScene(mibaseDeDatos);
		baseDeDatos.show();
	}
	
	public void mensajeError(String mensajeError, int numLineas) {
		Label lblTitulo = new Label(mensajeError);
		lblTitulo.setTextFill(Color.LAVENDER);
		lblTitulo.setFont(Font.font("Calibri Light", FontWeight.THIN, 20));
		lblTitulo.setTranslateX(100);
		lblTitulo.setTranslateY(37-(numLineas*5));
		lblTitulo.setMinWidth(200);
		lblTitulo.setMinHeight(30);
		lblTitulo.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		lblTitulo.setBorder(new Border(new BorderStroke(Color.TRANSPARENT,BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

		//Image
		Button btnError = new Button();
		btnError.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnError.setTranslateX(22);
		btnError.setTranslateY(25);
		asignarImg("/Imagenes/Error.png",btnError, 50);
		
		Button btnAceptar = new Button("Ok");
		btnAceptar.setTextFill(Color.LAVENDER);
		btnAceptar.setFont(Font.font("Calibri", FontWeight.NORMAL, 18));
		btnAceptar.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
		btnAceptar.setTranslateX(288);
		btnAceptar.setTranslateY(76);
		
		btnAceptar.setOnAction(e ->{
			stage.close();
		});
		
		Pane contenedor = new Pane();
		contenedor.getChildren().addAll(lblTitulo, btnError, btnAceptar);
		contenedor.setBackground(new Background(new BackgroundFill(Color.CADETBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

		Scene cuentaExistente = new Scene(contenedor);
		
		stage = new Stage();
		
		stage.setTitle("Error");
		stage.getIcons().add(new Image("/Imagenes/icono.png"));
		stage.setMinWidth(350);
		stage.setMinHeight(150);
		stage.setScene(cuentaExistente);
		stage.show();	
	}
	
	public void asignarImg(String ruta, Button nombre, int tamanio) {
		URL rutaImagen = getClass().getResource(ruta);
		Image imagen = new Image(rutaImagen.toString(), tamanio, tamanio, true, true);
		nombre.setGraphic(new ImageView (imagen));
	}
	
	public void listar() {
		Drogueria d = new Drogueria();
		size = 0;
		produc = new ArrayList<String>();
		canti = new ArrayList<Integer>();
		precio = new ArrayList<Double>();
		iden = new ArrayList<Integer>();
		
		String sql = "SELECT * FROM productos";
		
		try {
			cn = con.getConexion();
			st = cn.createStatement();
			rs = st.executeQuery(sql);
			
			while(rs.next()) {
				d.setId(rs.getInt("id"));
				d.setProducto(rs.getString("producto"));
				d.setCantidad(rs.getInt("cantidad"));
				d.setValorUnidad(rs.getDouble("valorUnidad"));
				
				int id = d.getId();
				String producto = d.getProducto();
				int cant = d.getCantidad();
				double valor = d.getValorUnidad();
				
				iden.add(id);
				produc.add(producto);
				canti.add(cant);
				precio.add(valor);
				
				Drogueria dd = new Drogueria(id, producto, cant, valor);
				tvTabla.getItems().add(dd);
				size++;
			}
			
		}catch (Exception e) {
			mensajeError(e.getMessage(),0);
		}
	}
	
	public void limpiar() {
		int j = 0;
		for (int i = 0; i < size; i++) {
			tvTabla.getItems().remove(j);
		}
	}
}

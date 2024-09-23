package com.lab1;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import com.lab1.model.Administrador;
import com.lab1.model.ClubDeportivo;
import com.lab1.model.Utilidades.Utilidades;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.logging.*;

public class LoginController{

    @FXML private ResourceBundle resources;

    @FXML private URL location;
    @FXML private Text tituloText;
    @FXML private Text idText;
    @FXML private TextField idLoginTextField;

    @FXML private Text nombreText;
    @FXML private TextField nombreLoginTextField;

    @FXML private Button iniciarButton;
    @FXML private ComboBox<String> idiomaComboBox;
    @FXML private Text idiomaText;

    public void initialize() throws IOException {

        idiomaComboBox.setItems(FXCollections.observableArrayList("es", "en"));
        textoInterfaz();
        Utilidades.getInstance().inicializarLogger();
        iniciarButton.setOnAction(e-> ingresar());
        idiomaComboBox.setOnAction(e->cambiarIdioma(idiomaComboBox.getValue()));
    }

    private void textoInterfaz(){
        idText.setText(Utilidades.getInstance().getBundle().getString("id"));
        nombreText.setText(Utilidades.getInstance().getBundle().getString("nombre"));
        tituloText.setText(Utilidades.getInstance().getBundle().getString("tituloApp"));
        iniciarButton.setText(Utilidades.getInstance().getBundle().getString("iniciar_button"));
        idiomaText.setText(Utilidades.getInstance().getBundle().getString("idioma"));
    }
    public void cambiarIdioma(String codigoIdioma) {
        Locale nuevoLocale = new Locale(codigoIdioma);
        Utilidades.getInstance().setLocale(nuevoLocale);
        textoInterfaz();
    }
    private void ingresar() {
        //Asigna los datos ingresados en los campos de texto como variables.
        String nombreAdmin = nombreLoginTextField.getText();
        String idTextField = idLoginTextField.getText();
        int idAdmin = -1; // Inicializado para que tenga algún valor por el momento.

        // Verifica si el ID ingresado es un número válido
        try {
            idAdmin = Integer.parseInt(idTextField);
        } catch (NumberFormatException e) {
            // Cuando la id ingresada es diferente a un número.
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("ID incorrecta.");
            alerta.setContentText("La ID solo debe contener números");
            alerta.showAndWait();
            Utilidades.getInstance().escribirLog(LoginController.class, "La ID solo debe contener números", Level.WARNING);
            return;
        }
            
        ClubDeportivo club = ClubDeportivo.getInstancia();
        Administrador admin = club.getAdmin();
        if (admin.getNombre().equals(nombreAdmin) && admin.getNumeroId() == idAdmin) {
            try {
                ManejadorEscenas.cambiarEscena("clubDeportivo");
            } catch (IOException e) {
                e.printStackTrace();
                Utilidades.getInstance().escribirLog(LoginController.class, "Error al cambiar de escena.", Level.WARNING);
            }
        }
        else{
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Los Datos son incorrectos.");
            alerta.setContentText("Ingrese los datos de nuevo.");
            alerta.showAndWait();
            Utilidades.getInstance().escribirLog(LoginController.class,"Los Datos son incorrectos." , Level.WARNING);
        }
        
    }
    

}
package com.lab1;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.lab1.model.Administrador;
import com.lab1.model.ClubDeportivo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class LoginController{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField idLoginTextField;

    @FXML
    private TextField nombreLoginTextField;

    @FXML
    void ingresar(ActionEvent event) {
        //Asigna los datos ingresados en los campos de texto como variables.
        String nombreAdmin = nombreLoginTextField.getText();
        String idText = idLoginTextField.getText();
        int idAdmin = -1; // Inicializado para que tenga algún valor por el momento.

        // Verifica si el ID ingresado es un número válido
        try {
            idAdmin = Integer.parseInt(idText);
        } catch (NumberFormatException e) {
            // Cuando la id ingresada es diferente a un número.
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("ID incorrecta.");
            alerta.setContentText("La ID solo debe contener números");
            alerta.showAndWait();;
            return;
        }
            
        ClubDeportivo club = ClubDeportivo.getInstancia();
        Administrador admin = club.getAdmin();
        if (admin.getNombre().equals(nombreAdmin) && admin.getNumeroId() == idAdmin) {
            try {
                ManejadorEscenas.cambiarEscena("clubDeportivo");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error al cambiar de escena.");
            }
        }
        else{
            Alert alerta = new Alert(AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Los Datos son incorrectos.");
            alerta.setContentText("Ingrese los datos de nuevo.");
            alerta.showAndWait();
        }
    }
    @FXML
    void initialize() {

    }

}
package com.lab1;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

import com.lab1.model.Administrador;
import com.lab1.model.ClubDeportivo;
import com.lab1.model.Etapa;
import com.lab1.model.Deporte;
import com.lab1.model.DeporteCRUD;
import com.lab1.model.Entrenador;
import com.lab1.model.EntrenadorCRUD;
import com.lab1.model.Estado;
import com.lab1.model.Miembro;
import com.lab1.model.MiembroCRUD;
import com.lab1.model.NivelDificultad;
import com.lab1.model.SesionEntrenamiento;
import com.lab1.model.SesionEntrenamientoCRUD;
import com.lab1.model.Utilidades.Utilidades;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.*;

public class ClubDeportivoController {

    @FXML private Text tituloText;
    
    @FXML private TableView<Deporte> deportesTableView;
    @FXML private TableView<Entrenador> entrenadoresTableView;
    @FXML private TableView<Miembro> miembrosTableView;
    @FXML private TableView<SesionEntrenamiento> sesionesTableView;

    @FXML private TableColumn<Deporte, String> nombreDeporteColumn;
    @FXML private TableColumn<Deporte, String> descripcionDeporteColumn;
    @FXML private TableColumn<Deporte, String> dificultadDeporteColumn;

    @FXML private TableColumn<Entrenador, String> nombreEntrenadorColumn;
    @FXML private TableColumn<Entrenador, String> especialidadEntrenadorColumn;
    @FXML private TableColumn<Entrenador, String> sesionesEntrenadorColumn;

    @FXML private TableColumn<Miembro, String> nombreMiembroColumn;
    @FXML private TableColumn<Miembro, String> emailMiembroColumn;
    @FXML private TableColumn<Miembro, String> identificacionMiembroColumn;
    @FXML private TableColumn<Miembro, String> etapaMiembroColumn;
    
    @FXML private TableColumn<SesionEntrenamiento, String> fechaSesionEntrenamientoColumn;
    @FXML private TableColumn<SesionEntrenamiento, String> entrenadorSesionEntrenamientoColumn;
    @FXML private TableColumn<SesionEntrenamiento, String> deporteSesionEntrenamientoColumn;
    @FXML private TableColumn<SesionEntrenamiento, String> estadoSesionEntrenamientoColumn;
    
    @FXML private ComboBox<String> traducirComboBox;

    @FXML private Button addDeporteButton;
    @FXML private Button addEntrenadorButton;
    @FXML private Button addMiembroButton;
    @FXML private Button programarSesionButton;

    @FXML private Button removeButton;
    
    @FXML private Button actualizarButton;
    @FXML private Button verInfoSesionButton;

    private DeporteCRUD deporteCRUD;
    private EntrenadorCRUD entrenadorCRUD;
    private MiembroCRUD miembroCRUD;
    private SesionEntrenamientoCRUD sesionEntrenamientoCRUD;
    private ClubDeportivo clubDeportivo = ClubDeportivo.getInstancia();
    private Administrador administrador = clubDeportivo.getAdmin();

    public void initialize() throws IOException {
        
        entrenadorCRUD = new EntrenadorCRUD();
        sesionEntrenamientoCRUD = new SesionEntrenamientoCRUD(entrenadorCRUD);
        deporteCRUD = new DeporteCRUD(entrenadorCRUD, sesionEntrenamientoCRUD);
        entrenadorCRUD.setSesionEntrenamientoCRUD(sesionEntrenamientoCRUD);
        miembroCRUD = new MiembroCRUD(sesionEntrenamientoCRUD);

        Utilidades.getInstance().inicializarLogger();
        configurarTablas();
        textoInterfaz();
        deseleccionar();
        inicializarDatos();
        cargarDatos();
        
        traducirComboBox.setItems(FXCollections.observableArrayList("es", "en"));

        traducirComboBox.setOnAction(e -> cambiarIdioma(traducirComboBox.getValue()));
        addDeporteButton.setOnAction(e -> agregarDeporte());
        addEntrenadorButton.setOnAction(e -> agregarEntrenador());
        addMiembroButton.setOnAction(e -> agregarMiembro());
        programarSesionButton.setOnAction(e -> programarSesion());

        removeButton.setOnAction(e -> eliminar());


        actualizarButton.setOnAction(e-> actualizar());
        verInfoSesionButton.setOnAction(e -> mostrarInformacionSesion());
        
    }

    private void textoInterfaz(){

        tituloText.setText(Utilidades.getInstance().getBundle().getString("titulo"));

        addDeporteButton.setText(Utilidades.getInstance().getBundle().getString("agregar_deporte_boton"));
        addEntrenadorButton.setText(Utilidades.getInstance().getBundle().getString("agregar_entrenador_boton"));
        addMiembroButton.setText(Utilidades.getInstance().getBundle().getString("agregar_miembro_boton"));
        programarSesionButton.setText(Utilidades.getInstance().getBundle().getString("programar_sesión_boton"));
    }
    private void configurarTablas(){

        //Columnas deportes información
        nombreDeporteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        descripcionDeporteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));
        dificultadDeporteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDificultad().toString()));
        
        //Limpia la seleccion de la tabla
        deportesTableView.getSelectionModel().clearSelection();

        //Columnas entrenador información
        nombreEntrenadorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        especialidadEntrenadorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecialidad().getNombre() + " " +cellData.getValue().getEspecialidad().getDificultad()));
        sesionesEntrenadorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSesionesEntrenaminetos().stream()
        .map(sesion -> String.format("Fecha: %s, Estado: %s",
            sesion.getFecha().toString(),
            sesion.getEstado().toString()))
        .collect(Collectors.joining("\n"))));


        //Limpia la seleccion de la tabla
        entrenadoresTableView.getSelectionModel().clearSelection();

        //Columnas miembro información
        nombreMiembroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        emailMiembroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        identificacionMiembroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroId())));
        etapaMiembroColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtapa().toString()));

        //Limpia la seleccion de la tabla
        miembrosTableView.getSelectionModel().clearSelection();

        //Columnas sesion información
        fechaSesionEntrenamientoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getFecha())));
        entrenadorSesionEntrenamientoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEntrenador().getNombre()));
        deporteSesionEntrenamientoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDeporte().getNombre()+ " " +cellData.getValue().getDeporte().getDificultad()));
        estadoSesionEntrenamientoColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEstado().toString()));
    
        //Limpia la seleccion de la tabla
        sesionesTableView.getSelectionModel().clearSelection();
        
    }


    private void inicializarDatos() {

        // generar deportes
        Deporte futbol = new Deporte("Fútbol", "Deporte en equipo", NivelDificultad.ALTO);
        Deporte tenis = new Deporte("Tenis", "Deporte de raqueta", NivelDificultad.MEDIO);
        Deporte natacion = new Deporte("Natación", "Deporte acuático", NivelDificultad.BAJO);

        administrador.crearDeporte(deporteCRUD, futbol);
        administrador.crearDeporte(deporteCRUD, tenis);
        administrador.crearDeporte(deporteCRUD, natacion);

        // Crear entrenadores
        Entrenador entrenador1 = new Entrenador("Carlos Pérez", futbol);
        Entrenador entrenador2 = new Entrenador("Laura Gómez", tenis);
        Entrenador entrenador3 = new Entrenador("Ana Martínez", natacion);

        administrador.crearEntrenador(entrenadorCRUD, entrenador1);
        administrador.crearEntrenador(entrenadorCRUD, entrenador2);
        administrador.crearEntrenador(entrenadorCRUD, entrenador3);

        // Crear miembros
        Miembro miembro1 = new Miembro("Juan López", "juan.lopez@example.com", 123, Etapa.JUVENIL);
        Miembro miembro2 = new Miembro("María Díaz", "maria.diaz@example.com", 124, Etapa.ADULTO);
        Miembro miembro3 = new Miembro("Luis García", "luis.garcia@example.com", 125, Etapa.ADULTO);
        Miembro miembro4 = new Miembro("Lucía Fernández", "lucia.fernandez@example.com", 126, Etapa.JUVENIL);

        administrador.crearMiembro(miembroCRUD, miembro1);
        administrador.crearMiembro(miembroCRUD, miembro2);
        administrador.crearMiembro(miembroCRUD, miembro3);
        administrador.crearMiembro(miembroCRUD, miembro4);

        // Crear sesiones de entrenamiento
        SesionEntrenamiento sesion1 = new SesionEntrenamiento(LocalDate.now().plusDays(1),30, Estado.PROGRAMADA, futbol,  entrenador1);
        SesionEntrenamiento sesion2 = new SesionEntrenamiento(LocalDate.now().plusDays(2),30, Estado.PROGRAMADA, tenis,  entrenador2);
        SesionEntrenamiento sesion3 = new SesionEntrenamiento(LocalDate.now().plusDays(3),30, Estado.PROGRAMADA, natacion, entrenador3);
        // Inscribir miembros a las sesiones
        sesion1.inscribirMiembro(miembro2);
        sesion2.inscribirMiembro(miembro3);
        sesion3.inscribirMiembro(miembro3);

        administrador.programarSesion(sesionEntrenamientoCRUD, sesion1);
        administrador.programarSesion(sesionEntrenamientoCRUD, sesion2);
        administrador.programarSesion(sesionEntrenamientoCRUD, sesion3);

    }

    private void mostrarInformacionSesion() {
        SesionEntrenamiento sesionSeleccionada = sesionesTableView.getSelectionModel().getSelectedItem();
        if (sesionSeleccionada != null) {
  
            Dialog<Miembro> dialogoInformacion = new Dialog<>();
            dialogoInformacion.setTitle("Información de la Sesión");

            Label fechaLabel = new Label("Fecha:");
            TextField fechaField = new TextField(sesionSeleccionada.getFecha().toString());
            fechaField.setEditable(false);

            Label entrenadorLabel = new Label("Entrenador:");
            TextField entrenadorField = new TextField(sesionSeleccionada.getEntrenador().getNombre());
            entrenadorField.setEditable(false);

            Label deporteLabel = new Label("Deporte:");
            TextField deporteField = new TextField(sesionSeleccionada.getDeporte().getNombre());
            deporteField.setEditable(false);

            Label miembrosInsLabel = new Label("Miembros:");


            TableView<Miembro> miembrosInsTableView = new TableView<>();
            miembrosInsTableView.setEditable(false);
            miembrosInsTableView.setPrefHeight(200);

            TableColumn<Miembro, String> nombreColumn = new TableColumn<>("Nombre");
            nombreColumn.setPrefWidth(150);
            TableColumn<Miembro, String> emailColumn = new TableColumn<>("Email");
            emailColumn.setPrefWidth(200);
            TableColumn<Miembro, String> identificacionColumn = new TableColumn<>("Identificación");
            identificacionColumn.setPrefWidth(150);
            TableColumn<Miembro, String> etapaColumn = new TableColumn<>("Etapa");
            etapaColumn.setPrefWidth(100);

            nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
            emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
            identificacionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getNumeroId())));
            etapaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEtapa().toString()));

            miembrosInsTableView.getColumns().add(nombreColumn);
            miembrosInsTableView.getColumns().add(emailColumn);
            miembrosInsTableView.getColumns().add(identificacionColumn);
            miembrosInsTableView.getColumns().add(etapaColumn);

            miembrosInsTableView.getItems().clear();
            miembrosInsTableView.getItems().addAll(sesionSeleccionada.getMiembros());


            Label miembrosLabel = new Label("Seleccionar Miembro para Inscribir:");
            ComboBox<Miembro> miembrosIncribirComboBox = new ComboBox<>();

            for (Miembro miembro : miembroCRUD.listar()) {
                if (!sesionSeleccionada.getMiembros().contains(miembro)) {
                    miembrosIncribirComboBox.getItems().add(miembro);
                }
            }

            VBox content = new VBox(10, fechaLabel, fechaField, entrenadorLabel, entrenadorField, deporteLabel, deporteField, miembrosInsLabel, miembrosInsTableView, miembrosLabel, miembrosIncribirComboBox);
            dialogoInformacion.getDialogPane().setContent(content);

            ButtonType addButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
            ButtonType removeButtonType = new ButtonType("Eliminar", ButtonBar.ButtonData.LEFT);

            dialogoInformacion.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType, removeButtonType);

            dialogoInformacion.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    Miembro miembroSeleccionado = miembrosIncribirComboBox.getValue();
                    if (miembroSeleccionado != null) {
                        return miembroSeleccionado;
                    }
                } else if (dialogButton == removeButtonType) {
                    Miembro miembroAEliminar = miembrosInsTableView.getSelectionModel().getSelectedItem();
                    if (miembroAEliminar != null) {
                        sesionSeleccionada.getMiembros().remove(miembroAEliminar);
                        miembrosInsTableView.getItems().remove(miembroAEliminar);
                    }
                }
                return null;
            });


            dialogoInformacion.showAndWait().ifPresent(miembroSeleccionado -> {
                try{
                    if (miembroSeleccionado != null) {
                        sesionSeleccionada.inscribirMiembro(miembroSeleccionado);
                        cargarDatos(); // Recargar los datos después de agregar el nuevo miembro 
                    }
                } catch (IllegalArgumentException e){
                    Alert alerta = new Alert(AlertType.ERROR);
                    alerta.setTitle("Error");
                    alerta.setHeaderText("Juvenil no puede ingresar a categoria alta");
                    alerta.setContentText("Ingrese a una categoria menor");
                    alerta.showAndWait();
                }
            });
        } else {
            // Mostrar un mensaje si no hay sesión seleccionada
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("No se seleccionó ninguna sesión");
            alerta.setContentText("Por favor, seleccione una sesión de la lista para ver la información.");
            alerta.showAndWait();
            
            //logger
            Utilidades.getInstance().escribirLog(ClubDeportivoController.class, "Por favor, seleccione una sesión de la lista para ver la información.", Level.WARNING );
        }
    }

    private void cargarDatos() {
        deportesTableView.getItems().clear();
        entrenadoresTableView.getItems().clear();
        miembrosTableView.getItems().clear();
        sesionesTableView.getItems().clear();

        deportesTableView.getItems().addAll(deporteCRUD.listar());
        entrenadoresTableView.getItems().addAll(entrenadorCRUD.listar());
        miembrosTableView.getItems().addAll(miembroCRUD.listar());
        sesionesTableView.getItems().addAll(sesionEntrenamientoCRUD.listar());
    }

    private void agregarDeporte() {
        Dialog<Deporte> dialog = new Dialog<>();
        dialog.setTitle("Agregar Deporte");
        dialog.setHeaderText("Ingrese la información del nuevo deporte");

        // Configurar los campos de entrada
        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();
        Label descripcionLabel = new Label("Descripción:");
        TextField descripcionField = new TextField();
        Label dificultadLabel = new Label("Nivel de Dificultad:");
        ComboBox<NivelDificultad> dificultadComboBox = new ComboBox<>();
        dificultadComboBox.getItems().addAll(NivelDificultad.values());

        VBox content = new VBox(10, nombreLabel, nombreField, descripcionLabel, descripcionField, dificultadLabel, dificultadComboBox);
        dialog.getDialogPane().setContent(content);

        ButtonType addButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String nombre = nombreField.getText();
                String descripcion = descripcionField.getText();
                NivelDificultad dificultad = dificultadComboBox.getValue();
                return new Deporte(nombre, descripcion, dificultad);
            }
            return null;
        });

        // Mostrar el diálogo y procesar la entrada
        dialog.showAndWait().ifPresent(deporte -> {
            administrador.crearDeporte(deporteCRUD, deporte);
            cargarDatos(); // Recargar los datos después de agregar el nuevo deporte
        });
        
    }

    private void agregarEntrenador() {
        Dialog<Entrenador> dialog = new Dialog<>();
        dialog.setTitle("Agregar Entrenador");
        dialog.setHeaderText("Ingrese la información del nuevo entrenador");

        // Configurar los campos de entrada
        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();
        Label especialidadLabel = new Label("Especialidad:");
        ComboBox<Deporte> especialidadComboBox = new ComboBox<>();
        for (int i = 0; i < deporteCRUD.listar().size(); i++) {
            especialidadComboBox.getItems().addAll(deporteCRUD.listar().get(i));
        } 
        VBox content = new VBox(10, nombreLabel, nombreField, especialidadLabel, especialidadComboBox);
        dialog.getDialogPane().setContent(content);

        ButtonType addButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String nombre = nombreField.getText();  
                Deporte especialidad = especialidadComboBox.getValue();
                return new Entrenador(nombre, especialidad);

            }
            return null;
        });

        // Mostrar el diálogo y procesar la entrada
        dialog.showAndWait().ifPresent(entrenador -> {
            administrador.crearEntrenador(entrenadorCRUD, entrenador);
            cargarDatos(); 
        });
    }

    private void agregarMiembro() {
        Dialog<Miembro> dialog = new Dialog<>();
        dialog.setTitle("Agregar Miembro");
        dialog.setHeaderText("Ingrese la información del nuevo miembro");
        Label nombreLabel = new Label("Nombre:");
        TextField nombreField = new TextField();
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        Label identificacionLabel = new Label("Identificacion:");
        TextField identificacionField = new TextField();
        ComboBox<Etapa> etapaComboBox = new ComboBox<>();
        etapaComboBox.getItems().addAll(Etapa.values());

        VBox content = new VBox(10, nombreLabel, nombreField, emailLabel, emailField, identificacionLabel, identificacionField, etapaComboBox);
        dialog.getDialogPane().setContent(content);

        ButtonType addButtonType = new ButtonType("Agregar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                String nombre = nombreField.getText();
                String email = emailField.getText();
                int identificacion = Integer.parseInt(identificacionField.getText());
                Etapa etapa = etapaComboBox.getValue();
                return new Miembro(nombre, email, identificacion, etapa);
            }
              
            return null;
        });
        dialog.showAndWait().ifPresent(miembro -> {
            administrador.crearMiembro(miembroCRUD, miembro);
            cargarDatos(); 
        });
    }

    private void programarSesion() {
        Dialog<SesionEntrenamiento> dialog = new Dialog<>();
        dialog.setTitle("Programar Sesión de Entrenamiento");
        dialog.setHeaderText("Ingrese la información para programar una nueva sesión de entrenamiento");
    
        // Configurar los campos de entrada
        Label fechaLabel = new Label("Fecha:");
        DatePicker fechaPicker = new DatePicker();
        Label duracionLabel = new Label("Duración (minutos):");
        TextField duracionField = new TextField();
        Label estadoLabel = new Label("Estado:");
        ComboBox<Estado> estadoComboBox = new ComboBox<>();
        estadoComboBox.getItems().addAll(Estado.values());
        Label entrenadorLabel = new Label("Entrenador:");
        ComboBox<Entrenador> entrenadorComboBox = new ComboBox<>();
        entrenadorComboBox.getItems().addAll(entrenadorCRUD.listar());
     
        VBox content = new VBox(10, fechaLabel, fechaPicker, duracionLabel, duracionField, estadoLabel, estadoComboBox, entrenadorLabel, entrenadorComboBox);
        dialog.getDialogPane().setContent(content);
    
        ButtonType programarButtonType = new ButtonType("Programar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, programarButtonType);
    
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == programarButtonType) {
                LocalDate fecha = fechaPicker.getValue();
                // Se revisa la fecha para ver que no sea anterior a la actual
                if (fecha.isBefore(LocalDate.now())) {
                    Alert alerta = new Alert(AlertType.ERROR);
                    alerta.setTitle("Error");
                    alerta.setHeaderText("La Fecha es anterior a la fecha actual.");
                    alerta.setContentText("Ingrese la fecha de nuevo.");
                    alerta.showAndWait();
                    throw new IllegalArgumentException("La fecha seleccionada no puede ser anterior a la fecha actual.");
                }
                int duracion = Integer.parseInt(duracionField.getText());
                Estado estado = estadoComboBox.getValue();
                Entrenador entrenador = entrenadorComboBox.getValue();
                Deporte deporte = entrenadorComboBox.getValue().getEspecialidad();
                return new SesionEntrenamiento(fecha, duracion, estado, deporte, entrenador);
            }
            return null;
        });
    
        // Mostrar el diálogo y procesar la entrada
        dialog.showAndWait().ifPresent(sesion -> {
            administrador.programarSesion(sesionEntrenamientoCRUD, sesion);
            cargarDatos(); 
        });
    }

    public void actualizar(){
        Deporte deporteSeleccionado = deportesTableView.getSelectionModel().getSelectedItem();
        Entrenador entrenadorSeleccionado = entrenadoresTableView.getSelectionModel().getSelectedItem();
        Miembro miembroSeleccionado = miembrosTableView.getSelectionModel().getSelectedItem();
        SesionEntrenamiento sesionSeleccionada = sesionesTableView.getSelectionModel().getSelectedItem();

        if(deporteSeleccionado != null){
            Dialog<Deporte> dialog = new Dialog<>();
            dialog.setTitle("Actualizar Deporte");
            dialog.setHeaderText("Edite la infromacion que desea cambiar");

            // Configurar los campos de entrada
            Label nombreLabel = new Label("Nombre:");
            TextField nombreField = new TextField(deporteSeleccionado.getNombre());
            Label descripcionLabel = new Label("Descripción:");
            TextField descripcionField = new TextField(deporteSeleccionado.getDescripcion());
            Label dificultadLabel = new Label("Nivel de Dificultad:");
            TextField dificultadField= new TextField(deporteSeleccionado.getDificultad().toString());
            dificultadField.setEditable(false);

            VBox content = new VBox(10, nombreLabel, nombreField, descripcionLabel, descripcionField, dificultadLabel, dificultadField);
            dialog.getDialogPane().setContent(content);

            ButtonType addButtonType = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    // Actualizar el deporte seleccionado con los nuevos valores
                    deporteSeleccionado.setNombre(nombreField.getText());
                    deporteSeleccionado.setDescripcion(descripcionField.getText());
                    return deporteSeleccionado; // Devolver el deporte actualizado
                }
                return null;
            });

            // Mostrar el diálogo y procesar la entrada
            dialog.showAndWait().ifPresent(deporte -> {
                administrador.actualizarDeporte(deporteCRUD, deporteSeleccionado);
                deportesTableView.getSelectionModel().clearSelection();
                cargarDatos(); // Recargar los datos después de actualizar el deporte
            });
        }
        else if(entrenadorSeleccionado != null){
            Dialog<Entrenador> dialog = new Dialog<>();
            dialog.setTitle("Actualizar Entrenador");
            dialog.setHeaderText("Edite la infromacion que desea cambiar");

            // Configurar los campos de entrada
            Label nombreLabel = new Label("Nombre:");
            TextField nombreField = new TextField(entrenadorSeleccionado.getNombre());
            Label especialidadLabel = new Label("Especialidad:");
            ComboBox<Deporte> especialidadComboBox = new ComboBox<>();
            especialidadComboBox.getItems().addAll(deporteCRUD.listar());
    
            VBox content = new VBox(10, nombreLabel, nombreField, especialidadLabel, especialidadComboBox);
            dialog.getDialogPane().setContent(content);

            ButtonType addButtonType = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    // Actualizar el entrenador seleccionado con los nuevos valores
                    entrenadorSeleccionado.setNombre(nombreField.getText());
                    entrenadorSeleccionado.setEspecialidad(especialidadComboBox.getValue());
                    return entrenadorSeleccionado; // Devolver el entrenador actualizado
                }
                return null;
            });

            // Mostrar el diálogo y procesar la entrada
            dialog.showAndWait().ifPresent(entrenador -> {
                administrador.actualizarEntrenador(entrenadorCRUD, entrenadorSeleccionado);
                entrenadoresTableView.getSelectionModel().clearSelection();
                cargarDatos(); // Recargar los datos después de actualizar el entrenador
            });
        }
        else if(miembroSeleccionado != null){
            Dialog<Miembro> dialog = new Dialog<>();
            dialog.setTitle("Actualizar Miembro");
            dialog.setHeaderText("Edite la infromacion que desea cambiar");

            // Configurar los campos de entrada
            Label nombreLabel = new Label("Nombre:");
            TextField nombreField = new TextField(miembroSeleccionado.getNombre());
            Label emailLabel = new Label("Email:");
            TextField emailField = new TextField(miembroSeleccionado.getEmail());
            Label etapaLabel = new Label("Etapa:");
            TextField etapaField= new TextField(miembroSeleccionado.getEtapa().toString());
            etapaField.setEditable(false);
            ComboBox<Etapa> etapaComboBox = new ComboBox<>();
            etapaComboBox.getItems().addAll(Etapa.values());
            Label numeroIdLabel = new Label("Numero ID:");
            TextField numeroIdField = new TextField(Integer.toString(miembroSeleccionado.getNumeroId()));
    
            VBox content = new VBox(10, nombreLabel, nombreField, emailLabel, emailField, etapaLabel, etapaField, numeroIdLabel, numeroIdField);
            dialog.getDialogPane().setContent(content);

            ButtonType addButtonType = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    // Actualizar el miembro seleccionado con los nuevos valores
                    miembroSeleccionado.setNombre(nombreField.getText());
                    miembroSeleccionado.setEmail(emailField.getText());
                    if (etapaComboBox.getValue() != null) {
                    miembroSeleccionado.setEtapa(etapaComboBox.getValue());
                    }
                    miembroSeleccionado.setNumeroId(Integer.parseInt(numeroIdField.getText()));
                    return miembroSeleccionado; // Devolver el miembro actualizado
                }
                return null;
            });

            // Mostrar el diálogo y procesar la entrada
            dialog.showAndWait().ifPresent(miembro -> {
                administrador.actualizarMiembro(miembroCRUD, miembroSeleccionado);
                cargarDatos(); // Recargar los datos después de actualizar el miembro
            });
        }
        else if(sesionSeleccionada != null){
            Dialog<SesionEntrenamiento> dialog = new Dialog<>();
            dialog.setTitle("Actualizar Sesion de Entrenamiento");
            dialog.setHeaderText("Edite la información que desea cambiar");
        
            // Configurar los campos de entrada
            Label fechaLabel = new Label("Fecha:");
            LocalDate fecha = sesionSeleccionada.getFecha();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");
            String fechaTexto = fecha.format(formatter);
            Label fechaActualLabel = new Label(fechaTexto);
        
            Label fechaNuevaLabel = new Label("Ingrese la nueva fecha:");
            DatePicker fechaPicker = new DatePicker();
        
            Label duracionLabel = new Label("Duracion:");
            TextField duracionField = new TextField(Integer.toString(sesionSeleccionada.getDuracion()));
        
            Label estadoLabel = new Label("Estado:");
            ComboBox<Estado> estadoComboBox = new ComboBox<>();
            estadoComboBox.getItems().addAll(Estado.values());
        
            Label entrenadorLabel = new Label("Entrenador:");
            ComboBox<Entrenador> entrenadorComboBox = new ComboBox<>();
            entrenadorComboBox.getItems().addAll(entrenadorCRUD.listar());
        
            VBox content = new VBox(10, fechaLabel, fechaActualLabel, fechaNuevaLabel, fechaPicker, duracionLabel, duracionField, estadoLabel, estadoComboBox, entrenadorLabel, entrenadorComboBox);
            dialog.getDialogPane().setContent(content);
        
            ButtonType addButtonType = new ButtonType("Actualizar", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, addButtonType);
        
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButtonType) {
                    // Actualizar la fecha y duración
                    if (fechaPicker.getValue() != null) {
                        sesionSeleccionada.setFecha(fechaPicker.getValue());
                    }
                    sesionSeleccionada.setDuracion(Integer.parseInt(duracionField.getText()));
        
                    // Actualizar el estado
                    if (estadoComboBox.getValue() != null) {
                        sesionSeleccionada.setEstado(estadoComboBox.getValue());
                    }
        
                    // Actualizar el entrenador y el deporte
                    Entrenador entrenadorAnterior = sesionSeleccionada.getEntrenador();
                    if (entrenadorComboBox.getValue() != null && !entrenadorComboBox.getValue().equals(entrenadorAnterior)) {
                        // Remover la sesión del entrenador anterior
                        entrenadorAnterior.getSesionesEntrenaminetos().remove(sesionSeleccionada);
        
                        // Asignar el nuevo entrenador y actualizar su lista de sesiones
                        Entrenador nuevoEntrenador = entrenadorComboBox.getValue();
                        sesionSeleccionada.setEntrenador(nuevoEntrenador);
                        sesionSeleccionada.setDeporte(nuevoEntrenador.getEspecialidad());
                        nuevoEntrenador.getSesionesEntrenaminetos().add(sesionSeleccionada);
                    }
                    return sesionSeleccionada;
                }
                return null;
            });
        
            dialog.showAndWait().ifPresent(sesion -> {
                administrador.actualizarSesion(sesionEntrenamientoCRUD, sesionSeleccionada);
                cargarDatos(); // Recargar los datos después de actualizar la sesión
            });
        }        
        else {
            // Mostrar un mensaje si no hay sesión seleccionada
            Alert alerta = new Alert(AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText("No se selecciono ningun elemento");
            alerta.setContentText("Por favor, seleccione un deporte o entrenador o miembro o sesion de las tablas para actualizar.");
            alerta.showAndWait();
        }
    }
    public void eliminar(){
        Deporte deporteSeleccionado = deportesTableView.getSelectionModel().getSelectedItem();
        Entrenador entrenadorSeleccionado = entrenadoresTableView.getSelectionModel().getSelectedItem();
        Miembro miembroSeleccionado = miembrosTableView.getSelectionModel().getSelectedItem();
        SesionEntrenamiento sesionSeleccionada = sesionesTableView.getSelectionModel().getSelectedItem();

        if (deporteSeleccionado != null) {
            administrador.eliminarDeporte(deporteCRUD, deporteSeleccionado);
            cargarDatos(); // Recargar los datos después de eliminar el deporte
        } else if (entrenadorSeleccionado != null) {
            administrador.eliminarEntrenador(entrenadorCRUD, entrenadorSeleccionado);
            cargarDatos(); // Recargar los datos después de eliminar el entrenador
        } else if (miembroSeleccionado != null) {
            administrador.eliminarMiembro(miembroCRUD, miembroSeleccionado);
            cargarDatos(); // Recargar los datos después de eliminar el miembro
        } else if (sesionSeleccionada != null) {
            administrador.eliminarSesion(sesionEntrenamientoCRUD, sesionSeleccionada);
            cargarDatos(); // Recargar los datos después de eliminar la sesión
        } else {
            // Mostrar un mensaje si no hay ningún elemento seleccionado
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Advertencia");
            alerta.setHeaderText(Utilidades.getInstance().getBundle().getString("eliminar_seleccion"));
            alerta.setContentText("Por favor, seleccione un deporte, entrenador, miembro o sesión de las tablas para eliminar.");
            alerta.showAndWait();
        }
    }

    private void deseleccionar() {
        deportesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                entrenadoresTableView.getSelectionModel().clearSelection();
                miembrosTableView.getSelectionModel().clearSelection();
                sesionesTableView.getSelectionModel().clearSelection();
            }
        });
    
        entrenadoresTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deportesTableView.getSelectionModel().clearSelection();
                miembrosTableView.getSelectionModel().clearSelection();
                sesionesTableView.getSelectionModel().clearSelection();
            }
        });
    
        miembrosTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deportesTableView.getSelectionModel().clearSelection();
                entrenadoresTableView.getSelectionModel().clearSelection();
                sesionesTableView.getSelectionModel().clearSelection();
            }
        });
    
        sesionesTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deportesTableView.getSelectionModel().clearSelection();
                entrenadoresTableView.getSelectionModel().clearSelection();
                miembrosTableView.getSelectionModel().clearSelection();
            }
        });
    }
    public void cambiarIdioma(String codigoIdioma) {
        Locale nuevoLocale = new Locale(codigoIdioma);
        Utilidades.getInstance().setLocale(nuevoLocale);
        textoInterfaz();
    }


    
    
}

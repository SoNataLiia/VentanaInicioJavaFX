package app;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfesorView extends VBox {

    private final ComboBox<String> cmbModulos = new ComboBox<>();
    private final TableView<FilaNota> table = new TableView<>();

    public ProfesorView(Stage stage, String profesor) {

        setPadding(new Insets(20));
        setSpacing(15);

        Label lblTitulo = new Label("Bienvenido profesor: " + profesor);

        cmbModulos.setPromptText("Seleccione un módulo");
        cargarModulos(profesor);

        // *** TABLA ***
        TableColumn<FilaNota, String> colAlumno = new TableColumn<>("Alumno");
        colAlumno.setCellValueFactory(new PropertyValueFactory<>("alumno"));

        TableColumn<FilaNota, Double> colNota = new TableColumn<>("Nota");
        colNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        table.getColumns().addAll(colAlumno, colNota);
        table.setEditable(true);

        cmbModulos.setOnAction(e ->
                cargarAlumnos(cmbModulos.getValue())
        );

        Button btnGuardar = new Button("GUARDAR NOTAS");
        btnGuardar.setOnAction(e -> guardarNotas());

        Button btnVolver = new Button("VOLVER");
        btnVolver.setOnAction(e ->
                stage.setScene(new Scene(new LoginView(stage), 760, 380))
        );

        getChildren().addAll(
                lblTitulo,
                cmbModulos,
                table,
                btnGuardar,
                btnVolver
        );
    }

    // *** MODULOS DEL PROFESOR ***
    private void cargarModulos(String profesor) {
        try (Connection con = DB.connect()) {
            PreparedStatement ps = con.prepareStatement("""
                SELECT m.nombre
                FROM modulos m
                JOIN usuarios u ON u.id = m.profesor_id
                WHERE u.nombre = ?
            """);
            ps.setString(1, profesor);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmbModulos.getItems().add(rs.getString("nombre"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ***ALUMNOS DEL MODULO ***
    private void cargarAlumnos(String modulo) {
        ObservableList<FilaNota> datos = FXCollections.observableArrayList();

        try (Connection con = DB.connect()) {
            PreparedStatement ps = con.prepareStatement("""
                SELECT u.nombre AS alumno, mat.nota
                FROM matriculas mat
                JOIN usuarios u ON u.id = mat.alumno_id
                JOIN modulos m ON m.id = mat.modulo_id
                WHERE m.nombre = ?
            """);
            ps.setString(1, modulo);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                datos.add(new FilaNota(
                        rs.getString("alumno"),
                        rs.getDouble("nota")
                ));
            }

            table.setItems(datos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // *** GUARDAR NOTAS ***
    private void guardarNotas() {
        String modulo = cmbModulos.getValue();
        if (modulo == null) return;

        try (Connection con = DB.connect()) {
            for (FilaNota f : table.getItems()) {
                PreparedStatement ps = con.prepareStatement("""
                    UPDATE matriculas
                    SET nota = ?
                    WHERE alumno_id = (
                        SELECT id FROM usuarios WHERE nombre = ?
                    )
                    AND modulo_id = (
                        SELECT id FROM modulos WHERE nombre = ?
                    )
                """);
                ps.setDouble(1, f.getNota());
                ps.setString(2, f.getAlumno());
                ps.setString(3, modulo);
                ps.executeUpdate();
            }

            Alert a = new Alert(Alert.AlertType.INFORMATION,
                    "Notas guardadas correctamente");
            a.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // *** MODELO ***
    public static class FilaNota {
        private final String alumno;
        private Double nota;

        public FilaNota(String alumno, Double nota) {
            this.alumno = alumno;
            this.nota = nota;
        }

        public String getAlumno() {
            return alumno;
        }

        public Double getNota() {
            return nota;
        }

        public void setNota(Double nota) {
            this.nota = nota;
        }
    }
}

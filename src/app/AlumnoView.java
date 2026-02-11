package app;

import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class AlumnoView extends VBox {

    private final TableView<Nota> table = new TableView<>();
    private final ComboBox<String> cmbModulos = new ComboBox<>();

    public AlumnoView(Stage stage, String usuario) {

        setPadding(new Insets(20));
        setSpacing(15);

        Label lbl = new Label("Bienvenido alumno: " + usuario);

        TableColumn<Nota, String> colModulo = new TableColumn<>("Módulo");
        colModulo.setCellValueFactory(new PropertyValueFactory<>("modulo"));

        TableColumn<Nota, Double> colNota = new TableColumn<>("Nota");
        colNota.setCellValueFactory(new PropertyValueFactory<>("nota"));

        table.getColumns().addAll(colModulo, colNota);

        Button btnVolver = new Button("VOLVER");
        btnVolver.setOnAction(e ->
                stage.setScene(new Scene(new LoginView(stage), 760, 380))
        );

        getChildren().addAll(lbl, cmbModulos, table, btnVolver);

        cargarModulos(usuario);

        cmbModulos.setOnAction(e ->
                cargarNotas(usuario, cmbModulos.getValue())
        );
    }

    private void cargarModulos(String usuario) {
        try (Connection con = DB.connect()) {
            PreparedStatement ps = con.prepareStatement("""
                SELECT m.nombre
                FROM modulos m
                JOIN matriculas mat ON mat.modulo_id = m.id
                JOIN usuarios u ON u.id = mat.alumno_id
                WHERE u.nombre = ?
            """);
            ps.setString(1, usuario);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cmbModulos.getItems().add(rs.getString("nombre"));
            }

        } catch (Exception e) {
            alerta("BD Error", e.getMessage());
        }
    }

    private void cargarNotas(String usuario, String modulo) {
        ObservableList<Nota> datos = FXCollections.observableArrayList();

        try (Connection con = DB.connect()) {
            PreparedStatement ps = con.prepareStatement("""
                SELECT m.nombre AS modulo, mat.nota
                FROM matriculas mat
                JOIN modulos m ON m.id = mat.modulo_id
                JOIN usuarios u ON u.id = mat.alumno_id
                WHERE u.nombre = ? AND m.nombre = ?
            """);
            ps.setString(1, usuario);
            ps.setString(2, modulo);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                datos.add(new Nota(
                        rs.getString("modulo"),
                        rs.getDouble("nota")
                ));
            }

            table.setItems(datos);

        } catch (Exception e) {
            alerta("BD Error", e.getMessage());
        }
    }

    private void alerta(String t, String m) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(t);
        a.setHeaderText(null);
        a.setContentText(m);
        a.showAndWait();
    }

    public static class Nota {
        private final String modulo;
        private final Double nota;

        public Nota(String m, Double n) {
            modulo = m;
            nota = n;
        }

        public String getModulo() { return modulo; }
        public Double getNota() { return nota; }
    }
}

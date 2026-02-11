package app;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginView extends BorderPane {

    public LoginView(Stage stage) {

        // *** GRID FORM ***
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(40));
        grid.setHgap(15);
        grid.setVgap(15);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setMinWidth(120);
        col1.setPrefWidth(120);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setMinWidth(220);
        col2.setPrefWidth(220);
        col2.setMaxWidth(220);

        grid.getColumnConstraints().addAll(col1, col2);

        Label lblUsuario = new Label("USUARIO");
        TextField txtUsuario = new TextField();
        txtUsuario.setPrefWidth(220);

        Label lblPassword = new Label("CONTRASEÑA");
        PasswordField txtPassword = new PasswordField();
        txtPassword.setPrefWidth(220);

        Label lblRol = new Label("CARGO");
        ComboBox<String> cmbRol = new ComboBox<>();
        cmbRol.getItems().addAll("alumno", "profesor");
        cmbRol.setPrefWidth(220);

        Button btnLimpiar = new Button("LIMPIAR");
        Button btnEntrar = new Button("ENTRAR");
        Button btnSalir = new Button("SALIR");

        btnLimpiar.setPrefWidth(90);
        btnEntrar.setPrefWidth(90);
        btnSalir.setPrefWidth(90);

        HBox botones = new HBox(15, btnLimpiar, btnEntrar, btnSalir);
        botones.setAlignment(Pos.CENTER_LEFT);

        grid.add(lblUsuario, 0, 0);
        grid.add(txtUsuario, 1, 0);

        grid.add(lblPassword, 0, 1);
        grid.add(txtPassword, 1, 1);

        grid.add(lblRol, 0, 2);
        grid.add(cmbRol, 1, 2);

        grid.add(botones, 1, 3);

        // *** IMAGE ***
        ImageView logo = new ImageView(
                new Image(getClass().getResourceAsStream("/ue.png"))
        );
        logo.setFitWidth(220);
        logo.setPreserveRatio(true);

        VBox rightBox = new VBox(logo);
        rightBox.setAlignment(Pos.CENTER);
        rightBox.setPadding(new Insets(40));

        // *** ACTIONS ***
        btnSalir.setOnAction(e -> stage.close());

        btnLimpiar.setOnAction(e -> {
            txtUsuario.clear();
            txtPassword.clear();
            cmbRol.setValue(null);
        });

        btnEntrar.setOnAction(e -> {
            String usuario = txtUsuario.getText();
            String password = txtPassword.getText();
            String rol = cmbRol.getValue();

            if (usuario.isEmpty() || password.isEmpty() || rol == null) {
                alerta("Error", "Completa todos los campos");
                return;
            }

            if (LoginService.login(usuario, password, rol)) {
                if (rol.equals("profesor")) {
                    stage.setScene(new Scene(
                            new ProfesorView(stage, usuario), 760, 380
                    ));
                } else {
                    stage.setScene(new Scene(
                            new AlumnoView(stage, usuario), 760, 380
                    ));
                }
            } else {
                alerta("Error", "Datos incorrectos");
            }
        });

        // *** LAYOUT ***
        setLeft(grid);
        setRight(rightBox);
    }

    private void alerta(String titulo, String texto) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.showAndWait();
    }
}

package com.uaemex.bolis.view;

import com.uaemex.bolis.model.Boli;
import com.uaemex.bolis.model.Usuario;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AdminView {
    public record UsuarioForm(String id, String login, String password, String nombre, String rol) {}
    public record BoliForm(String id, String sabor, String precio, String stock) {}

    public static Scene scene(Usuario admin, List<Usuario> usuarios, List<Boli> bolis, String msg,
                              Consumer<UsuarioForm> addU, Consumer<UsuarioForm> editU, Consumer<String> delU,
                              Consumer<BoliForm> addB, Consumer<BoliForm> editB, Consumer<String> delB,
                              Runnable ventas, Runnable asistencias, Runnable salir) {
        TextArea u = area(usuarios.stream().map(x -> x.id() + " | " + x.nombre() + " | " + x.rol()).collect(Collectors.joining("\n")));
        TextArea b = area(bolis.stream().map(x -> x.id() + " | " + x.sabor() + " | $" + x.precio() + " | stock " + x.stock()).collect(Collectors.joining("\n")));
        TextField ui = field("ID"), ul = field("Login"), up = field("Pass"), un = field("Nombre"), ur = field("Rol");
        TextField bi = field("ID"), bs = field("Sabor"), bp = field("Precio"), bk = field("Stock");
        VBox root = new VBox(10, new Label("Administrador: " + admin.nombre()),
                new HBox(10,
                        box("Usuarios", u, new HBox(5, ui, ul, up, un, ur),
                                new HBox(5, button("Agregar", () -> addU.accept(new UsuarioForm(ui.getText(), ul.getText(), up.getText(), un.getText(), ur.getText()))),
                                        button("Editar", () -> editU.accept(new UsuarioForm(ui.getText(), ul.getText(), up.getText(), un.getText(), ur.getText()))),
                                        button("Eliminar", () -> delU.accept(ui.getText())))),
                        box("Bolis", b, new HBox(5, bi, bs, bp, bk),
                                new HBox(5, button("Agregar", () -> addB.accept(new BoliForm(bi.getText(), bs.getText(), bp.getText(), bk.getText()))),
                                        button("Editar", () -> editB.accept(new BoliForm(bi.getText(), bs.getText(), bp.getText(), bk.getText()))),
                                        button("Eliminar", () -> delB.accept(bi.getText()))))),
                new HBox(5, button("Exportar ventas", ventas), button("Exportar asistencias", asistencias), button("Salir", salir)),
                new Label(msg));
        root.setPadding(new Insets(15));
        return new Scene(root, 800, 500);
    }

    private static VBox box(String title, Node... nodes) {
        VBox box = new VBox(5, new Label(title));
        box.getChildren().addAll(nodes);
        return box;
    }

    private static TextArea area(String text) {
        TextArea a = new TextArea(text);
        a.setEditable(false);
        a.setPrefRowCount(10);
        return a;
    }

    private static TextField field(String text) {
        TextField f = new TextField();
        f.setPromptText(text);
        f.setPrefWidth(80);
        return f;
    }

    private static Button button(String text, Runnable action) {
        Button b = new Button(text);
        b.setOnAction(e -> action.run());
        return b;
    }
}

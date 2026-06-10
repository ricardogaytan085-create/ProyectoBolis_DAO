package com.uaemex.bolis.view;

import com.uaemex.bolis.model.Boli;
import com.uaemex.bolis.model.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.List;
import java.util.function.Consumer;
//Editado Gwendy
public class AdminView {
    public record UsuarioForm(String id, String login, String password, String nombre, String email, String telefono, String rol) {}
    public record BoliForm(String id, String sabor, String precio, String stock) {}

    public static Scene scene(Usuario admin, List<Usuario> usuarios, List<Boli> bolis, String msg,
                              Consumer<UsuarioForm> addU, Consumer<UsuarioForm> editU, Consumer<String> delU,
                              Consumer<BoliForm> addB, Consumer<BoliForm> editB, Consumer<String> delB,
                              Runnable ventas, Runnable asistencias, Runnable salir) {
        TextField ui = Ui.smallField("ID"), ul = Ui.smallField("Login"), up = Ui.smallField("Pass"), un = Ui.smallField("Nombre");
        TextField ue = Ui.smallField("Email"), ut = Ui.smallField("Tel"), ur = Ui.smallField("Rol");
        TextField bi = Ui.smallField("ID"), bs = Ui.smallField("Sabor"), bp = Ui.smallField("Precio"), bk = Ui.smallField("Stock");
        GridPane usuarioForm = Ui.formGrid(
                Ui.label("ID"), ui, Ui.label("Login"), ul,
                Ui.label("Password"), up, Ui.label("Nombre"), un,
                Ui.label("Email"), ue, Ui.label("Telefono"), ut,
                Ui.label("Rol"), ur);
        GridPane boliForm = Ui.formGrid(
                Ui.label("ID"), bi, Ui.label("Sabor"), bs,
                Ui.label("Precio"), bp, Ui.label("Stock"), bk);
        ListView<Usuario> lu = Ui.listView(usuarios);
        lu.getSelectionModel().selectedItemProperty().addListener((obs, old, x) -> {
            if (x == null) return;
            ui.setText(String.valueOf(x.id()));
            ul.setText(x.login());
            up.setText("");
            un.setText(x.nombre());
            ue.setText(x.email());
            ut.setText(x.telefono());
            ur.setText(x.rol());
        });
        ListView<Boli> lb = Ui.listView(bolis);
        lb.getSelectionModel().selectedItemProperty().addListener((obs, old, x) -> {
            if (x == null) return;
            bi.setText(String.valueOf(x.id()));
            bs.setText(x.sabor());
            bp.setText(String.valueOf(x.precio()));
            bk.setText(String.valueOf(x.stock()));
        });
        return Ui.scene(Ui.root(Ui.title("Administrador: " + admin.nombre()),
                Ui.row(
                        Ui.box("Usuarios", lu, usuarioForm,
                                Ui.tightRow(Ui.button("Agregar", () -> addU.accept(new UsuarioForm(ui.getText(), ul.getText(), up.getText(), un.getText(), ue.getText(), ut.getText(), ur.getText()))),
                                        Ui.button("Editar", () -> editU.accept(new UsuarioForm(ui.getText(), ul.getText(), up.getText(), un.getText(), ue.getText(), ut.getText(), ur.getText()))),
                                        Ui.button("Eliminar", () -> delU.accept(ui.getText())))),
                        Ui.box("Bolis", lb, boliForm,
                                Ui.tightRow(Ui.button("Agregar", () -> addB.accept(new BoliForm(bi.getText(), bs.getText(), bp.getText(), bk.getText()))),
                                        Ui.button("Editar", () -> editB.accept(new BoliForm(bi.getText(), bs.getText(), bp.getText(), bk.getText()))),
                                        Ui.button("Eliminar", () -> delB.accept(bi.getText()))))),
                Ui.tightRow(Ui.button("Exportar ventas", ventas), Ui.button("Exportar asistencias", asistencias), Ui.button("Salir", salir)),
                Ui.message(msg)), 800, 500);
    }
}

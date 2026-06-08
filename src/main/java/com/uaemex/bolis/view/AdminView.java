package com.uaemex.bolis.view;

import com.uaemex.bolis.model.Boli;
import com.uaemex.bolis.model.Usuario;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
        TextArea u = Ui.area(usuarios.stream().map(x -> x.id() + " | " + x.nombre() + " | " + x.rol()).collect(Collectors.joining("\n")));
        TextArea b = Ui.area(bolis.stream().map(x -> x.id() + " | " + x.sabor() + " | $" + x.precio() + " | stock " + x.stock()).collect(Collectors.joining("\n")));
        TextField ui = Ui.smallField("ID"), ul = Ui.smallField("Login"), up = Ui.smallField("Pass"), un = Ui.smallField("Nombre"), ur = Ui.smallField("Rol");
        TextField bi = Ui.smallField("ID"), bs = Ui.smallField("Sabor"), bp = Ui.smallField("Precio"), bk = Ui.smallField("Stock");
        return Ui.scene(Ui.root(Ui.title("Administrador: " + admin.nombre()),
                Ui.row(
                        Ui.box("Usuarios", u, Ui.tightRow(ui, ul, up, un, ur),
                                Ui.tightRow(Ui.button("Agregar", () -> addU.accept(new UsuarioForm(ui.getText(), ul.getText(), up.getText(), un.getText(), ur.getText()))),
                                        Ui.button("Editar", () -> editU.accept(new UsuarioForm(ui.getText(), ul.getText(), up.getText(), un.getText(), ur.getText()))),
                                        Ui.button("Eliminar", () -> delU.accept(ui.getText())))),
                        Ui.box("Bolis", b, Ui.tightRow(bi, bs, bp, bk),
                                Ui.tightRow(Ui.button("Agregar", () -> addB.accept(new BoliForm(bi.getText(), bs.getText(), bp.getText(), bk.getText()))),
                                        Ui.button("Editar", () -> editB.accept(new BoliForm(bi.getText(), bs.getText(), bp.getText(), bk.getText()))),
                                        Ui.button("Eliminar", () -> delB.accept(bi.getText()))))),
                Ui.tightRow(Ui.button("Exportar ventas", ventas), Ui.button("Exportar asistencias", asistencias), Ui.button("Salir", salir)),
                Ui.message(msg)), 800, 500);
    }
}

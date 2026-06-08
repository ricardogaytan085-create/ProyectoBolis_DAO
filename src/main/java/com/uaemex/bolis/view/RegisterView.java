package com.uaemex.bolis.view;

import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.util.function.Consumer;

public class RegisterView {
    public record Form(String login, String password, String nombres, String paterno, String materno, String fecha, String email, String telefono) {}

    public static Scene scene(String msg, Consumer<Form> registrar, Runnable cancelar) {
        TextField login = Ui.field("Usuario"), nombres = Ui.field("Nombres");
        TextField paterno = Ui.field("Apellido paterno"), materno = Ui.field("Apellido materno");
        TextField fecha = Ui.field("Fecha nacimiento yyyy-mm-dd"), email = Ui.field("Email"), telefono = Ui.field("Telefono");
        TextField password = Ui.password();
        return Ui.scene(Ui.root(Ui.title("Registro"), login, password, nombres, paterno, materno, fecha, email, telefono,
                Ui.tightRow(Ui.button("Crear cuenta", () -> registrar.accept(new Form(login.getText(), password.getText(), nombres.getText(), paterno.getText(), materno.getText(), fecha.getText(), email.getText(), telefono.getText()))),
                        Ui.button("Cancelar", cancelar)), Ui.message(msg)), 380, 330);
    }
}

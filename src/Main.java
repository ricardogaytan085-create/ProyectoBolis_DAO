public class Main {
  public static void main(String[] args) {


    BoliDAO gerente = new BoliDAO();

   // gerente.eliminarBoli(301);
   // gerente.eliminarBoli(401);
  // gerente.eliminarBoli(502);
  //  gerente.eliminarBoli(505);
  //  gerente.eliminarBoli(101);
  //  gerente.eliminarBoli(201);

    // Si quieren agregar un sabor nuevo, solo tienen que descomentar esta línea:
    // gerente.insertarBoli("Vainilla", 14.50);


    System.out.println("\n--- INVENTARIO DE BOLIS ACTUALIZADO ---");
    gerente.listarBolis();
  }
}
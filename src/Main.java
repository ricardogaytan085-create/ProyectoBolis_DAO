import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.*;

public class Main {
  //Prueba Commit
  private static final String URL = "jdbc:derby:C:/Users/Ricardo/MiProyectoDB";

  public static void main(String[] args) {
    insertarBoli("Fresa", 15.50);
  }

  public static void insertarBoli(String sabor, double precio) {
    String sql = "INSERT INTO bolis (sabor, precio) VALUES (?, ?)";

    try {
      Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
    } catch (ClassNotFoundException e) {
      System.err.println("¡No se encontró el Driver!");
    }

    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      pstmt.setString(1, sabor);
      pstmt.setDouble(2, precio);
      pstmt.executeUpdate();
      System.out.println("¡Éxito!");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
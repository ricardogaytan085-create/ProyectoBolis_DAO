import java.sql.*;

public class BoliDAO {


    private final String URL = "jdbc:derby:C:/Users/Ricardo/MiProyectoDB";


    public BoliDAO() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            System.err.println("¡No se encontró el Driver!");
        }
    }



    public void insertarBoli(String sabor, double precio) {
        String sql = "INSERT INTO bolis (sabor, precio) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, sabor);
            pstmt.setDouble(2, precio);
            pstmt.executeUpdate();
            System.out.println("¡Boli de " + sabor + " guardado con éxito!");

        } catch (SQLException e) {
            System.err.println("Error al guardar: " + e.getMessage());
        }
    }

    public void listarBolis() {
        String sql = "SELECT * FROM bolis";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            System.out.println("ID | SABOR | PRECIO | STOCK");
            System.out.println("---------------------------------");

            while (rs.next()) {
                System.out.println(rs.getInt("id") + " | " +
                        rs.getString("sabor") + " | " +
                        rs.getDouble("precio") + " | " +
                        rs.getInt("stock"));
            }

        } catch (SQLException e) {
            System.err.println("Error al leer: " + e.getMessage());
        }
    }

    public void eliminarBoli(int id) {
        String sql = "DELETE FROM bolis WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int filasBorradas = pstmt.executeUpdate();

            if (filasBorradas > 0) {
                System.out.println("¡Boli con ID " + id + " eliminado para siempre!");
            } else {
                System.out.println("No se encontró ningún boli con el ID " + id + ".");
            }

        } catch (SQLException e) {
            System.err.println("Error al intentar borrar: " + e.getMessage());
        }
    }
}
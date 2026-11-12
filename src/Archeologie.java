import java.sql.*;
import java.util.Scanner;

public class Archeologie {
        private static final String URL = "jdbc:mysql://localhost:3306/archeologie";
        private static final String USER = "root";
        private static final String PASSWORD = "";

        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                System.out.println("Připojení bylo úspěšné.");

                ukol2(conn);
                ukol3(conn, sc);
                ukol4(conn, sc);
                ukol5(conn);



            } catch (SQLException e){
                System.out.println("Chyba při připojení k databázi: " + e.getMessage());
            }
        }

        //2
        private static void ukol2(Connection conn) throws SQLException {
            System.out.println("\n--- Všechny nálezy (od nejstaršího) ---");
            String sql = "SELECT Nazev, Material, Stari FROM Nalezy ORDER BY Stari DESC";

            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                while (rs.next()) {
                    System.out.println(rs.getString("Nazev") + " material: " + rs.getString("Material") + " stáří: " + rs.getInt("Stari"));
                }
            }
        }

        //3
    private static void ukol3(Connection conn, Scanner sc) throws SQLException {
            System.out.println("\n--- Hledání pokladu ---");
            System.out.println("Zadej materiál: ");
            String material = sc.nextLine().trim();
            System.out.println("Zadej minimální hodnotu: ");
            int minHodnota = Integer.parseInt(sc.nextLine().trim());

            String sql = "SELECT Nazev, Hodnota FROM Nalezy WHERE Material = ? AND Hodnota >= ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, material);
                ps.setInt(2, minHodnota);
                try (ResultSet rs = ps.executeQuery()) {
                    boolean nalezeno = false;
                    while (rs.next()) {
                        nalezeno = true;
                        System.out.println(rs.getString("Nazev") + " - hodnota: " + rs.getInt("Hodnota"));
                    }
                    if (!nalezeno) {
                        System.out.println("Žádný předmět nebyhovuje zadaným podmínkám.");
                    }
                }
            }
    }


    //4
    private static void ukol4(Connection conn, Scanner sc) throws SQLException {
        System.out.println("\n--- Průzkum lokalit ---");
        System.out.println("Zadej název státu: ");
        String stat = sc.nextLine().trim();

        String sql = "SELECT n.Nazev AS predmet, l.Nazev AS lokalita, n.Stari " + "FROM Nalezy n JOIN Lokality l ON n.Id_lokality = l.Id_lokality " + "WHERE l.Stat = ? ORDER BY l.Nazev";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, stat);

            try (ResultSet rs = ps.executeQuery()) {
                boolean nalezeno = false;
                while (rs.next()) {
                    nalezeno = true;
                    System.out.println(rs.getString("predmet") + " - lokality: " + rs.getString("lokalita") + " - stáří: " + rs.getInt("Stari"));
                }
                if (!nalezeno) {
                    System.out.println("Pro zadaný stát nebyly nalezeny žádné nálezy.");
                }
            }
        }
    }

            //5
            private static void ukol5(Connection conn) throws SQLException {
                System.out.println("\n--- Počet nálezů podle materiálu ---");
                String sql = "SELECT Material, COUNT(*) AS pocet FROM Nalezy GROUP BY Material";
                try (Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(sql)) {
                    while (rs.next()) {
                        int pocet =  rs.getInt("pocet");
                        System.out.println(rs.getString("Material") + " - " + pocet + " " + sklonuj(pocet));
                    }
                }
            }

            private static String sklonuj(int n) {
            if (n == 1) return "nález";
            if (n >= 2 && n <= 4) return "nálezy";
            return "nálezů";
            }





}
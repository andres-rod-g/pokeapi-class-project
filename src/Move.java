import java.sql.*;
import java.util.ArrayList;

public class Move {
    private String _id;
    private String _name;
    private int _damage;

    public String getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getDamage() {
        return _damage;
    }

    public Move(String id, String name, int damage) {
        _id = id;
        _name = name;
        _damage = damage;
    }

    public static Move hydrate(ResultSet moveResult) {
        try {
            return new Move(moveResult.getString("id"), moveResult.getString("name"), moveResult.getInt("damage"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void show() {
        System.out.println("ID: " + _id);
        System.out.println("Nombre: " + _name);
        System.out.println("Daño: " + _damage);
    }

    public void showSmall() {
        System.out.println(_id + ". " + _name);
    }

    public static Move getById(String id) {
        Move move = null;
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener la información del Move
            String moveQuery = "SELECT * FROM Move WHERE id = ?";
            PreparedStatement moveStatement = connection.prepareStatement(moveQuery);
            moveStatement.setString(1, id);
            ResultSet moveResult = moveStatement.executeQuery();

            if (moveResult.next()) {
                // Crear y poblar el objeto Move
                move = Move.hydrate(moveResult);
            }

            // Cerrar ResultSet y Statement para Move
            moveResult.close();
            moveStatement.close();

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return move;
    }

    public static ArrayList<Move> getAll() {
        ArrayList<Move> moves = new ArrayList<>();
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener todos los Moves
            String moveQuery = "SELECT * FROM Move";
            Statement moveStatement = connection.createStatement();
            ResultSet moveResult = moveStatement.executeQuery(moveQuery);

            while (moveResult.next()) {
                // Crear y poblar cada objeto Move
                Move move = Move.hydrate(moveResult);
                moves.add(move);
            }

            // Cerrar ResultSet y Statement para Move
            moveResult.close();
            moveStatement.close();

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return moves;
    }
}

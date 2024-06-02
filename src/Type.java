import java.sql.*;
import java.util.ArrayList;

public class Type {
    private String _id;
    private String _name;
    private ArrayList<Move> _moves;

    public Type(String id, String name, ArrayList<Move> moves){
        _id = id;
        _name = name;
        _moves = moves;
    }

    public String getName(){
        return _name;
    }

    public String getId(){
        return _id;
    }

    public static Type hydrate(ResultSet typeResult, ResultSet movesResult){
        try {
            ArrayList<Move> moves = new ArrayList<Move>();

            while(movesResult.next()){
                Move move = Move.getById(movesResult.getString("id"));
                moves.add(move);
            }

            Type type = new Type(typeResult.getString("id"), typeResult.getString("name"), moves);
            return type;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void show(){
        System.out.println("ID: " + _id);
        System.out.println("Nombre: " + _name);
        System.out.println("Movimientos: ");
        for (Move move : _moves){
            move.show();
        }
    }

    public void showSmall(){
        System.out.println(_id + ". " + _name);
    }

    public static Type getById(String id) {
        Type type = null;
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener la información del Type
            String typeQuery = "SELECT * FROM Type WHERE id = ?";
            PreparedStatement typeStatement = connection.prepareStatement(typeQuery);
            typeStatement.setString(1, id);
            ResultSet typeResult = typeStatement.executeQuery();

            if (typeResult.next()) {
                // Consulta para obtener los movimientos del Type
                String movesQuery = "SELECT Move.id FROM Move JOIN TypeMove ON Move.id = TypeMove.moveId WHERE TypeMove.typeId = ?";
                PreparedStatement movesStatement = connection.prepareStatement(movesQuery);
                movesStatement.setString(1, id);
                ResultSet movesResult = movesStatement.executeQuery();

                // Crear y poblar el objeto Type
                type = Type.hydrate(typeResult, movesResult);

                // Cerrar ResultSets y Statements
                movesResult.close();
                movesStatement.close();
            }

            // Cerrar ResultSet y Statement para Type
            typeResult.close();
            typeStatement.close();

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return type;
    }

    public static ArrayList<Type> getAll() {
        ArrayList<Type> types = new ArrayList<>();
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener todos los Types
            String typeQuery = "SELECT * FROM Type";
            Statement typeStatement = connection.createStatement();
            ResultSet typeResult = typeStatement.executeQuery(typeQuery);

            while (typeResult.next()) {
                String id = typeResult.getString("id");

                // Consulta para obtener los movimientos del Type
                String movesQuery = "SELECT Move.id FROM Move JOIN TypeMove ON Move.id = TypeMove.moveId WHERE TypeMove.typeId = ?";
                PreparedStatement movesStatement = connection.prepareStatement(movesQuery);
                movesStatement.setString(1, id);
                ResultSet movesResult = movesStatement.executeQuery();

                // Crear y poblar el objeto Type
                Type type = Type.hydrate(typeResult, movesResult);
                types.add(type);

                // Cerrar ResultSets y Statements para movimientos
                movesResult.close();
                movesStatement.close();
            }

            // Cerrar ResultSet y Statement para Type
            typeResult.close();
            typeStatement.close();

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return types;
    }
}

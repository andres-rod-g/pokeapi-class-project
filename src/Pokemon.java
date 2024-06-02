import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Pokemon {
    private String _id;
    private String _name;
    private String _image;
    private ArrayList<Type> _types;
    private ArrayList<Move> _moves;

    public Pokemon(String id, String name, String image, ArrayList<Type> types, ArrayList<Move> moves){
        _id = id;
        _name = name;
        _image = image;
        _types = types;
        _moves = moves;
    }

    public static void hydrate(ResultSet pokemonResult, ResultSet movesResult, ResultSet typesResult){
        ArrayList<Type> types = new ArrayList<Type>();
        ArrayList<Move> moves = new ArrayList<Move>();

        try {
            while(typesResult.next()){
                Type type = new Type(typesResult.getString("id"), typesResult.getString("name"), null);
                types.add(type);
            }

            while(movesResult.next()){
                Move move = new Move(movesResult.getString("id"), movesResult.getString("name"), movesResult.getInt("damage"));
                moves.add(move);
            }

            Pokemon pokemon = new Pokemon(pokemonResult.getString("id"), pokemonResult.getString("name"), pokemonResult.getString("image"), types, moves);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void show(){
        System.out.println("ID: " + _id);
        System.out.println("Nombre: " + _name);
        System.out.println("Imagen: " + _image);
        System.out.println("Tipos: ");
        for (Type type : _types){
            System.out.println(" - " + type.getName());
        }
        System.out.println("Movimientos: ");
        for (Move move : _moves){
            System.out.println(" - " + move.getName() + " - Daño: " + move.getDamage());
        }
    }

    public void showSmall(){
        System.out.println(_id + ". " + _name);
    }

    public static Pokemon getById(String id) {
        Pokemon pokemon = null;
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener la información del Pokemon
            String pokemonQuery = "SELECT * FROM Pokemon WHERE id = ?";
            PreparedStatement pokemonStatement = connection.prepareStatement(pokemonQuery);
            pokemonStatement.setString(1, id);
            ResultSet pokemonResult = pokemonStatement.executeQuery();

            if (pokemonResult.next()) {
                // Consulta para obtener los tipos del Pokemon
                String typesQuery = "SELECT Type.id, Type.name FROM Type JOIN PokemonType ON Type.id = PokemonType.typeId WHERE PokemonType.pokemonId = ?";
                PreparedStatement typesStatement = connection.prepareStatement(typesQuery);
                typesStatement.setString(1, id);
                ResultSet typesResult = typesStatement.executeQuery();

                // Consulta para obtener los movimientos del Pokemon
                String movesQuery = "SELECT Move.id, Move.name, Move.damage FROM Move JOIN PokemonMove ON Move.id = PokemonMove.moveId WHERE PokemonMove.pokemonId = ?";
                PreparedStatement movesStatement = connection.prepareStatement(movesQuery);
                movesStatement.setString(1, id);
                ResultSet movesResult = movesStatement.executeQuery();

                // Crear y poblar el objeto Pokemon
                ArrayList<Type> types = new ArrayList<>();
                while (typesResult.next()) {
                    Type type = new Type(typesResult.getString("id"), typesResult.getString("name"), null);
                    types.add(type);
                }

                ArrayList<Move> moves = new ArrayList<>();
                while (movesResult.next()) {
                    Move move = new Move(movesResult.getString("id"), movesResult.getString("name"), movesResult.getInt("damage"));
                    moves.add(move);
                }

                pokemon = new Pokemon(pokemonResult.getString("id"), pokemonResult.getString("name"), pokemonResult.getString("image"), types, moves);

                // Cerrar ResultSets y Statements
                pokemonResult.close();
                typesResult.close();
                movesResult.close();
                pokemonStatement.close();
                typesStatement.close();
                movesStatement.close();
            }

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokemon;
    }

    public static ArrayList<Pokemon> getAll() {
        ArrayList<Pokemon> pokemons = new ArrayList<>();
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener todos los Pokemon
            String pokemonQuery = "SELECT * FROM Pokemon";
            Statement pokemonStatement = connection.createStatement();
            ResultSet pokemonResult = pokemonStatement.executeQuery(pokemonQuery);

            while (pokemonResult.next()) {
                String id = pokemonResult.getString("id");

                // Consulta para obtener los tipos del Pokemon
                String typesQuery = "SELECT Type.id, Type.name FROM Type JOIN PokemonType ON Type.id = PokemonType.typeId WHERE PokemonType.pokemonId = ?";
                PreparedStatement typesStatement = connection.prepareStatement(typesQuery);
                typesStatement.setString(1, id);
                ResultSet typesResult = typesStatement.executeQuery();

                // Consulta para obtener los movimientos del Pokemon
                String movesQuery = "SELECT Move.id, Move.name, Move.damage FROM Move JOIN PokemonMove ON Move.id = PokemonMove.moveId WHERE PokemonMove.pokemonId = ?";
                PreparedStatement movesStatement = connection.prepareStatement(movesQuery);
                movesStatement.setString(1, id);
                ResultSet movesResult = movesStatement.executeQuery();

                // Crear y poblar el objeto Pokemon
                ArrayList<Type> types = new ArrayList<>();
                while (typesResult.next()) {
                    Type type = new Type(typesResult.getString("id"), typesResult.getString("name"), null);
                    types.add(type);
                }

                ArrayList<Move> moves = new ArrayList<>();
                while (movesResult.next()) {
                    Move move = new Move(movesResult.getString("id"), movesResult.getString("name"), movesResult.getInt("damage"));
                    moves.add(move);
                }

                Pokemon pokemon = new Pokemon(pokemonResult.getString("id"), pokemonResult.getString("name"), pokemonResult.getString("image"), types, moves);
                pokemons.add(pokemon);

                // Cerrar ResultSets y Statements para tipos y movimientos
                typesResult.close();
                movesResult.close();
                typesStatement.close();
                movesStatement.close();
            }

            // Cerrar ResultSet y Statement para Pokemon
            pokemonResult.close();

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pokemons;
    }

}

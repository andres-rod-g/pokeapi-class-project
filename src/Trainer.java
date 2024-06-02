import java.sql.*;
import java.util.ArrayList;

public class Trainer {
    private String _id;
    private String _name;
    private String _age;
    private String _region;
    private ArrayList<Pokemon> _pokemon;

    public Trainer(String id, String name, String age, String region, ArrayList<Pokemon> pokemon){
        _id = id;
        _name = name;
        _age = age;
        _region = region;
        _pokemon = pokemon;
    }

    public static Trainer hydrate(ResultSet trainerResult, ResultSet pokemonResult){
        try {
            ArrayList<Pokemon> pokemons = new ArrayList<Pokemon>();

            try {
                while(pokemonResult.next()){
                    Pokemon pokemon = Pokemon.getById(pokemonResult.getString("id"));
                    pokemons.add(pokemon);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Trainer trainer = new Trainer(trainerResult.getString("id"), trainerResult.getString("name"), trainerResult.getString("age"), trainerResult.getString("region"), pokemons);
            return trainer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void show(){
        System.out.println("ID: " + _id);
        System.out.println("Nombre: " + _name);
        System.out.println("Edad: " + _age);
        System.out.println("Región: " + _region);
        System.out.println("Pokémon: ");
        for (Pokemon pokemon : _pokemon){
            pokemon.showSmall();
        }
    }

    public void showSmall(){
        System.out.println(_id + ". " + _name);
    }

    public static Trainer getById(String id) {
        Trainer trainer = null;
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener la información del Trainer
            String trainerQuery = "SELECT * FROM Trainer WHERE id = ?";
            PreparedStatement trainerStatement = connection.prepareStatement(trainerQuery);
            trainerStatement.setString(1, id);
            ResultSet trainerResult = trainerStatement.executeQuery();

            if (trainerResult.next()) {
                // Consulta para obtener los Pokémon del Trainer
                String pokemonQuery = "SELECT Pokemon.id FROM Pokemon JOIN PokemonOwnership ON Pokemon.id = PokemonOwnership.pokemonId WHERE PokemonOwnership.trainerId = ?";
                PreparedStatement pokemonStatement = connection.prepareStatement(pokemonQuery);
                pokemonStatement.setString(1, id);
                ResultSet pokemonResult = pokemonStatement.executeQuery();

                // Crear y poblar el objeto Trainer
                trainer = Trainer.hydrate(trainerResult, pokemonResult);

                // Cerrar ResultSets y Statements
                pokemonResult.close();
                pokemonStatement.close();
            }

            // Cerrar ResultSet y Statement para Trainer
            trainerResult.close();
            trainerStatement.close();

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainer;
    }

    public static ArrayList<Trainer> getAll() {
        ArrayList<Trainer> trainers = new ArrayList<>();
        try {
            // Conectar a la base de datos
            Connection connection = Database.connect();

            // Consulta para obtener todos los Trainers
            String trainerQuery = "SELECT * FROM Trainer";
            Statement trainerStatement = connection.createStatement();
            ResultSet trainerResult = trainerStatement.executeQuery(trainerQuery);

            while (trainerResult.next()) {
                String id = trainerResult.getString("id");

                // Consulta para obtener los Pokémon del Trainer
                String pokemonQuery = "SELECT Pokemon.id FROM Pokemon JOIN PokemonOwnership ON Pokemon.id = PokemonOwnership.pokemonId WHERE PokemonOwnership.trainerId = ?";
                PreparedStatement pokemonStatement = connection.prepareStatement(pokemonQuery);
                pokemonStatement.setString(1, id);
                ResultSet pokemonResult = pokemonStatement.executeQuery();

                // Crear y poblar el objeto Trainer
                Trainer trainer = Trainer.hydrate(trainerResult, pokemonResult);
                trainers.add(trainer);

                // Cerrar ResultSets y Statements para Pokémon
                pokemonResult.close();
                pokemonStatement.close();
            }

            // Cerrar ResultSet y Statement para Trainer
            trainerResult.close();
            trainerStatement.close();

            // Cerrar la conexión
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return trainers;
    }
}

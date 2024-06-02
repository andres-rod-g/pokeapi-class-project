import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PokeApi", "root", "");

            process(connection);

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }

    }

    public static void process(Connection connection){
        System.out.println("¡Bienvenid@ a la PokeApi!");
        System.out.println("Podrás consultar información sobre los entrenadores, pokemon, sus movimientos y tipos.");
        System.out.println("¿Qué deseas hacer? \n");
        while (true){
            System.out.println(" == PokeApi ==");
            showMenu();

            mainMenu();
        }
    }

    public static void showMenu(){
        System.out.println("1. Consultar entrenadores");
        System.out.println("2. Consultar Pokemon");
        System.out.println("3. Consultar movimientos");
        System.out.println("4. Consultar tipos");
        System.out.println("5. Salir");
    }

    public static void mainMenu(){
        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option){
            case 1:
                trainerMenu();
                break;
            case 2:
                pokemonMenu();
                break;
            case 3:
                moveMenu();
                break;
            case 4:
                typeMenu();
                break;
            case 5:
                System.out.println("Salir");
                System.exit(0);
                break;
            default:
                System.out.println("Opción no válida");
                break;
        }

        System.out.println("Presiona enter para continuar...");
        scanner.nextLine();
        scanner.nextLine();

        System.out.println("\n\n\n");
    }

    public static void trainerMenu(){
        System.out.println("1. Consultar entrenador por ID");
        System.out.println("2. Consultar listado de entrenadores");
        System.out.println("3. Regresar");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option){
            case 1:
                System.out.println("Introce el ID del entrenador");

                Scanner scanner2 = new Scanner(System.in);
                String id = scanner2.nextLine();

                Trainer trainer = Trainer.getById(id);

                if (trainer == null){
                    System.out.println("Entrenador no encontrado");
                    return;
                }

                trainer.show();
                break;
            case 2:
                ArrayList<Trainer> trainers = Trainer.getAll();
                for (Trainer t : trainers){
                    t.showSmall();
                }
                break;
            case 3:
                System.out.println("Regresar");
                return;
            default:
                break;
        }
    }

    public  static void pokemonMenu() {
        System.out.println("1. Consultar Pokemon por ID");
        System.out.println("2. Consultar listado de Pokemon");
        System.out.println("3. Regresar");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option){
            case 1:
                System.out.println("Introce el ID del Pokemon");

                Scanner scanner2 = new Scanner(System.in);
                String id = scanner2.nextLine();

                Pokemon pokemon = Pokemon.getById(id);

                if (pokemon == null){
                    System.out.println("Pokemon no encontrado");
                    return;
                }

                pokemon.show();
                break;
            case 2:
                ArrayList<Pokemon> pokemons = Pokemon.getAll();
                for (Pokemon p : pokemons){
                    p.showSmall();
                }
                break;
            case 3:
                System.out.println("Regresar");
                return;
            default:
                break;
        }
    }

    public static void moveMenu(){
        System.out.println("1. Consultar movimiento por ID");
        System.out.println("2. Consultar listado de movimientos");
        System.out.println("3. Regresar");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option){
            case 1:
                System.out.println("Introce el ID del movimiento");

                Scanner scanner2 = new Scanner(System.in);
                String id = scanner2.nextLine();

                Move move = Move.getById(id);

                if (move == null){
                    System.out.println("Movimiento no encontrado");
                    return;
                }

                move.show();
                break;
            case 2:
                ArrayList<Move> moves = Move.getAll();
                for (Move m : moves){
                    m.showSmall();
                }
                break;
            case 3:
                System.out.println("Regresar");
                return;
            default:
                break;
        }
    }

    public static void typeMenu(){
        System.out.println("1. Consultar tipo por ID");
        System.out.println("2. Consultar listado de tipos");
        System.out.println("3. Regresar");

        Scanner scanner = new Scanner(System.in);
        int option = scanner.nextInt();

        switch (option){
            case 1:
                System.out.println("Introce el ID del tipo");

                Scanner scanner2 = new Scanner(System.in);
                String id = scanner2.nextLine();

                Type type = Type.getById(id);

                if (type == null){
                    System.out.println("Tipo no encontrado");
                    return;
                }

                type.show();
                break;
            case 2:
                ArrayList<Type> types = Type.getAll();
                for (Type t : types){
                    t.showSmall();
                }
                break;
            case 3:
                System.out.println("Regresar");
                return;
            default:
                break;
        }
    }

}
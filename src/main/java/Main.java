import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static ArrayList<Transaction> transactions;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String fileName = "src/main/resources/transactions.csv";
        loadTransactions(fileName);

//        mainMenu();
    }

    /**
     * Reads a CSV file into an ArrayList of Transaction objects.
     */
    private static void loadTransactions(String fileName) {
        transactions = new ArrayList<Transaction>();
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader reader = new BufferedReader(fileReader);

            String header = reader.readLine(); // eat header
            if (!header.equals("date|time|description|vendor|amount")) {
                System.out.println("wait!!! what?  That doesn't look like a header.");
            }

            String line;
            while((line = reader.readLine()) != null) {
                // something like: "2023-04-15|10:13:25|ergonomic keyboard|Amazon|-89.50"
                String[] parts = line.split("\\|"); //break up line

                // Convert all the parts of the line to appropriate type
                LocalDate date = LocalDate.parse(parts[0]);
                LocalTime time = LocalTime.parse(parts[1]);
                String description = parts[2];
                String vendor = parts[3];
                double amount = Double.parseDouble(parts[4]);

                // Create a new transaction object
                Transaction t = new Transaction(date, time, description, vendor, amount);

                // Add it to our collection
                transactions.add(t);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File name no good, dude: " + e);
        } catch (IOException e) {
            System.out.println("Some kind of weird exception reading file: " + e);
        }
    }

    private static void mainMenu() {
        String userInput = Main.scanner.nextLine();
        do {
            System.out.println("1, 2, 3");
            // get input
            switch (userInput) {
                case "L":
                    ledgerMenu();
                    break;
                case "A":
                    printAllTransactions();
                    break;
                default:
                    System.out.println("bad input");
            }
        } while( !userInput.equals("X"));
    }

    private static void ledgerMenu() {

    }

    private static void printAllTransactions() {

    }


}



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
        mainMenu();

        printAllTransactions();
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
        String userInput;
        do {
            String prompt = """
                    D) Add Deposit
                    P) Make Payment (Debit)
                    L) Ledger - display the ledger screen
                    X) Exit - exit the application""";
            System.out.println(prompt);
            userInput = scanner.nextLine();

            // get input
            switch (userInput) {
                case "D":
                    addDeposit();
                    break;
                case "P":
                    addPayment();
                    break;
                case "L":
                    ledgerMenu();
                    break;
                case "X":
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("bad input");
            }
        } while( !userInput.equals("X"));
    }

    private static void addDeposit() {
        System.out.println("Enter deposit date:");
        String userInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(userInput);

        System.out.println("Enter deposit time:");
        userInput = scanner.nextLine();
        LocalTime time = LocalTime.parse(userInput);

        System.out.println("Enter deposit description");
        String description = scanner.nextLine();

        System.out.println("Enter deposit vendor");
        String vendor = scanner.nextLine();

        System.out.println("Enter deposit amount");
        double amount = Double.parseDouble(scanner.nextLine());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime transactionDateTime = date.atTime(time);
        if(transactionDateTime.isAfter(now)) {
            System.out.println("Yo!  You can't enter stuff from the future!");
        }
        // TODO: How do i prevent this from being added?

        Transaction t = new Transaction(date, time, description, vendor, amount);
        transactions.add(t);
    }

    private static void addPayment() {
        System.out.println("Enter payment date:");
        String userInput = scanner.nextLine();
        LocalDate date = LocalDate.parse(userInput);

        System.out.println("Enter payment time:");
        userInput = scanner.nextLine();
        LocalTime time = LocalTime.parse(userInput);

        System.out.println("Enter payment description");
        String description = scanner.nextLine();

        System.out.println("Enter payment vendor");
        String vendor = scanner.nextLine();

        System.out.println("Enter payment amount");
        double amount = Double.parseDouble(scanner.nextLine());
        amount = amount * -1;

        Transaction t = new Transaction(date, time, description, vendor, amount);
        transactions.add(t);
    }

    private static void ledgerMenu() {
        String userInput;
        do {
            String prompt = """
                    A) Show all transactions
                    D) Show all deposits
                    P) Show all payments
                    R) Reports - display the reports screen
                    H) Home - exit to main menu""";
            System.out.println(prompt);
            userInput = scanner.nextLine();

            // get input
            switch (userInput) {
                case "A":
                    printAllTransactions();
                    break;
                case "D":
                    printAllDeposits();
                    break;
                case "P":
                    printAllPayments();
                    break;
                case "R":
                    reportsMenu();
                    break;
                case "H":
                    System.out.println("Returning to Main Menu...");
                    break;
                default:
                    System.out.println("bad input");
            }
        } while( !userInput.equals("H"));
    }

    private static void reportsMenu() {
        String userInput;
        do {
            String prompt = """
                    1) Print month to date transactions
                    2) Print out transactions for previous month
                    3) Print out YTD transactions
                    """;
            System.out.println(prompt);
            userInput = scanner.nextLine();

            // get input
            switch (userInput) {
                case "1":
                    printMonthToDate();
                    break;
                case "2":
                    printPreviousMonth();
                    break;
                case "3":
                    printYtd();
                    break;

                case "0":
                    System.out.println("Returning to Ledger Menu...");
                    break;
                default:
                    System.out.println("bad input");
            }
        } while( !userInput.equals("H"));
    }

    private static void printYtd() {
        LocalDate today = LocalDate.now();

        for(Transaction t: transactions) {
            if(today.getYear() == t.getDateTime().getYear()) {
                System.out.println(t.toString());
            }
        }
    }

    private static void printPreviousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate lastMonth = today.minusMonths(1);
        for(Transaction t: transactions) {
            if(lastMonth.getMonth() == t.getDateTime().getMonth() && lastMonth.getYear() == t.getDateTime().getYear()) {
                System.out.println(t.toString());
            }
        }
    }

    private static void printMonthToDate() {
        LocalDate today = LocalDate.now();
        for(Transaction t: transactions) {
            LocalDateTime transactionDateTime = t.getDateTime();
            int transactionYear = transactionDateTime.getYear();
            if((today.getYear() == transactionYear) && (today.getMonth() == t.getDateTime().getMonth())) {
                System.out.println(t.toString());
            }
        }
    }

    private static void printAllTransactions() {
        for (Transaction t: transactions) {
            System.out.println(t.toString());
        }
    }

    private static void printAllDeposits() {
        for (Transaction t: transactions) {
            if(t.getAmount() > 0) {
                System.out.println(t.toString());
            }
        }
    }

    private static void printAllPayments() {
        for (Transaction t: transactions) {
            if(t.getAmount() < 0) {
                System.out.println(t.toString());
            }
        }
    }

}



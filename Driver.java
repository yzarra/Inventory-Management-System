import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
 * Appliance Inventory Management System
 *
 * A console-based Java application that allows users to:
 * - Add and manage appliance inventory
 * - Search by brand or price
 * - Edit existing appliance records (password-protected)
 *
 * Features input validation, unique serial tracking, and basic access control.
 */

public class Driver {
    // Global Variables
    private static int counter = 0; // counts # of passw. attempts
    static boolean mainLoop = true;
    static boolean globalCount = true; // if you're doing 4 passw.times available or not

    // Main Method
    public static void main(String[] args) {
        // Variables & Scanner
        Scanner keyboard = new Scanner(System.in);
        mainLoop = true;

        // Welcome Message
        System.out.print("Welcome to the Appliance Store!\n"

                // Maximum Appliances
                + "Enter the maximum number of appliances your store can hold: ");
        int maxApps = keyboard.nextInt();
        keyboard.nextLine(); // consume leftover newline
        Appliance[] inventory = new Appliance[maxApps];
        loadInventory(inventory);

        // Main Menu
        while (mainLoop) {
            System.out.print("What would you like to do?\n" +
                    "\t1. Enter new appliances (password required)\n" +
                    "\t2. Change information of an appliance (password required)\n" +
                    "\t3. Display all appliances by a specific brand\n" +
                    "\t4. Display all appliances under a certain a price\n" +
                    "\t5. Exit the program\nPlease enter your selection: ");

            // User selection
            int selection = keyboard.nextInt();
            keyboard.nextLine(); // consume leftover newline
            // making sure selection is valid
            while (selection < 1 || selection > 5) {
                System.out.print("Invalid selection. Please enter a number between 1 and 5: ");
                selection = keyboard.nextInt();
                keyboard.nextLine(); // consume leftover newline
            }

            // 1. New Appliance
            if (selection == 1) {
                // Password check
                boolean check;
                globalCount = true;
                check = Pass();
                if (check == true) { // correct password

                    // Ask how many appliances to add
                    System.out.print("How many appliances would you like to add?: ");
                    int numToAdd = keyboard.nextInt();
                    keyboard.nextLine(); // consume leftover newline

                    // Check if room in inventory
                    int remainingSpace = maxApps - Appliance.findNumberOfCreatedAppliances();
                    while (1 <= numToAdd && numToAdd > remainingSpace) {
                        System.out.println("Not enough space in inventory. You can only add " + remainingSpace + " more appliance(s).\nHow many appliances would you like to add?: ");
                        numToAdd = keyboard.nextInt();
                        keyboard.nextLine(); // consume leftover newline
                    }

                    // Loop for each appliance
                    for (int i = 0; i < numToAdd; i++) {
                        // Input
                        System.out.println("\nAppliance #" + (i + 1) + ":");
                        System.out.print("What is the appliance's type?: ");
                        String type = keyboard.nextLine();
                        // check if valid type
                        while (!type.equalsIgnoreCase("Fridge") && !type.equalsIgnoreCase("Air Conditioner") && !type.equalsIgnoreCase("Washer") && !type.equalsIgnoreCase("Dryer") &&
                                !type.equalsIgnoreCase("Freezer") && !type.equalsIgnoreCase("Stove") && !type.equalsIgnoreCase("Dishwasher") && !type.equalsIgnoreCase("Water Heaters") &&
                                !type.equalsIgnoreCase("Microwave")) {
                            System.out.print("Invalid appliance type. Please enter a valid type (Fridge, Air Conditioner, Washer, Dryer, Freezer, Stove, Dishwasher, Water Heaters, or Microwave): ");
                            type = keyboard.nextLine();
                        }
                        System.out.print("What is the appliance's brand?: ");
                        String brand = keyboard.nextLine();
                        System.out.print("What is the appliance's price?: ");
                        double price = keyboard.nextDouble();
                        keyboard.nextLine(); // consume leftover newline
                        // check if valid price
                        while (price < 1) {
                            System.out.print("Invalid price. Please enter a price greater or equal to $1: ");
                            price = keyboard.nextDouble();
                            keyboard.nextLine(); // consume leftover newline
                        }

                        // Create new Appliance
                        Appliance newApp = new Appliance(type, brand, price);
                        inventory[Appliance.findNumberOfCreatedAppliances() - 1] = newApp; // store into inventory
                    }
                }
            }

            // 2. Edit Appliance
            else if (selection == 2) {
                // Variables
                boolean check;
                boolean search = true;
                boolean edit = true;
                globalCount = false;
                int serialEntry;
                int index = -1;

                // Password check
                check = Pass();
                if (check == true) { // correct password
                    System.out.print("Which object would you like to edit? (Enter serial number): ");
                    serialEntry = keyboard.nextInt();
                    keyboard.nextLine(); // consume leftover newline
                    // Search for appliance w that serial #
                    while (search == true) {
                        for (int i = 0; i < Appliance.findNumberOfCreatedAppliances(); i++) {
                            if (inventory[i] != null && inventory[i].getSerial() == serialEntry) {
                                index = i;
                                search = false;
                            }
                        }
                        // If not found
                        if (index == -1) {
                            System.out.print("Appliance with serial #" + serialEntry + " not found. Would you like to try again? (y/n): ");
                            char tryAgain = keyboard.next().charAt(0);
                            if (!(tryAgain == 'y') && !(tryAgain == 'Y')) { //if no
                                search = false;
                                // otherwise stays true and tries again :)
                            }
                        }
                    }
                    // When Serial # found
                    if (index != -1) {
                        // Print out Appliance
                        System.out.println(inventory[index]); //using overriden toString()
                        // Menu Loop
                        while (edit == true) {
                            System.out.print("What information would you like to change?\n" +
                                    "\t1.\tbrand\n" +
                                    "\t2.\ttype\n" +
                                    "\t3.\tprice\n" +
                                    "\t4.\tQuit\n" +
                                    "Please enter your choice> ");
                            int editChoice = keyboard.nextInt();
                            keyboard.nextLine(); // consume leftover newline

                            // 1. Change Brand
                            if (editChoice == 1) {
                                System.out.print("Enter the new brand: ");
                                String newBrand = keyboard.nextLine();
                                inventory[index].setBrand(newBrand); // update the appliance
                                System.out.println("Brand updated successfully!");
                                System.out.println(inventory[index]); // display updated appliance
                            }

                            // 2. Change Type
                            else if (editChoice == 2) {
                                System.out.print("Enter new type: ");
                                String newType = keyboard.nextLine();
                                // Check if valid type
                                while (!newType.equals("Fridge") && !newType.equals("Air Conditioner") && !newType.equals("Washer") && !newType.equals("Dryer") &&
                                        !newType.equals("Freezer") && !newType.equals("Stove") && !newType.equals("Dishwasher") && !newType.equals("Water Heaters") &&
                                        !newType.equals("Microwave")) {
                                    System.out.print("Invalid appliance type. Please enter a valid type (Fridge, Air Conditioner, Washer, Dryer, Freezer, Stove, Dishwasher, Water Heaters, or Microwave): ");
                                    newType = keyboard.nextLine();
                                }
                                // Update appliance
                                inventory[index].setType(newType);
                                System.out.println("Type updated successfully!");
                                System.out.println(inventory[index]); // display updated appliance
                            }

                            // 3. Change Price
                            else if (editChoice == 3) {
                                System.out.print("Enter new price: ");
                                double newPrice = keyboard.nextDouble();
                                keyboard.nextLine(); // consume leftover newline
                                // Check if valid price
                                while (newPrice < 1) {
                                    System.out.print("Invalid price. Please enter a price greater or equal to $1: ");
                                    newPrice = keyboard.nextDouble();
                                    keyboard.nextLine(); // consume leftover newlinef
                                }
                                // Update appliance
                                inventory[index].setPrice(newPrice);
                                System.out.println("Price updated successfully!");
                                System.out.println(inventory[index]); // display updated appliance
                            }

                            // 4. Quit
                            else if (editChoice == 4) {
                                edit = false;
                                System.out.println("Exiting edit menu.");
                            }

                            // 5+. Invalid choice
                            else {
                                System.out.println("Invalid choice. Please enter a number between 1 and 4.");
                            }

                            // Offer to Re-Loop Menu for Valid Entries
                            if (editChoice >= 1 && editChoice <= 3) {
                                System.out.print("Would you like to edit something else? (y/n): ");
                                char again = keyboard.next().charAt(0);
                                if (!(again == 'y') && !(again == 'Y')) { //if no
                                    edit = false;
                                    System.out.println("Returning to main menu.");
                                    // else loops automatically
                                }
                            }
                        }
                    }
                }
            }

            // 3. Display All Appliances by a Brand
            else if (selection == 3) {
                boolean found = false;
                boolean loop = true;
                while (loop == true) {
                    // Input Brand Loop
                    System.out.print("Enter the brand you want to display: ");
                    String brandEntry = keyboard.nextLine().trim();

                    // Search inventory for that brand
                    for (int i = 0; i < Appliance.findNumberOfCreatedAppliances(); i++) {
                        if (inventory[i] != null && inventory[i].getBrand().equalsIgnoreCase(brandEntry)) {
                            System.out.println(inventory[i]); // display matching appliance
                            found = true;
                        }
                    }

                    // If none found
                    if (!found) {
                        System.out.println("No appliances found for brand. Would you like to try again? (y/n): ");
                        char tryAgain = keyboard.next().charAt(0);
                        if (!(tryAgain == 'y') && !(tryAgain == 'Y')) { //if no
                            loop = false;
                        }
                    }

                    // If found / ask if re-loop?
                    else {
                        System.out.print("Would you like to search for another brand? (y/n): ");
                        char again = keyboard.next().charAt(0);
                        if (!(again == 'y') && !(again == 'Y')) { //if no
                            loop = false;
                        }
                        found = false; // reset found for next loop
                    }
                }
            }

            // 4. Display All Appliances Under a Price
            else if (selection == 4) {
                boolean found = false;
                boolean loop = true;
                while (loop == true) {
                    // Input Price
                    System.out.print("Enter the price: ");
                    double priceEntry = keyboard.nextDouble();
                    keyboard.nextLine(); // consume leftover newline

                    // Check if valid price
                    while (priceEntry < 1) {
                        System.out.print("Invalid price. Please enter a price greater or equal to $1: ");
                        priceEntry = keyboard.nextDouble();
                        keyboard.nextLine(); // consume leftover newline
                    }

                    // Search inventory for all apps under that price
                    for (int i = 0; i < Appliance.findNumberOfCreatedAppliances(); i++) {
                        if (inventory[i] != null && inventory[i].getPrice() <= priceEntry) {
                            System.out.println(inventory[i]); // display matching or lower appliance
                            found = true;
                        }
                    }

                    // If none found
                    if (!found) {
                        System.out.println("No appliances found under that price. Would you like to try again? (y/n): ");
                        char tryAgain = keyboard.next().charAt(0);
                        if (!(tryAgain == 'y') && !(tryAgain == 'Y')) { //if no
                            loop = false;
                        }
                    }

                    // If found / ask if re-loop?
                    else {
                        System.out.print("Would you like to search under another price? (y/n): ");
                        char again = keyboard.next().charAt(0);
                        if (!(again == 'y') && !(again == 'Y')) { //if no
                            loop = false;
                        }
                        found = false; // reset found for next loop
                    }
                }
            }

            // 5. Exit program
            else if (selection == 5) {
                saveInventory(inventory);
                System.out.println("Thank you for using the Appliance Store program. Goodbye!");
                System.exit(0);
            }

            // 6+. Incorrect Input
            else {
                System.out.println("Invalid selection. Please enter a number between 1 and 5.");
            }
        }
    }

    // Password Method
    public static boolean Pass() {
        // Variables
        Scanner keyboard = new Scanner(System.in);
        final String pass = "CompanyPassword101";
        String entry;
        boolean correct = false;
        int instCount = 0; //count only during this session

        // Password input
        System.out.print("Please enter the password: ");
        entry = keyboard.nextLine();
        // Incorrect loop
        if (pass.equals(entry)) {
            correct = true;
        }
        while (!correct) {
            // Limit attempts
            instCount++;
            counter++;

            // too many attempts for this session
            if (instCount == 3) {
                System.out.println("Too many incorrect attempts. Returning to main menu.");
                correct = false;
                break;
            }
            // Far too many attempts EVER
            else if (globalCount == true) {
                if (counter == 12) {
                    System.out.println("You have exceeded the maximum number of attempts. The program will now exit.");
                    System.exit(0);
                }
            }

            // Retry
            if (counter < 12 && instCount < 3) {
                System.out.print("Incorrect password. Please try again: ");
                entry = keyboard.nextLine();
                if (pass.equals(entry)) {
                    correct = true;
                    counter = 0;
                }
            }
        }
        return correct;
    }


    // Method to Save
    public static void saveInventory(Appliance[] inventory) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("inventory.csv"))) {
            for (int i = 0; i < Appliance.findNumberOfCreatedAppliances(); i++) {
                if (inventory[i] != null) {
                    writer.println(inventory[i].toCSV());
                }
            }
            System.out.println("Inventory saved.");
        } catch (IOException e) {
            System.out.println("Error saving inventory: " + e.getMessage());
        }
    }

    // Method to Load
    public static int loadInventory(Appliance[] inventory) {
        File file = new File("inventory.csv");
        if (!file.exists()) return 0; // no save file yet, that's fine

        int loaded = 0;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine() && loaded < inventory.length) {                String line = fileScanner.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split(",");
                long serial = Long.parseLong(parts[0]);
                String type = parts[1];
                String brand = parts[2];
                double price = Double.parseDouble(parts[3]);
                inventory[loaded] = new Appliance(type, brand, price, serial); // uses load constructor
                loaded++;
            }
            System.out.println("Loaded " + loaded + " appliance(s) from save.");
        } catch (IOException e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }
        return loaded;
    }
}

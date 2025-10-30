import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * CoffeeApp class.
 * 
 * This class is the core of the CoffeePayer application. Taking user input, running functions, and reading to/writing to file.
 */
public class CoffeeApp{

    Scanner uScanner;
    Map<String, OrderAccount> coworkers;
    String saveFile = "src\\CoffeeList.txt";

    String lastBuyer = null;
    double lastOrderTotal = 0;

    public CoffeeApp(){
        coworkers = new HashMap<String, OrderAccount>();
        uScanner = new Scanner(System.in);
    }

    public void runApp() throws IOException{

        // load the list of coworkers if possible
        readMembersFromFile();
        
        System.out.println("\n\n#########################################################");
        System.out.println("##              Welcome To Coffee Payer!               ##");
        printOptions();

        int userInput= 8;
        try{
            userInput = uScanner.nextInt();
        }
        catch (Exception e) {
            System.out.println("Invalid input. Exception encountered: " + e.getMessage());
        }
        uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.
        while (userInput != 0)
        {
            switch(userInput){
                case 1:
                    // Add new member
                    addMember();
                    break;
                case 2:
                    // Get today's payer
                    getPayer();
                    break;
                case 3:
                    // Update a members's order
                    updateMemberOrder();
                    break;
                case 4:
                    // Update a members's balance
                    updateMemberBalance();
                    break;
                case 5:
                    // Remove a member
                    removeMember();
                    break;
                case 6:
                    // print list of members
                    printMemberNames();
                    break;
                case 7:
                    // print members's order and amount owed
                    printMemberAccount();
                    break;
                case 8:
                    // print options
                    System.out.println("\n");
                    printOptions();
                    break;
                default:
                    System.out.println("\nInput '" + userInput + "' is not in the supported list.");
            }

            System.out.println("\nPlease select next action...");
            try{
                userInput = uScanner.nextInt();
            }
            catch (Exception e) {
                System.out.println("Invalid input. Exception encountered: " + e.getMessage());
                userInput = 8;
            }
            uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.
        }

        // Close Scanner
        uScanner.close();

        System.out.println("\nProgram terminating.\nThanks a Latte!\n\n");
    }

    /**
     * Helper class to print the available actions a user can take.
     */
    private void printOptions() {
        // Print out the available options for our users:

        System.out.println("#########################################################");
        System.out.println("##                  Available Actions                  ##");
        System.out.println("#########################################################");
        System.out.println("## 1: Add a new member to the list                     ##");
        System.out.println("## 2: Select today's coffee buyer!                     ##");
        System.out.println("## 3: Update a member's order                          ##");
        System.out.println("## 4: Update a member's account balance                ##");
        System.out.println("## 5: Remove a member from the list     :(             ##");
        System.out.println("## 6: Print list of members getting coffee             ##");
        System.out.println("## 7: Print a selected member's order and balance      ##");
        System.out.println("## 8: Re-print these options                           ##");
        System.out.println("## 0: Exit application                                 ##");
        System.out.println("#########################################################\n\n");
    }

    /**
     * Helper file to read in state from the expected save file, if it exists.
     * 
     * This function ignores any entries with the improper number of elements, but does not do any further validation of file details.
     * Such validation could be made in the future to protect against file corruption.
     * 
     * @throws IOException
     */
    private void readMembersFromFile() throws IOException {
        // check if save file exists
        Path inputPath = Paths.get(saveFile);
        if (Files.exists(inputPath)) {
            String inputString = Files.readString(inputPath, StandardCharsets.UTF_8);

            String[] members = inputString.split("\n");
            
            // Only read entries with the proper amount of elements
            for (int i = 0; i < members.length; i++){
                String[] formattedEntry = members[i].split(",");

                if (formattedEntry.length == 3) {
                    
                    String memberName = formattedEntry[0];
                    double orderPrice = Double.parseDouble(formattedEntry[1]);
                    double currentBalance = Double.parseDouble(formattedEntry[2]);

                    // only add entry if map doesn't yet contain it:
                    if (!coworkers.containsKey(memberName)){
                        coworkers.put(memberName, new OrderAccount(orderPrice, currentBalance));
                    }
                }

            }
        }
    }

    /**
     * Helper file to write the current state of the coffee list accounts to file so that it can be preserved between sessions.
     * 
     * @throws IOException
     */
    private void writeMembersToFile() throws IOException{
        // prepare contents to write to file:
        String fileContents = "";
        for (Map.Entry<String, OrderAccount> element : coworkers.entrySet()){
            fileContents = fileContents + element.getKey() + "," + Double.toString(element.getValue().getOrderCost()) + "," + Double.toString(element.getValue().getAccountBalance()) + "\n";
        }

        // remove any trailing new lines or whitespace
        fileContents.trim();

        Path targetPath = Paths.get(saveFile);

        Files.createDirectories(targetPath.getParent()); // Ensure directory is created if necessary.

        Files.write(targetPath, fileContents.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Helper function to take user input to add a new member to the coffee list. Initially new additions are expected to not owe
     * anything on their account.
     * 
     * @throws IOException
     */
    private void addMember() throws IOException{
        // Request input for name
        System.out.println("Please enter new member's name...");

        String memberName = uScanner.nextLine();

        if (coworkers.containsKey(memberName)){
            System.out.println("Invalid input. Cannot have duplicate members, please select another name.");
            return;
        }

        double orderPrice = 0;
        System.out.println("Please enter the cost of " + memberName + "'s coffee order (please input only numbers and decimal places, no unit symbols).");
        try{
            orderPrice = uScanner.nextDouble();
        }
        catch (Exception e){
            System.out.println("Invalid input format. Please try again. Exception: " + e.getMessage());
            uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.
            return;
        }
        uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.

        coworkers.put(memberName, new OrderAccount(orderPrice, 0));

        System.out.println("New member '" + memberName + "' with order of price '" + Double.toString(orderPrice) + "' has been added to the list!");

        writeMembersToFile();
    }

    /**
     * Helper method to allow a change to a users order cost, in the event that the user wishes to change their order or set it incorrectly originally
     * 
     * @throws IOException
     */
    private void updateMemberOrder() throws IOException{
        // Request input for name
        System.out.println("Please enter the name of the member to update...");

        String memberName = uScanner.nextLine();

        if (!(coworkers.containsKey(memberName))){
            System.out.println("Invalid input. Name not found, please try again with a valid name.");
            return;
        }

        OrderAccount order = coworkers.get(memberName);

        System.out.println("Please enter the updated cost of " + memberName + "'s coffee order" 
            + "(please input only numbers and decimal places, no unit symbols). Current cost: " + Double.toString(order.getOrderCost()));
        double orderPrice = 0;
        try{
            orderPrice = uScanner.nextDouble();
        }
        catch (Exception e){
            System.out.println("Invalid input format. Please try again. Exception: " + e.getMessage());
            uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.
            return;
        }
        uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.

        order.setOrderCost(orderPrice);

        System.out.println(memberName + "'s order has been updated with price '" + Double.toString(orderPrice) + "'!");

        writeMembersToFile();
    }

    /**
     * Helper method to allow updates to a user's balance. Meant for re-adding old users that left with a lingering balance, or
     *  perhaps adding a user that joined in coffee before but hadn't yet been added to the list.
     * 
     * @throws IOException
     */
    private void updateMemberBalance() throws IOException{
        // Request input for name
        System.out.println("Please enter the name of the member to update...");

        String memberName = uScanner.nextLine();

        if (!(coworkers.containsKey(memberName))){
            System.out.println("Invalid input. Name not found, please try again with a valid name.");
            return;
        }

        OrderAccount order = coworkers.get(memberName);

        System.out.println("Please enter the updated balance of " + memberName + "'s account" 
            + "(please input only numbers and decimal points, no unit symbols). Current account balance: " + Double.toString(order.getAccountBalance()));
        double accountBalance = 0;
        try{
            accountBalance = uScanner.nextDouble();
        }
        catch (Exception e){
            System.out.println("Invalid input format. Please try again. Exception: " + e.getMessage());
            uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.
            return;
        }
        uScanner.nextLine(); // necessary to skip the newline at the end of entering the int, so following entries aren't broken.

        order.setAccountBalance(accountBalance);

        System.out.println(memberName + "'s account has been updated with balance '" + Double.toString(accountBalance) + "'!");

        writeMembersToFile();
    }

    /**
     * Helper method to remove a member in the event that they are no longer participating.
     * 
     * Could be used in conjunction with adding a user and updating the balance to temporarily remove a user from the list for a selection.
     * 
     * @throws IOException
     */
    private void removeMember() throws IOException{
        
        // Request input for name
        System.out.println("Please enter the name of the member to remove...");

        String memberName = uScanner.nextLine();

        if (!(coworkers.containsKey(memberName))){
            System.out.println("Invalid input. Name not found, please try again with a valid name.");
            return;
        }

        OrderAccount orderDetails = coworkers.remove(memberName);

        System.out.println(memberName + " has been removed from the list. At time of removal, a remaining balance of '" + Double.toString(orderDetails.getAccountBalance()) + "' was on their account.");

        writeMembersToFile();
    }

    /**
     * Helper method to print the names of members.
     */
    private void printMemberNames(){
        System.out.println("Current members of the Coffee List:");
        for (Map.Entry<String, OrderAccount> element : coworkers.entrySet()){
            System.out.println("\t" + element.getKey());
        }
    }

    /**
     * Helper method to print the details of a given user's current order cost and account balance.
     */
    private void printMemberAccount(){
        // Request input for name
        System.out.println("Please enter the name of the member to check...");

        String memberName = uScanner.nextLine();

        if (!(coworkers.containsKey(memberName))){
            System.out.println("Invalid input. Name not found, please try again with a valid name.");
            return;
        }

        OrderAccount order = coworkers.get(memberName);

        System.out.println(memberName + "'s current order status:");
        System.out.println("\tOrder Cost: " + Double.toString(order.getOrderCost()));
        System.out.println("\tCurrent Balance: " + Double.toString(order.getAccountBalance()));
    }

    /**
     * Helper method to select a user to pay for coffee this time and update the account balances accoringly.
     * 
     * @throws IOException
     */
    private void getPayer() throws IOException{
        if (coworkers.size() <= 0){
            System.out.println("There are no entries in the coffee list. No one can be selected to pay.");
            return;
        }

        double totalOrderCost = 0;
        String buyer = null;
        OrderAccount buyersOrder = null;

        for (Map.Entry<String, OrderAccount> element : coworkers.entrySet()){
            OrderAccount order = element.getValue();

            double orderCost = order.getOrderCost();
            double newBalance = order.getAccountBalance() + orderCost;
            order.setAccountBalance(newBalance);
            totalOrderCost = totalOrderCost + orderCost;

            if ((buyer == null) || (buyersOrder == null) || (newBalance > buyersOrder.getAccountBalance())){
                // buyer has not yet been selected or the current members's balance is higher
                buyer = element.getKey();
                buyersOrder = order;
            }
        }

        double buyersBalance = buyersOrder.getAccountBalance();
        buyersOrder.setAccountBalance(buyersBalance - totalOrderCost);

        lastBuyer = buyer;
        lastOrderTotal = totalOrderCost;

        writeMembersToFile();

        System.out.println("The buyer for today is: " + buyer + ", and the cost of the order will be: " + Double.toString(totalOrderCost));
    }

}
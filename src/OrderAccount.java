/**
 * OrderAccount class to keep track of the cost and current balance of the order of a given
 * member of the coffee list.
 * 
 * Tracking name was also considered, but deemed unnecessary for this implementation as these
 * will be stored in a hashmap with the member name as the key.
 **/
public class OrderAccount {

    private double orderCost;
    private double accountBalance;
    
    /**
     * OrderAccount constructor
     * @param orderPrice - the price of the coffee order for the associated member
     * @param balance - the current account balance for the associated member
     */
    public OrderAccount(double orderPrice, double balance){
        orderCost = orderPrice;
        accountBalance = balance;
    }

    /**
     * Getter for orderCost
     * @return The current value of the cost of the associated member's order.
     */
    public double getOrderCost() {
        return orderCost;
    }

    /**
     * Setter for orderCost
     * @param newPrice - The value to update orderCost to. 
     */
    public void setOrderCost(double newPrice){
        orderCost = newPrice;
    }

    /**
     * Getter for accountBalance
     * @return - The current value of the balance for the associated member's orders
     */
    public double getAccountBalance() {
        return accountBalance;
    }

    /**
     * Setter for accountBalance
     * @param newBalance - The value to set accountBalance to.
     */
    public void setAccountBalance(double newBalance){
        accountBalance = newBalance;
    }
}

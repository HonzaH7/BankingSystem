import java.util.Scanner;

public class Main {
    static Scanner userInput;
    static Bank bank;
    public static void main(String[] args) {
        System.out.println("Do you have an account? ");
        System.out.println("1. Yes\n2. No");
        int input = Integer.parseInt(userInput.nextLine());
        switch (input) {
            case 1:
                loginActionAndFollowUp();
                break;
            case 2:
                createAnAccount();
                break;
            default:
                System.out.println("Bye!");
        }

    }

    public static void options() {
        System.out.println("What action do you want to do?\n1. Deposit money\n2. Withdraw money\n3. Delete account\n4. Exit");
    }

    public static void loginActionAndFollowUp() {
        for(int attempts = 0; attempts < 3; ++attempts) {
            System.out.println("Please login.");
            System.out.println("Your username: ");
            String username = userInput.nextLine();
            System.out.println("Your password: ");
            String password = userInput.nextLine();
            if (bank.login(username, password) != null) {
                int action;
                do {
                    options();
                    action = Integer.parseInt(userInput.nextLine());
                    chosenAction(action);
                } while(action != 4);

                return;
            }

            System.out.println("Login failed. Please try again.");
        }

        System.out.println("Login failed after multiple attempts.");
    }

    public static void createAnAccount() {
        System.out.println("Please create an account.");
        System.out.println("Your firstname: ");
        String firstname = userInput.nextLine();
        System.out.println("Your lastname: ");
        String lastname = userInput.nextLine();
        System.out.println("Your username: ");
        String username = userInput.nextLine();
        System.out.println("Your password: ");
        String password = userInput.nextLine();
        bank.createAccount(firstname, lastname, username, password);
        loginActionAndFollowUp();
    }

    public static void chosenAction(int action) {
        switch (action) {
            case 1:
                System.out.println("Amount you would like to deposit:");
                double depositAmount = Double.parseDouble(userInput.nextLine());
                bank.deposit(depositAmount);
                break;
            case 2:
                System.out.println("Amount you would like to withdraw:");
                double withdrawAmount = Double.parseDouble(userInput.nextLine());
                bank.withdraw(withdrawAmount);
                break;
            case 3:
                System.out.println("Are you sure you want to delete your account? (yes/no)");
                String confirmation = userInput.nextLine();
                if ("yes".equalsIgnoreCase(confirmation)) {
                    bank.deleteAccount();
                } else {
                    System.out.println("Account deletion cancelled.");
                }
                break;
            case 4:
                System.out.println("See you soon!");
                bank.logout();
                break;
            default:
                System.out.println("Invalid option selected.");
        }

    }

    static {
        userInput = new Scanner(System.in);
        bank = new Bank();
    }
}

package Main;
import java.io.File;
import java.util.*;
import Staff.StaffApp;
import utils.Initialiser;
import utils.ProjectRootPathFinder;
import Customer.CustomerApp;
/**
 * Driver App
 */
public class MainApp {
    
    /** 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        File file = new File(ProjectRootPathFinder.findProjectRootPath() + "/Database/Cineplex/cineplexes.txt");
        if (file.length() == 0)
            Initialiser.getInstance();
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        while(choice != 3){
            System.out.println("--- WELCOME TO MOBLIMA! ---\n"
                              +" 1. Customer\n"
                              +" 2. Staff\n"
                              +" 3. Exit\n"
                              +"---------------------------");
            System.out.println("Please enter your choice:");
            

            /*
             * Check if input is an integer
             */
            while (!sc.hasNextInt()) {
            	System.out.println("Invalid input type. Please enter an integer value.");
        		sc.next(); // remove newline
            }
            
            choice = sc.nextInt();
            switch(choice){
                case 1:
                    CustomerApp.getInstance().displayCustomerMenu();
                    break;
                case 2:
                    StaffApp.getInstance().displayStaffLogin();
                    break;
                case 3:
                    System.out.println("Thank you for using MOBLIMA!");
                    break;
                default:
                    System.out.println("Please enter an integer between 1-3.");
                    break;
            }
        }
        sc.close();
    }
}

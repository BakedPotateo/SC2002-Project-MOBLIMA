package Customer;

import java.util.Scanner;

import Managers.CustomerManager;
import Managers.MovieManager;
import Movies.Movie;

public class CustomerApp {
    private Scanner sc = new Scanner(System.in);

    // Attributes

    /*
     * instance checks whether CustomerApp has been instantiated before, Static variable is the same between objects of the same class.
     */
    public static CustomerApp instance = null;

    /*
     * Empty class constructor
     */
    private CustomerApp() {}

    //public methods

    /*
     * getInstance checks if CustomerApp has been instantiated before.
     * If no previous instance was created, it creates a new one,
     * else it will use the original instance.
     */
    public static CustomerApp getInstance()
    {
        if(instance == null)
            instance = new CustomerApp(); //instance is a static variable
        return instance;
    }
    
    // Customer main app to access the Moblima System
    public void displayCustomerMenu(){
        int choice;
        boolean exit = false;

        while(!exit){
            System.out.println("------------- MOBLIMA CUSTOMER APP ------------\n" +
                               " 1. View current movies\n" +
                               " 2. Search for a movie\n" +
                               " 3. View movie details\n" +
                               " 4. Book and purchase ticket\n" +
                               " 5. View booking history\n" +
                               " 6. List the Top 5 movies\n" +
                               " 7. Submit a movie review\n" +
                               " 8. Exit\n"+
                               "-----------------------------------------------\n");            

            System.out.println("Please enter your choice:");

            /*
             * Check if input is an integer
            */
            while(!sc.hasNextInt()){
                System.out.println("Invalid input type. Please enter an integer value. ");
                sc.next(); //remove new line
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    //MovieManager.getInstance().getAvailableMovies();
                    MovieManager.getInstance().viewMovies();
                    break;
                case 2:
                    CustomerManager.getInstance().SearchMovie();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    MovieManager.getInstance().showTop5();
                    break;
                case 7:
                    CustomerManager.getInstance().addReview();
                    break;
                case 8:
                    System.out.println("Returning to the main page...\n");
                    exit = true;
                    break;
                default:
                     System.out.println("Please enter an integer between 1-9.\n");
                    break;
            
            }
        }
    }

    
}


        
            
            

      


    

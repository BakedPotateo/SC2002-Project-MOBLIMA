package Managers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import Cinema.Cinema;
import Cinema.Cineplex;
import Movies.Movie;
import Movies.Showtime;
import utils.ProjectRootPathFinder;
/**
 * Manager class to create and manage showtimes
 */
public class ShowtimeManager {
    /**
     * instance checks whether ShowtimeManager has been instantiated before. Static variable is the same between objects of the same class.
     */
    private static ShowtimeManager instance = null;

    /**
     * Empty class constructor
     */
    public ShowtimeManager(){}

    /**
     * The file name of the database file that this manager will access
     */
    public final static String FILE = ProjectRootPathFinder.findProjectRootPath() + "/Database/Movies/movies.txt";
    private Scanner sc = new Scanner(System.in);

    /**
     * getInstance checks if ShowtimeManager has been instantiated before. 
     * If no previous instance was created, it creates a new one, 
     * else it will use the original instance.
     */
    public static ShowtimeManager getInstance()
    {
        if (instance == null)
            instance = new ShowtimeManager(); // instance is a static variable
        return instance;
    }

    /**
     * Method to display showtime editing menu
     * @param movie  movie to be edited
     * @return new ArrayList of showtimes for movie. Returns original if no changes were made
     */
    public ArrayList<Showtime> editShowtimeMenu(Movie movie){
        int choice = 0;
        ArrayList<Showtime> newShowtimes = movie.getShowtimes();
        while(choice != 4){
            System.out.println("-------- EDIT SHOWTIMES --------\n"
                              +" 1. Add showtime\n"
                              +" 2. Remove showtime\n"
                              +" 3. Preview showtimes\n"
                              +" 4. Exit\n"
                              +"--------------------------------");

            System.out.println("Please enter your choice:");

            /*
             * Check if input is an integer
             */
            while (!sc.hasNextInt()) {
            	System.out.println("Invalid input type. Please enter an integer value.");
        		sc.next(); // remove newline
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    newShowtimes = this.addShowtime(movie);
                    break;
                case 2:
                    newShowtimes = this.removeShowtime(movie);
                    break;
                case 3:
                    for(Showtime showtime : newShowtimes)
                        showtime.makeString();
                    break;
                case 4:
                    System.out.println("Exiting...\n");
                    break;
            }
        }
        return newShowtimes;
    }

    /**
     * Method to create a new showtime for a movie
     * @param movie  Movie to be edited
     * @return ArrayList of showtimes with existing showtimes and new showtime
     */
    public ArrayList<Showtime> addShowtime(Movie movie){
        ArrayList<Showtime> showtimes = new ArrayList<Showtime>();
        if (movie.getShowtimes() != null)
            showtimes = movie.getShowtimes();

        System.out.println("Enter the showtime ID:");
        String showtimeID;
        while(true){
            while (!sc.hasNext()) {
                System.out.println("Invalid input type. Please try again!");
                sc.next(); // Remove newline character
            }
            showtimeID = sc.nextLine();
            if(isValidShowtimeID(movie, showtimeID)) break;
        }
        System.out.println("Enter the date in the format yyyy-mm-dd:");
        while (!sc.hasNext()) {
            System.out.println("Invalid input type. Please try again!");
            sc.next(); // Remove newline character
        }
        String date = sc.nextLine();
        System.out.println("Enter the time in the format hh:mm:");
        while (!sc.hasNext()) {
            System.out.println("Invalid input type. Please try again!");
            sc.next(); // Remove newline character
        }
        String time = sc.nextLine();

        String dateTimeStr = date + " " + time;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"); 
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);

        ArrayList<Cineplex> cineplexes = CinemaManager.getInstance().read();
        for(int i = 0; i < cineplexes.size(); i++){
            System.out.println((i+1) + ". " + cineplexes.get(i).getName());
        }

        System.out.println("Please choose a cineplex:");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input type. Please try again!");
            sc.next(); // Remove newline character
        }
        int cineplexChoice = sc.nextInt() - 1;

        ArrayList<Cinema> cinemas = cineplexes.get(cineplexChoice).getCinemas();

        for(int i = 0; i < cinemas.size(); i++){
            System.out.println((i+1) + ". " + cinemas.get(i).getId());
        }

        System.out.println("Please choose a cinema:");
        while (!sc.hasNextInt()) {
            System.out.println("Invalid input type. Please try again!");
            sc.next(); // Remove newline character
        }
        int cinemaChoice = sc.nextInt() - 1;

        Showtime newShowtime = new Showtime(showtimeID, dateTime, movie.getId(), cinemas.get(cinemaChoice), cineplexes.get(cineplexChoice).getName());
        showtimes.add(newShowtime);
        return showtimes;
    }

    /**
     * Method to delete showtime for a movie
     * @param movie  Movie to be edited
     * @return ArrayList of showtimes with edited showtimes
     */
    public ArrayList<Showtime> removeShowtime(Movie movie){
        ArrayList<Showtime> showtimes = new ArrayList<Showtime>();
        if (movie.getShowtimes() != null)
            showtimes = movie.getShowtimes();
        System.out.println("Enter the showtime ID:");
        String showtimeID;
        while(true){
            while (!sc.hasNext()) {
                System.out.println("Invalid input type. Please try again!");
                sc.next(); // Remove newline character
            }
            showtimeID = sc.nextLine();
            if(showtimeIdExists(movie, showtimeID)) break;
        }

        for(int i = 0; i < showtimes.size(); i++)
            if(showtimes.get(i).getShowtimeID().equals(showtimeID))
                showtimes.remove(i);
        
        return showtimes;
    }

    /**
     * Checks if showtime exists for a specific movie when booking movie
     * @param movie        Movie to be checked
     * @param showtimeID  Showtime ID to be checked for
     * @return true if newShowtimeID exists for selected movie, else false
     */
    public boolean showtimeIdExists(Movie movie, String showtimeID) {
        ArrayList<Showtime> showtimes = movie.getShowtimes();
        if(showtimes == null)
            return false;
        for(Showtime showtime : showtimes)
            if(showtime.getShowtimeID().equals(showtimeID)){
                return true;
            }
        System.out.println("ID does not exist.");
        return true;
    }

    /**
     * Checks if showtime exists for a specific movie when creating showtime
     * @param movie          Movie to be checked
     * @param newShowtimeID  Showtime ID to be checked for
     * @return true if newShowtimeID exists for selected movie, else false
     */
    public boolean isValidShowtimeID(Movie movie, String newShowtimeID){
        ArrayList<Showtime> showtimes = movie.getShowtimes();
        if(showtimes == null)
            return true;
        for(Showtime showtime : showtimes)
            if(showtime.getShowtimeID().equals(newShowtimeID)){
                System.out.println("Showtime ID already exists.\n");
                return false;
            }
        return true;
    }
}

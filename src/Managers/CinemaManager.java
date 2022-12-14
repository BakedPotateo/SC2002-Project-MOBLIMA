package Managers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
import Cinema.*;
import utils.ProjectRootPathFinder;
/**
 * Manager class to manage creation of cinemas
 */
public class CinemaManager {

    /**
     * instance checks whether CInemaManager has been instantiated before. Static variable is the same between objects of the same class.
     */
    public static CinemaManager instance = null;
    private Scanner sc = new Scanner(System.in);

    /**
     * Empty class constructor
     */
    private CinemaManager(){}

    /**
     * The file name of the database file that this manager will access
     */
    public final static String FILE = ProjectRootPathFinder.findProjectRootPath() + "/Database/Cineplex/cineplexes.txt";

    /**
     * getInstance checks if CinemaManager has been instantiated before. 
     * If no previous instance was created, it creates a new one, 
     * else it will use the original instance.
     */
    public static CinemaManager getInstance()
    {
        if (instance == null)
            instance = new CinemaManager(); // instance is a static variable
        return instance;
    }

    /**
     * Function to read database for cineplexes
     * @return  ArrayList of cineplexes
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Cineplex> read() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(FILE));
            ArrayList<Cineplex> cineplexes = (ArrayList<Cineplex>) objectInputStream.readObject();
            objectInputStream.close();
            return cineplexes;
        } catch (ClassNotFoundException | IOException e) {}
        return new ArrayList<Cineplex>();
    }

    
    /** 
     * Methos to create cinieplexes
     * @param name    Cineplex name
     * @param cinemas Cinemas to be added to cineplex
     * Data written directly to database
     */
    public void createCineplex(String name, ArrayList<Cinema> cinemas) {
        Cineplex cineplex = new Cineplex(name, cinemas);
        ArrayList<Cineplex> data = new ArrayList<Cineplex>();
        File myFile = new File(FILE);
        if (myFile.exists()) 
            data = read();
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE));
            data.add(cineplex);
            out.writeObject(data);
            out.flush();
            out.close();
        } catch (IOException e) {}
    }

    
    /** 
     * Method to search cineplex by name
     * @param name Name of cineplex to search for
     * @return Cineplex
     */
    public Cineplex searchCineplexByName(String name) {
        ArrayList<Cineplex> data = read();
        for (int i=0; i<data.size(); i++) {
            Cineplex tempCineplex = data.get(i);
            if (tempCineplex.getName().equals(name))
                return tempCineplex;
        }
        return null;
    }

    
    /** 
     * Method to delete cineplex by name
     * @param name Cineplex to search for
     */
    public void deleteCineplexByName(String name) {
        ArrayList<Cineplex> data = read();

        for (int i=0; i<data.size(); i++) {
            Cineplex tempCineplex = data.get(i);
            if (tempCineplex.getName().equalsIgnoreCase(name)) {
                data.remove(tempCineplex);
                break;
            }
        }

        try {
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(FILE));
            output.writeObject(data);
            output.flush();
            output.close();
        } catch (IOException e) {}
    }

    
    /** 
     * Method to edit cineplex name
     * @param oldName old cineplex name
     * @param newName new cineplex name
     */
    public void editCineplexName(String oldName, String newName) {
        ArrayList<Cineplex> data = read();
        Cineplex cineplex = null;
        for (int i=0; i<data.size(); i++) {
            cineplex = data.get(i);
            if (cineplex.getName().equals(oldName))
                break;
        }
        if (cineplex == null) {
            System.out.println("Cineplex does not exist.");
            return;
        }
        cineplex.setName(newName);
        deleteCineplexByName(oldName);
        createCineplex(cineplex.getName(), cineplex.getCinemas());
    }

    /**
     * Menu for editing cineplexes
     */
    public void editCineplex() {
        int choice = 0;
        while (choice != 2) {
            System.out.println("----- EDIT CINEPLEX -----\n"
                              +" 1. Edit Cineplex Name\n"
                              +" 2. Exit\n"
                              +"----------------------");
            
            System.out.println("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.println("Invalid input type. Please enter an integer value.");
                sc.next(); // remove newline
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1: 
                    System.out.println("Please enter name of Cineplex: ");
                    while (!sc.hasNext()) {
                        System.out.println("Invalid input type. Please try again!");
                        sc.next(); // remove newline
                    }
                    String cineplexName = sc.nextLine();

                    System.out.println("Please enter new name of Cineplex: ");
                    while (!sc.hasNext()) {
                        System.out.println("Invalid input type. Please try again!");
                        sc.next(); // remove newline
                    }
                    String newCineplexName = sc.nextLine();

                    this.editCineplexName(cineplexName, newCineplexName);
                    break;
                case 2: 
                    System.out.println("Cineplex Details updated!");
                    break;
                default:
                    System.out.println("Please enter an integer between 1-2.");     
                    break;     
            }
        }
    }

    /**
     * Menu for viewing cineplexes
     */
    public void viewCineplexes() {
        int choice = 0;
        while(choice != 3){
            System.out.println("-------- VIEW CINEPLEXES --------\n"
                              +" 1. View all cineplexes\n"
                              +" 2. View cineplex by name\n"
                              +" 3. Exit\n"
                              +"-----------------------------");

            System.out.println("Please enter your choice:");

            while (!sc.hasNextInt()) {
            	System.out.println("Invalid input type. Please enter an integer value.");
        		sc.next(); // remove newline
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    ArrayList<Cineplex> cineplexes = read();
                    for (Cineplex cineplex : cineplexes) 
                        System.out.println(cineplex.makeString()); 
                    break;
                case 2:
                    System.out.println("Please enter Cineplex Name:");
                    String cineplexName = sc.nextLine();
                    Cineplex cineplex = searchCineplexByName(cineplexName);
                    System.out.println(cineplex.makeString());
                    break;
                case 3:
                    System.out.println("Exiting...");
                    break;
            }
        }
    }


    
    /** 
     * Method to create a cinema
     * @param cineplexName  Cineplex for cinema to be added to
     * @param id            Cinema ID
     * @param classOfCinema Cinema class (e.g. Gold Class)
     * @param layout        Cinema seating layout
     */
    public void createCinema(String cineplexName, String id, String classOfCinema, SeatingLayout layout) {
        Cinema cinema = new Cinema(id, classOfCinema, layout);
        ArrayList<Cineplex> data = read();
        for (int i=0; i<data.size(); i++) {
            Cineplex cineplex = data.get(i);
            data.remove(i);
            if (cineplex.getName().equalsIgnoreCase(cineplexName)) {
                ArrayList<Cinema> cinemas = cineplex.getCinemas();
                cinemas.add(cinema);
                cineplex.setCinemas(cinemas);
                deleteCineplexByName(cineplexName);
                createCineplex(cineplexName, cineplex.getCinemas());
                break;
            }
        }
    }

    
    /** 
     * Methos to get all cinemas
     * @return ArrayList<Cinema>
     */
    public ArrayList<Cinema> readCinemas() {
        ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
        ArrayList<Cineplex> cineplexes = read();
        
        for (int i=0; i<cineplexes.size(); i++) {
            Cineplex cineplex = cineplexes.get(i);
            for (Cinema cinema : cineplex.getCinemas())
                cinemas.add(cinema);
        }
        return cinemas;
    }

    
    /** 
     * Method to return all cinmas in a specific cineplex
     * @param name Cineplex to get cinemas from
     * @return ArrayList<Cinema>
     */
    public ArrayList<Cinema> getCinemasByCineplex(String name) {
        ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
        ArrayList<Cineplex> cineplexes = read();
        Cineplex cineplex = null;

        for (int i=0; i<cineplexes.size(); i++) {
            cineplex = cineplexes.get(i);
            if (cineplex.getName().equalsIgnoreCase(name)) {
                for (Cinema cinema : cineplex.getCinemas()) {
                    cinemas.add(cinema);
                }
            }
        }

        return cinemas;
    }

    
    /** 
     * Method to search for cinema by its ID
     * @param code Cinema ID to search for
     * @return Cinema
     */
    public Cinema searchCinema(String code) {
        ArrayList<Cineplex> data = read();
        for (int i=0; i<data.size(); i++) {
            Cineplex cineplex = data.get(i);
            ArrayList<Cinema> cinemas = cineplex.getCinemas();
            for (int j=0; j<cinemas.size(); j++) {
                Cinema cinema = cinemas.get(j);
                if (cinema.getId().equals(code)) {
                    return cinema;
                }
            }
        }
        return null;
    }
 
    
    /** 
     * Method to delete cinema
     * @param code Cinema to delete
     */
    public void deleteCinema(String code) {
        ArrayList<Cineplex> data = read();
        for (int i=0; i<data.size(); i++) {
            Cineplex cineplex = data.get(i);

            if (cineplex.getCinemas().size() > 3) {
                ArrayList<Cinema> cinemas = cineplex.getCinemas();
                
                for (int j=0; j<cinemas.size(); j++) {
                    Cinema cinema = cinemas.get(j);
                    if (cinema.getId().equals(code)) {
                        cinemas.remove(j);
                        cineplex.setCinemas(cinemas);
                        createCineplex(cineplex.getName(), cineplex.getCinemas());
                        deleteCineplexByName(cineplex.getName());
                        return;
                    }
                }
            }
        }
    }

    
    /** 
     * Method to edit cinema ID
     * @param OldId Old ID
     * @param newId New ID
     * @param cineplexName Cinplex to get cinema to edit
     */
    public void editCinemaId(String OldId, String newId, String cineplexName) {
        Cinema cinema = searchCinema(OldId);

        if (cinema == null) {
            System.out.println("Cinema code does not exist.");
            return;
        }
            
        deleteCinema(OldId);
        cinema.setId(newId);
        createCinema(cineplexName, cinema.getId(), cinema.getClassOfCinema(), cinema.getLayout());
    }

    
    /** 
     * Method to edit cinema class
     * @param Id ID of cinema to edit
     * @param newClassOfCinema new class
     * @param cineplexName Cineplex to get cinema from
     */
    public void editCinemaClass(String Id, String newClassOfCinema, String cineplexName) {
        Cinema cinema = searchCinema(Id);

        if (cinema == null) {
            System.out.println("Cinema code does not exist.");
            return;
        }

        deleteCinema(Id);
        cinema.setClassOfCinema(newClassOfCinema);
        createCinema(cineplexName, Id, newClassOfCinema, cinema.getLayout());
    }

    
    /** 
     * Method to edit cinema layout
     * @param row    number of rows of seats
     * @param column number of columns of seats
     * @param Id     cinema ID
     * @param cinemplexName Cineplex to get cinema from
     */
    public void editCinemaLayout(int row, int column, String Id, String cinemplexName) {
        SeatingLayout layout = new SeatingLayout(row, column);
        Cinema cinema = searchCinema(Id);

        if (cinema == null) {
            System.out.println("Cinema code does not exist.");
            return;
        }

        deleteCinema(Id);
        cinema.setLayout(layout);
        createCinema(cinemplexName, Id, cinema.getClassOfCinema(), cinema.getLayout());
    }

    /**
     * Edit cinema menu
     */
    public void editCinema() {
        int choice = 0;
        while (choice != 4) {
            System.out.println("----- EDIT CINEMA -----\n"
                              +" 1. Edit Cinema ID\n"
                              +" 2. Edit Cinema Class\n"
                              +" 3. Edit Cinema Layout\n"
                              +" 4. Exit"
                              +"----------------------");
                            
            System.out.println("Enter your choice: ");
            while (!sc.hasNextInt()) {
            	System.out.println("Invalid input type. Please enter an integer value.");
        		sc.next(); // remove newline
            }

            choice = sc.nextInt();

            System.out.println("Please enter the Cineplex name the Cinema is located in: ");
            while (!sc.hasNext()) {
                System.out.println("Invalid input type. Please try again!");
                sc.next(); // remove newline
            }
            String cineplexName = sc.nextLine();

            System.out.println("Please enter the current Cinema Code: ");
            while (!sc.hasNext()) {
                System.out.println("Invalid input type. Please try again!");
                sc.next(); // remove newline
            }
            String id = sc.nextLine();

            switch(choice){
                case 1:
                    System.out.println("Please enter the new Cinema Code: ");
                    while (!sc.hasNext()) {
                        System.out.println("Invalid input type. Please try again!");
                        sc.next(); // remove newline
                    }
                    String newId = sc.nextLine();

                    this.editCinemaClass(id, newId, cineplexName);
                    break;
                case 2:
                    System.out.println("Please enter the new Cinema Class: ");
                    while (!sc.hasNext()) {
                        System.out.println("Invalid input type. Please try again!");
                        sc.next(); // remove newline
                    }
                    String cinemaClass = sc.nextLine();

                    this.editCinemaClass(id, cinemaClass, cineplexName);
                    break;
                case 3:
                    System.out.println("Please enter the number of rows: ");
                    while (!sc.hasNextInt()) {
                        System.out.println("Invalid input type. Please try again!");
                        sc.next(); // remove newline
                    }
                    int row = sc.nextInt();

                    System.out.println("Please enter the number of columns: ");
                    while (!sc.hasNextInt()) {
                        System.out.println("Invalid input type. Please try again!");
                        sc.next(); // remove newline
                    }
                    int column = sc.nextInt();

                    this.editCinemaLayout(row, column, id, cineplexName);
                case 4:
                    System.out.println("Cinema Details updated!");
                    break;
                default:
                    System.out.println("Please enter an integer between 1-4.\n");
                    break;
            }
        } 
    }

    /**
     * Menu to view cinemas
     */
    public void viewCinemas() {
        int choice = 0;
        ArrayList<Cinema> cinemas = new ArrayList<Cinema>();
        System.out.println("Please enter name of Cineplex:");
        String cineplexName = sc.nextLine();

        while(choice != 5){
            System.out.println("-------- VIEW CINEMA --------\n"
                              +" 1. View all cinemas\n"
                              +" 2. View cinema by cinema ID\n"
                              +" 3. View cinema by cinema class\n"
                              +" 4. View cinema layout\n"
                              +" 5. Exit\n"
                              +"-----------------------------");

            System.out.println("Please enter your choice:");

            while (!sc.hasNextInt()) {
            	System.out.println("Invalid input type. Please enter an integer value.");
        		sc.next(); // remove newline
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch(choice){
                case 1:
                    cinemas = getCinemasByCineplex(cineplexName);
                    for (Cinema cinema : cinemas) {
                        System.out.println(cinema.makeString());
                    }
                    break;
                case 2:
                    System.out.println("Please enter Cinema ID:");
                    String cinemaId = sc.nextLine();
                    cinemas = getCinemasByCineplex(cineplexName);
                    Cinema cinema = null;
                    for (Cinema temp : cinemas) {
                        if (temp.getId().equals(cinemaId)) {
                            cinema = temp;
                            break;
                        }
                    }
                    System.out.println(cinema.makeString());
                    break;
                case 3:
                    System.out.println("Please enter Cinema Class:");
                    String cinemaClass = sc.nextLine();
                    cinemas = getCinemasByCineplex(cineplexName);
                    ArrayList<Cinema> returnList = new ArrayList<Cinema>();
                    for (Cinema temp : cinemas) {
                        if (temp.getClassOfCinema().equals(cinemaClass)) {
                            returnList.add(temp);
                        }
                    }
                    for (Cinema temp : returnList) {
                        System.out.println(temp.makeString());
                    }
                    break;
                case 4:
                    System.out.println("Please Enter Cinema ID:");
                    String id = sc.nextLine();
                    cinemas = getCinemasByCineplex(cineplexName);
                    Cinema tempCinema = null;
                    for (Cinema temp : cinemas) {
                        if (temp.getId().equals(id)) {
                            tempCinema = temp;
                            break;
                        }
                    }
                    System.out.println("Cinema Layout:");
                    tempCinema.getLayout().printLayout();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
            }
        }
    }
}

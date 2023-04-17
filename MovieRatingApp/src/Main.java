import com.cognixia.jumpplus.Dao.AdminDaoSql;
import com.cognixia.jumpplus.Dao.Film;
import com.cognixia.jumpplus.Dao.User;
import com.cognixia.jumpplus.Dao.UserDaoSql;
import com.cognixia.jumpplus.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome! Please select an option from the menu");
        try (Scanner scanner = new Scanner(System.in)) {

            int choice;
            do {
                System.out.println("1.Register \n2.Login \n3.View Movies\n4.Exit");
                UserDaoSql userDao = new UserDaoSql();
                userDao.setConnection();
                choice = scanner.nextInt();
                if (choice == 1) {
                  signUp(scanner,userDao);
                } else if (choice == 2) {
                    User user = signIn(scanner, userDao);
                    if (user.getRole() == 1) {
                        userActions(userDao, scanner, user);
                    } else {
                        adminActions(scanner);
                    }
                } else if (choice == 3) {
                    userDao.displayFilms();
                }
            } while (choice != 4);
            System.out.println("Thank You!");
        } catch (SQLException | IOException | ClassNotFoundException | ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static String validateEmailAddress(Scanner scanner) {
        String email;
        boolean valid = false;
        do {
            System.out.println("Enter Email \n");
            email = scanner.next();
            if (email.matches("^.+@.+$")) {
                valid = true;
            }else System.out.println("Please enter a valid email");
        } while (!valid);
        return email;
    }
    public static void signUp(Scanner scanner,UserDaoSql userDao) throws SQLException {
        String email = validateEmailAddress(scanner);
        System.out.println("Enter Password");
        String password = scanner.next();

        String confirmation;
        do {
            System.out.println("Confirm Password");
            confirmation = scanner.next();
            if (confirmation.equals(password)) {
                User user = new User(email, password);
                if (userDao.addUser(user)) {
                    System.out.println("User Was Added");
                }
            } else System.out.println("Password doesn't match, try again");
        } while (!confirmation.equals(password));


    }
    public static User signIn(Scanner scanner, UserDaoSql userDao) throws SQLException {

        Optional<User> userFound;
        User user = null;
        do {
            System.out.println("Enter your email");
            String email = scanner.next();
            System.out.println("Enter your password");
            String password = scanner.next();
            userFound = userDao.authenticateUser(email, password);
            if (userFound.isEmpty()) {
                System.out.println("User has not been found");
            } else {
                user = userFound.get();

                System.out.println("Welcome " + user.getEmail());
            }
        } while (userFound.isEmpty());
        return user;
    }

    public static void userActions(UserDaoSql userDao, Scanner scanner, User user) throws SQLException, ResourceNotFoundException {
        int movieToRate;
        int choice;
        do {
            System.out.println("Enter 1 to rate a film or 2 to view your ratings or 3 to Exit");
            choice = scanner.nextInt();
            if (choice == 1) {
                do {
                    userDao.displayFilms();
                    System.out.println("Enter id of the show you'd like to rate or 0 to Exit");
                    movieToRate = scanner.nextInt();
                    if (movieToRate != 0) {
                        Film film = userDao.getFilm(movieToRate);
                        System.out.println("Movie " + film.getName());
                        System.out.println("Rating:\n0. Really Bad\n1. Bad\n2. Not Good\n3. Okay\n4. Good\n5. Great");
                        int rating = scanner.nextInt();
                        if (userDao.addRating(user.getId(), film.getId(), rating))
                            System.out.println("Rating added");
                    }
                } while (movieToRate != 0);
            } else if (choice == 2) {
                userDao.getUserRatings(user.getId());
            }
        } while (choice != 3);
    }

    public static void adminActions(Scanner scanner) throws SQLException, IOException, ClassNotFoundException, ResourceNotFoundException {
        AdminDaoSql adminDaoSql = new AdminDaoSql();
        adminDaoSql.setConnection();
        int choice;
        do {
            adminDaoSql.getFilms();
            System.out.println("1. Add Film\n2. Update Film\n3. Exit");
            choice = scanner.nextInt();

            if (choice == 1) {
                System.out.print("Film Name:");
                scanner.nextLine();
                String movieName = scanner.nextLine();
                Film film = new Film(0, movieName);
                if (adminDaoSql.addFilm(film)) {
                    System.out.println("Film Added");
                } else System.out.println("Error adding the film");
            } else if (choice == 2) {
                System.out.println("Enter id of the Film you'd like to update");

                int filmToUpdate = scanner.nextInt();
                Film film = adminDaoSql.getFilm(filmToUpdate);
                System.out.println("Change Film Name:");
                scanner.nextLine();
                String filmName = scanner.nextLine();
                if (adminDaoSql.updateFilm(film.getId(), filmName))
                    System.out.println("Updated Successfully");
                else System.out.println("Updated Failed");
            }
        } while (choice != 3);
    }
}


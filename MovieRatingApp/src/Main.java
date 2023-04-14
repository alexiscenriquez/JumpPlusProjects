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
            System.out.println("1.Register \n2.Login \n3.View Movies\n4.Exit");

            int choice;
            do {
                UserDaoSql userDao = new UserDaoSql();
                userDao.setConnection();
                choice = scanner.nextInt();
                if (choice == 1) {
                    String email = validateEmailAddress(scanner);
                    System.out.println("Enter Password \n");
                    String password = scanner.next();
                    User user = new User(email, password);

                    if (userDao.addUser(user)) {
                        System.out.println("User Was Added");
                    }
                } else if (choice == 2) {
                    Optional<User> userFound;
                    do {
                        System.out.println("Enter your email");
                        String email = scanner.next();
                        System.out.println("Enter your password");
                        String password = scanner.next();
                        userFound = userDao.authenticateUser(email, password);

                        int movieToRate;
                        if (userFound.isEmpty()) {
                            System.out.println("User has not been found");
                        } else {

                            User user = userFound.get();

                            System.out.println("Welcome " + user.getEmail());
                            if (user.getRole() == 1) {
                                do {
                                    userDao.displayFilms();
                                    System.out.println("Enter id of the show you'd like to rate or 0 to Exit");
                                    movieToRate = scanner.nextInt();
                                    if(movieToRate==0){
                                        System.out.println("1.Register \n2.Login \n3.View Movies\n4.Exit");

                                    }
                                    else {
                                        Film film = userDao.getFilm(movieToRate);
                                        System.out.println("Movie " + film.getName());

                                        System.out.println("Rating:\n0. Really Bad\n1. Bad\n2. Not Good\n3. Okay\n4. Good\n5. Great");
                                        int rating = scanner.nextInt();
                                        if (userDao.addRating(user.getId(), film.getId(), rating))
                                            System.out.println("Rating added");
                                    }
                                } while (movieToRate != 0);
                            } else {
                                AdminDaoSql adminDaoSql = new AdminDaoSql();
                                adminDaoSql.setConnection();
                                int choice2;
                                do {
                                    adminDaoSql.getFilms();
                                    System.out.println("1. Add Film\n2. Update Film\n3.Exit");
                                    choice2 = scanner.nextInt();

                                    if (choice2 == 1) {
                                        System.out.print("Film Name:");
                                        scanner.nextLine();
                                        String movieName = scanner.nextLine();
                                        Film film = new Film(0, movieName);
                                        if (adminDaoSql.addFilm(film)) {
                                            System.out.println("Film Added");
                                        } else System.out.println("Error adding the film");
                                    } else if(choice2==2){
                                        System.out.println("Enter id of the Film you'd like to update");

                                        int filmToUpdate=scanner.nextInt();
                                      Film film=  adminDaoSql.getFilm(filmToUpdate);
                                        System.out.println("Change Film Name:");
                                        scanner.nextLine();
                                        String filmName=scanner.nextLine();
                                       if( adminDaoSql.updateFilm(film.getId(),filmName))
                                           System.out.println("Updated Successfully");
                                           else System.out.println("Updated Failed");
                                    }
                                } while (choice2 != 3);
                                System.out.println("1.Register \n2.Login \n3.View Movies\n4.Exit");
                                break;

                            }
                        }
                    } while (userFound.isEmpty());
                } else if (choice == 3) {
                    userDao.displayFilms();

                    System.out.println("1.Register \n2.Login \n3.View Movies\n4.Exit");
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
            }
        } while (!valid);
        return email;
    }

}
package in.gcet.sis.service;
import org.junit.jupiter.api.Test;import static org.junit.jupiter.api.Assertions.*;
class AuthServiceValidationTest {
 private final AuthService auth=new AuthService();
 @Test void rejectsBlankName(){assertThrows(IllegalArgumentException.class,()->auth.register(" ","person@example.com","password1".toCharArray()));}
 @Test void rejectsInvalidEmail(){assertThrows(IllegalArgumentException.class,()->auth.register("Student","not-an-email","password1".toCharArray()));}
 @Test void rejectsShortPassword(){assertThrows(IllegalArgumentException.class,()->auth.register("Student","person@example.com","short".toCharArray()));}
}
package co.edu.unicauca.mycompany.projects.domain.entities;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    void testCompanyConstructorAndGetters() {
        User userTest = new User("1234", "example@example.com", "password");
        
        assertThat(userTest.getId()).isEqualTo("1234");
        assertThat(userTest.getEmail()).isEqualTo("example@example.com");
        assertThat(userTest.getPassword()).isEqualTo("password");

    }

    @Test
    void testSetters() {
        User user = new User("", "", "");

        user.setId("123456");
        user.setEmail("user@example.com");
        user.setPassword("securepassword");

        assertThat(user.getId()).isEqualTo("123456");
        assertThat(user.getEmail()).isEqualTo("user@example.com");
        assertThat(user.getPassword()).isEqualTo("securepassword");
    }
}

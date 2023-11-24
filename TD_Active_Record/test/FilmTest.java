import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    public int id;

    @BeforeEach
    void setUp() {
        DBConnection.setDbName("testpersonne");
        Personne.createTable();
        Film.createTable();
        Film test = new Film("test", Personne.findByName("test").get(0));
        test.save();
        id = test.getId();
    }


    @AfterEach
    void tearDown() {
        DBConnection.setDbName("testpersonne");
        Film.deleteTable();
        Personne.deleteTable();
        try{
            Objects.requireNonNull(Film.findById(id)).delete();
            DBConnection.getConnection().createStatement().executeUpdate("DROP TABLE Film");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

}
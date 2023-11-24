import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    @Test
    public void testDelete(){
        Film f = new Film("test",Personne.findByName("test").get(0));
        f.save();
        f.delete();
        assertNull(Film.findById(f.getId()));
    }

    @Test
    public void testFindById(){
        Film f = Film.findById(id);
        assertNotNull(f);
        assertEquals(f.getId(),id);
    }

    @Test
    public void testFindByRealisateur(){
        Personne p = Personne.findByName("test").get(0);
        Film f = new Film("test",p);
        f.save();
        assertEquals(f.findByRealisateur().get(0).getId(),f.getId());
    }

}
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class PersonneTest {
    public int id;
    @BeforeEach
    void setUp() throws SQLException {
        DBConnection.setDbName("testpersonne");
        Film.createTable();
        Personne test = new Personne("test","test");
        test.save();
        this.id = test.id;
    }

    @AfterEach
    void tearDown() {
        DBConnection.setDbName("testpersonne");
        if(Personne.findById(this.id)!=null){
            Personne.findById(this.id).delete();
        }
        Film.deleteTable();
        Personne.deleteTable();
    }


    @Test
    public void testDelete(){
        Personne.createTable();
        Film.createTable();
        Personne p = new Personne("test","test");
        p.save();
        Film f = new Film("test",p);
        f.setId_real(p.id);
        f.save();
        p.delete();
        assertNull(Personne.findById(p.id));
    }

}
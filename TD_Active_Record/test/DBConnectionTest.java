import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {

    @org.junit.jupiter.api.Test
    void setDbName() {
        DBConnection.setDbName("testpersonne");
    }

    @org.junit.jupiter.api.Test
    void getConnection() throws SQLException {
        DBConnection.setDbName("testpersonne");
        Connection con = DBConnection.getConnection();
        assertNotNull(con);
        assertTrue(con.isValid(0));
        assertEquals(con, DBConnection.getConnection());

    }

}
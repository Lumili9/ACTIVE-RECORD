import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Film {
    private String titre;
    private int id;
    private int id_rea;

    public Film(String titre, Personne realisateur) {
        this.titre = titre;
        this.id_rea = realisateur.getId();
        this.id = -1;
    }

    private Film(String titre, int id_real, int id) {
        this.titre = titre;
        this.id_rea = id_real;
        this.id = id;
    }

    public static Film findById(int id) {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("SELECT * FROM Film WHERE id=?");
            stat.setInt(1, id);
            stat.execute();
            java.sql.ResultSet rs = stat.getResultSet();
            if (rs.next()) {
                String titre = rs.getString("titre");
                int id_real = rs.getInt("id_rea");
                Film f = new Film(titre, id_real, id);
                return f;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<Film> findByRealisateur() {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("SELECT * FROM Film WHERE id_rea=?");
            stat.setInt(1, id_rea);
            stat.execute();
            java.sql.ResultSet rs = stat.getResultSet();
            ArrayList<Film> tab = new ArrayList<>();
            while (rs.next()) {
                String titre = rs.getString("titre");
                int id_rea = rs.getInt("id_rea");
                int id = rs.getInt("id");
                Film f = new Film(titre, id_rea, id);
                tab.add(f);
            }
            return tab;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Personne getRealisateur() {
        return Personne.findById(id_rea);
    }

    public void setId_real(int id_real) {
        this.id_rea = id_real;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public void update() {
        if(id_rea==-1){
            throw new RuntimeException("Le realisateur n'est pas enregistré");
        }
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("UPDATE Film SET titre=?, id_rea=? WHERE id=?");
            stat.setString(1, titre);
            stat.setInt(2, id_rea);
            stat.setInt(3, id);
            stat.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveNew() {
        if(id_rea==-1){
            throw new RuntimeException("Le realisateur n'est pas enregistré");
        }
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("INSERT INTO Film (titre, id_rea) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            stat.setString(1, titre);
            stat.setInt(2, id_rea);
            stat.execute();
            java.sql.ResultSet rs = stat.getGeneratedKeys();
            if(rs.next()){
                this.id=rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        if(id_rea==-1){
            throw new RuntimeException("Le realisateur n'est pas enregistré");
        }
        if (id == -1) {
            saveNew();
        } else {
            update();
        }
    }


    public void delete() {
        Personne.deleteTable();
        if(id_rea==-1){
            throw new RuntimeException("Le realisateur n'est pas enregistré");
        }
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("DELETE FROM Film WHERE id=?");
            stat.setInt(1, id);
            stat.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable() {
        Personne.createTable();
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("DROP TABLE IF EXISTS Film");
            stat.execute();
            stat = con.prepareStatement(
                    "CREATE TABLE `Film` (\n" +
                    "  `id` int(11) NOT NULL,\n" +
                    "  `titre` varchar(40) NOT NULL,\n" +
                    "  `id_rea` int(11) DEFAULT NULL\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=latin1");
            stat.execute();
            stat =  con.prepareStatement("ALTER TABLE `Film`\n" +
                    "  ADD PRIMARY KEY (`id`),\n" +
                    "  ADD KEY `id_rea` (`id_rea`)");
            stat.execute();
            stat =  con.prepareStatement("ALTER TABLE `Film`\n" +
                    "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8");
            stat.execute();
            stat =  con.prepareStatement("ALTER TABLE `Film`\n" +
                    "  ADD CONSTRAINT `film_ibfk_1` FOREIGN KEY (`id_rea`) REFERENCES `Personne` (`id`)");
            stat.execute();
            stat = con.prepareStatement("INSERT INTO `Film` (`id`, `titre`, `id_rea`) VALUES\n" +
                    "(1, 'Arche perdue', 1),\n" +
                    "(2, 'Alien', 2),\n" +
                    "(3, 'Temple Maudit', 1),\n" +
                    "(4, 'Blade Runner', 2),\n" +
                    "(5, 'Alien3', 4),\n" +
                    "(6, 'Fight Club', 4),\n" +
                    "(7, 'Orange Mecanique', 3)");
            stat.execute();
            System.out.println("Table Film créée");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTable() {
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("DROP TABLE Film");
            stat.execute();
            System.out.println("Table Film supprimée");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

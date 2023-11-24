import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Personne {
    public int id;
    public String nom;
    public String prenom;
    public Personne(String nom,String prenom){
        this.nom=nom;
        this.prenom=prenom;
        this.id=-1;
    }
    //patron active record
    public void save(){
            // Updating
            if(this.id==-1){
                this.saveNew();
            }else{
                this.update();
            }
    }

    public void saveNew(){
        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement stat= con.prepareStatement("INSERT INTO Personne (nom, prenom) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
            stat.setString(1, this.nom);
            stat.setString(2, this.prenom);
            stat.executeUpdate();
            //recuperation de l'id
            java.sql.ResultSet rs = stat.getGeneratedKeys();
            if(rs.next()){
                this.id=rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(){
        if(this.id==-1){
            throw new RuntimeException("Impossible de supprimer une personne non enregistrée");
        }else{
            // Updating
            Connection con = null;
            try {
                con = DBConnection.getConnection();
                try{
                    PreparedStatement stat = con.prepareStatement("DELETE FROM Film WHERE id_rea=?");
                    stat.setInt(1, this.id);
                    stat.executeUpdate();
                }catch (SQLException e){
                    System.out.println("Pas de film pour cette personne");
                }
                PreparedStatement stat = con.prepareStatement("DELETE FROM Personne WHERE id=?");
                stat.setInt(1, this.id);
                stat.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void update(){
        if(this.id==-1){
            throw new RuntimeException("Impossible de mettre à jour une personne non enregistrée");
        }else{
            // Updating
            Connection con = null;
            try {
                con = DBConnection.getConnection();
                PreparedStatement stat = con.prepareStatement("UPDATE Personne SET nom=?, prenom=? WHERE id=?");
                stat.setString(1, this.nom);
                stat.setString(2, this.prenom);
                stat.setInt(3, this.id);
                stat.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void createTable(){
        Connection con = null;

        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("DROP TABLE IF EXISTS Film");
            stat.execute();
            stat = con.prepareStatement("DROP TABLE IF EXISTS Personne");
            stat.execute();
            stat = con.prepareStatement(
                    "CREATE TABLE `Personne` (\n" +
                            "  `id` int(11) NOT NULL,\n" +
                            "  `nom` varchar(40) NOT NULL,\n" +
                            "  `prenom` varchar(40) NOT NULL\n" +
                            ") ENGINE=InnoDB DEFAULT CHARSET=latin1"
            );
            stat.execute();
            stat =  con.prepareStatement("ALTER TABLE `Personne`\n" +
                    "  ADD PRIMARY KEY (`id`)");
            stat.execute();
            stat =  con.prepareStatement("ALTER TABLE `Personne`\n" +
                    "  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5");
            stat.execute();
            stat = con.prepareStatement("INSERT INTO `Personne` (`id`, `nom`, `prenom`) VALUES\n" +
                    "(1, 'Spielberg', 'Steven'),\n" +
                    "(2, 'Scott', 'Ridley'),\n" +
                    "(3, 'Kubrick', 'Stanley'),\n" +
                    "(4, 'Fincher', 'David')");
            stat.execute();
            System.out.println("**** table cree et tuples ajoutes ***");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteTable(){
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("DROP TABLE IF EXISTS Personne");
            stat.execute();
            System.out.println("**** table supprimee ***");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Personne findById(int id){
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("SELECT * FROM Personne WHERE id=?");
            stat.setInt(1, id);
            stat.execute();
            java.sql.ResultSet rs = stat.getResultSet();
            if(rs.next()){
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                Personne p = new Personne(nom,prenom);
                p.id=id;
                return p;
            }else{
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Personne> findByName(String name){
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("SELECT * FROM Personne WHERE nom=?");
            stat.setString(1, name);
            stat.execute();
            java.sql.ResultSet rs = stat.getResultSet();
            ArrayList<Personne> tab = new ArrayList<>();
            while(rs.next()){
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                Personne p = new Personne(nom,prenom);
                p.id=id;
                tab.add(p);
            }
            return tab;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Personne> findAll(){
        Connection con = null;
        try {
            con = DBConnection.getConnection();
            PreparedStatement stat = con.prepareStatement("SELECT * FROM Personne");
            stat.execute();
            java.sql.ResultSet rs = stat.getResultSet();
            ArrayList<Personne> tab = new ArrayList<>();
            while(rs.next()){
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                int id = rs.getInt("id");
                Personne p = new Personne(nom,prenom);
                p.id=id;
                tab.add(p);
            }
            return tab;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String toString(){
        return "("+this.id+") "+this.nom+", "+this.prenom;
    }

    public String getNom(){
        return this.nom;
    }

    public void setNom(String nom){
        this.nom=nom;
    }

    public String getPrenom(){
        return this.prenom;
    }

    public void setPrenom(String prenom){
        this.prenom=prenom;
    }

    public int getId(){
        return this.id;
    }

}

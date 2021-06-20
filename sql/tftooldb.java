import java.sql.Connection;
import java.sql.DriverManager;

public class tftooldb {
    public static void main(String[] args) {
        try{
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tftooldb", "root", "deidara14");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from summoner");

            while(resultSet.next()){
                System.out.println(resultSet.getString("summonerName"))
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
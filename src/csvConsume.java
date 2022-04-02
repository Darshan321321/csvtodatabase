import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static java.lang.Integer.parseInt;

public class csvConsume {
    public static void main(String[] args){

        String jdbcUrl = "jdbc:postgresql://localhost:5432/sampledb";
        // domainDetail is the name of the database go to Xampp server and open Apache and mySQl

        String userName = "postgres";
        String password = "Welldone@328*";

        String filePath = "D:\\volumes\\MOCK_DATA.csv";

        int batchSize = 30;

        Connection connection = null;

        try{
            connection = DriverManager.getConnection(jdbcUrl,userName, password);
            connection.setAutoCommit(false);
            String sql = "Insert into Employee " +
                    "(username,emppassword,email)" +
                    "values(?,?,?)";

            PreparedStatement statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));

            String lineText = null;
            int count = 0;

            lineReader.readLine();
            while((lineText = lineReader.readLine())!= null){
                String[] data = lineText.split(",");

                String username = data[0];
                String emppassword = data[1];
                String email = data[2];


                statement.setString(1,username);
                statement.setString(2,emppassword);
                statement.setString(3,email);


                statement.addBatch();
                if(count%batchSize==0){
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();

            System.out.println("The data is added successfully to the data base.");
        }
        catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
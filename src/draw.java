import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class draw extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {

        StringBuilder output = new StringBuilder();

        String sql_url = "jdbc:mysql://localhost:3306/market_cap";
        String user = "root";
        String password = "";

        /* ---------------------------------------------------------------------------------------------------------- */
        output.append("[] Connecting to ").append(sql_url).append(" ...");
        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
        output.setLength(0);
        /* ---------------------------------------------------------------------------------------------------------- */

        try (Connection con = DriverManager.getConnection(sql_url, user, password)) {

            /* ------------------------------------------------------------------------------------------------------ */
            output.append("[] Done!");
            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
            output.setLength(0);
            /* ------------------------------------------------------------------------------------------------------ */

            try (Statement stmt = con.createStatement()) {
                String query = "SELECT `datum`, `name`, `position`, `cap`, `price`, `volume` FROM `coins`;";

                /* -------------------------------------------------------------------------------------------------- */
                output.append("[] Excecute SQL statement...");
                System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
                output.setLength(0);
                /* -------------------------------------------------------------------------------------------------- */

                ResultSet rs = stmt.executeQuery(query);

                //for storing all entries
                ArrayList<ArrayList<Object>> data = new ArrayList<>();

                //for storing names uniquely
                Set<String> names = new TreeSet<>();

                //tmp for adding rows as arraylist to data
                ArrayList<Object> tmp = new ArrayList<>();

                while (rs.next()) {
                    tmp.add(rs.getString(1));   //date
                    tmp.add(rs.getString(2));   //name
                    tmp.add(rs.getInt(3));      //position
                    tmp.add(rs.getLong(4));     //cap
                    tmp.add(rs.getFloat(5));    //prize
                    tmp.add(rs.getLong(6));     //volume
                    data.add(tmp);
                    names.add(rs.getString(2));
                }

                /* -------------------------------------------------------------------------------------------------- */
                output.append("[] Done! ").append(data.size()).append(" entries were found.");
                System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
                output.setLength(0);
                /* -------------------------------------------------------------------------------------------------- */


                stage.setTitle("Line Chart Sample");
                final CategoryAxis xAxis = new CategoryAxis();
                final NumberAxis yAxis = new NumberAxis();
                xAxis.setLabel("Datum");
                final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);

                lineChart.setTitle("Stock Monitoring, 2018");

                XYChart.Series series1 = new XYChart.Series();
                series1.setName("Portfolio 1");

                series1.getData().add(new XYChart.Data("Jan", 23));
                series1.getData().add(new XYChart.Data("Feb", 14));
                series1.getData().add(new XYChart.Data("Mar", 15));
                series1.getData().add(new XYChart.Data("Apr", 24));
                series1.getData().add(new XYChart.Data("May", 34));
                series1.getData().add(new XYChart.Data("Jun", 36));
                series1.getData().add(new XYChart.Data("Jul", 22));
                series1.getData().add(new XYChart.Data("Aug", 45));
                series1.getData().add(new XYChart.Data("Sep", 43));
                series1.getData().add(new XYChart.Data("Oct", 17));
                series1.getData().add(new XYChart.Data("Nov", 29));
                series1.getData().add(new XYChart.Data("Dec", 25));

                XYChart.Series series2 = new XYChart.Series();
                series2.setName("Portfolio 2");
                series2.getData().add(new XYChart.Data("Jan", 33));
                series2.getData().add(new XYChart.Data("Feb", 34));
                series2.getData().add(new XYChart.Data("Mar", 25));
                series2.getData().add(new XYChart.Data("Apr", 44));
                series2.getData().add(new XYChart.Data("May", 39));
                series2.getData().add(new XYChart.Data("Jun", 16));
                series2.getData().add(new XYChart.Data("Jul", 55));
                series2.getData().add(new XYChart.Data("Aug", 54));
                series2.getData().add(new XYChart.Data("Sep", 48));
                series2.getData().add(new XYChart.Data("Oct", 27));
                series2.getData().add(new XYChart.Data("Nov", 37));
                series2.getData().add(new XYChart.Data("Dec", 29));

                XYChart.Series series3 = new XYChart.Series();
                series3.setName("Portfolio 3");
                series3.getData().add(new XYChart.Data("Jan", 44));
                series3.getData().add(new XYChart.Data("Feb", 35));
                series3.getData().add(new XYChart.Data("Mar", 36));
                series3.getData().add(new XYChart.Data("Apr", 33));
                series3.getData().add(new XYChart.Data("May", 31));
                series3.getData().add(new XYChart.Data("Jun", 26));
                series3.getData().add(new XYChart.Data("Jul", 22));
                series3.getData().add(new XYChart.Data("Aug", 25));
                series3.getData().add(new XYChart.Data("Sep", 43));
                series3.getData().add(new XYChart.Data("Oct", 44));
                series3.getData().add(new XYChart.Data("Nov", 45));
                series3.getData().add(new XYChart.Data("Dec", 44));

                Scene scene = new Scene(lineChart, 1280, 600);
                lineChart.getData().addAll(series1, series2, series3);

                stage.setScene(scene);
                stage.show();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Starter {

    public static void main(String[] args) throws IOException {

        ArrayList<List<String>> rows_arraylist = new ArrayList<>();

        String web_url = "https://coinmarketcap.com/all/views/all/";

        StringBuilder output = new StringBuilder();

        /* ---------------------------------------------------------------------------------------------------------- */
        output.append("[] Connecting to ").append(web_url).append(" ...");
        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
        output.setLength(0);
        /* ---------------------------------------------------------------------------------------------------------- */

        String webPage = Jsoup.connect(web_url).get().outerHtml();

        /* ---------------------------------------------------------------------------------------------------------- */
        output.append("[] Done!");
        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
        output.setLength(0);
        /* ---------------------------------------------------------------------------------------------------------- */

        Document doc = Jsoup.parse(webPage);
        Element table = doc.select("table").get(0);
        Elements rows = table.select("tr");

        Element row;
        Elements cols;

        /* ---------------------------------------------------------------------------------------------------------- */
        output.append("[] Fill and edit Collection...");
        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
        output.setLength(0);
        /* ---------------------------------------------------------------------------------------------------------- */

        // populate arraylist with all td per row
        for (int i = 1; i < 445; i++) {
            row = rows.get(i);
            cols = row.select("td");
            rows_arraylist.add(cols.eachText());
        }

        // delete unwanted entries, remove $ symbol
        for (int i = 0; i < rows_arraylist.size(); i++) {
            rows_arraylist.get(i).remove(9);
            rows_arraylist.get(i).remove(8);
            rows_arraylist.get(i).remove(7);
            rows_arraylist.get(i).remove(5);
            rows_arraylist.get(i).remove(2);

            rows_arraylist.get(i).set(2, rows_arraylist.get(i).get(2).replace("$", "")
                    .replace(",", ""));
            rows_arraylist.get(i).set(3, rows_arraylist.get(i).get(3).replace("$", "")
                    .replace(",", ""));
            rows_arraylist.get(i).set(4, rows_arraylist.get(i).get(4).replace("$", "")
                    .replace(",", ""));
        }

        /* ---------------------------------------------------------------------------------------------------------- */
        output.append("[] Done! ").append(rows_arraylist.size()).append(" entries have been found!");
        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
        output.setLength(0);
        /* ---------------------------------------------------------------------------------------------------------- */

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

            // the mysql insert statement
            String query = " INSERT INTO `coins` (`datum`, `name`, `position`, `cap`, `price`, `volume`)"
                    + " VALUES (?, ?, ?, ?, ?, ?)";

            // create the mysql insert preparedstatement
            PreparedStatement preparedStmt = con.prepareStatement(query);

            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String currentTime = sdf.format(new java.util.Date());

            /* ------------------------------------------------------------------------------------------------------ */
            output.append("[] Prepare and excecute SQL statement...");
            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
            output.setLength(0);
            /* ------------------------------------------------------------------------------------------------------ */

            for (int i = 0; i < rows_arraylist.size(); i++) {
                preparedStmt.setString(1, currentTime);
                preparedStmt.setString(2, rows_arraylist.get(i).get(1));
                preparedStmt.setInt(3, Integer.parseInt(rows_arraylist.get(i).get(0)));
                preparedStmt.setLong(4, Long.parseLong(rows_arraylist.get(i).get(2)));
                preparedStmt.setFloat(5, Float.parseFloat(rows_arraylist.get(i).get(3)));
                preparedStmt.setLong(6, Long.parseLong(rows_arraylist.get(i).get(4)));

                preparedStmt.execute();
            }

            /* ------------------------------------------------------------------------------------------------------ */
            output.append("[] Done!\n");
            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
            output.setLength(0);
            /* ------------------------------------------------------------------------------------------------------ */

        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(Runtime.Version.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}

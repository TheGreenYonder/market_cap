import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;


public class Starter {

    public static void main(String[] args) {

        String landing_page = "https://coinmarketcap.com/all/views/all/";

        StringBuilder output = new StringBuilder();

        /* ---------------------------------------------------------------------------------------------------------- */
        output.append("[] Connecting to ").append(landing_page).append(" ...");
        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
        output.setLength(0);
        /* ---------------------------------------------------------------------------------------------------------- */

        // 10000000 is working for this page
        Document doc;
        try {
            doc = Jsoup.connect(landing_page).maxBodySize(10000000).get();


            /* ---------------------------------------------------------------------------------------------------------- */
            output.append("[] Done!");
            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
            output.setLength(0);
            /* ---------------------------------------------------------------------------------------------------------- */
            output.append("[] Fetch URLs of coins ...");
            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
            output.setLength(0);
            /* ---------------------------------------------------------------------------------------------------------- */

            //get all links to currencies
            Elements coin_urls = doc.select("a.currency-name-container");

            //arraylist for storing all objects
            ArrayList<Coin> coins = new ArrayList<>();

            //for range of histoical data
            String historical_frame = "historical-data/?start=20170110&end=20180110";

            /* ---------------------------------------------------------------------------------------------------------- */
            output.append("[] Done!");
            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
            output.setLength(0);
            /* ---------------------------------------------------------------------------------------------------------- */
            output.append("[] Populate coin objects");
            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
            output.setLength(0);
            /* ---------------------------------------------------------------------------------------------------------- */

            for (Element link : coin_urls) {
                coins.add(new Coin(link.attr("abs:href") + historical_frame, link.text()));
            }

            coins = new ArrayList<>(coins.subList(0, 500));
            //removes object if less than 30 entries (days)
            coins.removeIf(coin -> coin.fetch_market_caps() < 50);

            ArrayList<Long> market_cap_change = new ArrayList<>();
            for (Coin coin : coins) {
                coin.oldest_latest();
                coin.market_cap_change();
                market_cap_change.add(coin.getCap_change());
            }

            for (Coin coin : coins) {
                coin.market_cap_change();
            }

            market_cap_change.sort(Collections.reverseOrder());

            int j;
            for (Long aMarket_cap_change : market_cap_change) {
                j = 0;
                while (coins.get(j).getCap_change() != aMarket_cap_change) {
                    j++;
                }
                System.out.println(coins.get(j).getName());
                System.out.println(NumberFormat.getNumberInstance(Locale.GERMANY).format(coins.get(j).getCap_change()));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


        // populate arraylist with all td per row


//        ArrayList<List<String>> rows_arraylist = new ArrayList<>();
//
//        String web_url = "https://coinmarketcap.com/all/views/all/";
//
//        StringBuilder output = new StringBuilder();
//
//        /* ---------------------------------------------------------------------------------------------------------- */
//        output.append("[] Connecting to ").append(web_url).append(" ...");
//        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//        output.setLength(0);
//        /* ---------------------------------------------------------------------------------------------------------- */
//
//        // 10000000 is working for this page
//        Document doc = Jsoup.connect(web_url).maxBodySize(10000000).get();
//        Element table = doc.select("table").get(0);
//        Elements rows = table.select("tr");
//
//        /* ---------------------------------------------------------------------------------------------------------- */
//        output.append("[] Done!");
//        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//        output.setLength(0);
//        /* ---------------------------------------------------------------------------------------------------------- */
//
//        Element row;
//        Elements cols;
//
//        /* ---------------------------------------------------------------------------------------------------------- */
//        output.append("[] Fill and edit Collection...");
//        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//        output.setLength(0);
//        /* ---------------------------------------------------------------------------------------------------------- */
//
//        // populate arraylist with all td per row
//        for (int i = 1; i < rows.size(); i++) {
//            row = rows.get(i);
//            cols = row.select("td");
//            rows_arraylist.add(cols.eachText());
//        }
//
//        // delete unwanted entries, remove $ symbol
//        for (int i = 0; i < rows_arraylist.size(); i++) {
//            rows_arraylist.get(i).remove(9);
//            rows_arraylist.get(i).remove(8);
//            rows_arraylist.get(i).remove(7);
//            rows_arraylist.get(i).remove(5);
//            rows_arraylist.get(i).remove(2);
//
//            rows_arraylist.get(i).set(2, rows_arraylist.get(i).get(2).replace("$", "")
//                    .replace(",", ""));
//            rows_arraylist.get(i).set(3, rows_arraylist.get(i).get(3).replace("$", "")
//                    .replace(",", ""));
//            rows_arraylist.get(i).set(4, rows_arraylist.get(i).get(4).replace("$", "")
//                    .replace(",", ""));
//        }
//
//        /* ---------------------------------------------------------------------------------------------------------- */
//        output.append("[] Done! ").append(rows_arraylist.size()).append(" entries have been found!");
//        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//        output.setLength(0);
//        /* ---------------------------------------------------------------------------------------------------------- */
//
//        String sql_url = "jdbc:mysql://localhost:3306/market_cap";
//        String user = "root";
//        String password = "";
//
//        /* ---------------------------------------------------------------------------------------------------------- */
//        output.append("[] Connecting to ").append(sql_url).append(" ...");
//        System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//        output.setLength(0);
//        /* ---------------------------------------------------------------------------------------------------------- */
//
//        try (Connection con = DriverManager.getConnection(sql_url, user, password)) {
//
//            /* ------------------------------------------------------------------------------------------------------ */
//            output.append("[] Done!");
//            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//            output.setLength(0);
//            /* ------------------------------------------------------------------------------------------------------ */
//
//            // the mysql insert statement
//            String query = " INSERT INTO `coins` (`datum`, `name`, `position`, `cap`, `price`, `volume`)"
//                    + " VALUES (?, ?, ?, ?, ?, ?)";
//
//            // create the mysql insert preparedstatement
//            PreparedStatement preparedStmt = con.prepareStatement(query);
//
//            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String currentTime = sdf.format(new java.util.Date());
//
//            /* ------------------------------------------------------------------------------------------------------ */
//            output.append("[] Prepare and excecute SQL statement...");
//            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//            output.setLength(0);
//            /* ------------------------------------------------------------------------------------------------------ */
//
//            int err_cnt = 0;
//            for (int i = 0; i < rows_arraylist.size(); i++) {
//
//                // handle error if entries are not in expected format
//                try {
//                    preparedStmt.setString(1, currentTime);
//                    preparedStmt.setString(2, rows_arraylist.get(i).get(1));
//                    preparedStmt.setInt(3, Integer.parseInt(rows_arraylist.get(i).get(0)));
//                    preparedStmt.setLong(4, Long.parseLong(rows_arraylist.get(i).get(2)));
//                    preparedStmt.setFloat(5, Float.parseFloat(rows_arraylist.get(i).get(3)));
//                    preparedStmt.setLong(6, Long.parseLong(rows_arraylist.get(i).get(4)));
//
//                    preparedStmt.execute();
//                } catch (NumberFormatException ex) {
//                    err_cnt++;
//                }
//            }
//
//            /* ------------------------------------------------------------------------------------------------------ */
//            output.append("[] Done! ").append(err_cnt).append(" entries were incomplete and ignored.");
//            System.out.println(output.insert(1, new Exception().getStackTrace()[0].getLineNumber()));
//            output.setLength(0);
//            /* ------------------------------------------------------------------------------------------------------ */
//
//        } catch (SQLException ex) {
//            Logger lgr = Logger.getLogger(Runtime.Version.class.getName());
//            lgr.log(Level.SEVERE, ex.getMessage(), ex);
//        }

    }
}
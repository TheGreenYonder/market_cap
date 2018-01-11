
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

class Coin {
    private String historic_data_url;               //absolute url path
    private HashMap<String, Long> all_market_caps;  //key(date) - long(market_cap)
    private String name;                            //name of coin
    private long min;
    private long max;

    Coin(String historic_data_url, String name) {
        this.historic_data_url = historic_data_url;
        this.all_market_caps = new HashMap<>();
        this.name = name;
        this.min = Long.MAX_VALUE;
        this.max = Long.MIN_VALUE;
    }

    int fetch_market_caps() {
        Document doc = null;
        try {

            //connect to url
            doc = Jsoup.connect(this.historic_data_url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            //find table
            Element table = null;
            if (doc != null) {
                table = doc.select("table").get(0);
            }
            Elements rows = null;
            if (table != null) {
                rows = table.select("tbody").select("tr");
            }

            Element row;
            Elements cols;
            String cap;

            if (rows != null) {

                //loop through rows (days)
                for (Element row1 : rows) {
                    row = row1;
                    cols = row.select("td");

                    //remove , in string to parse it later into Long
                    cap = cols.get(6).text().replace(",", "");

                    //check if string contains of digits only
                    if (!cap.matches("\\D*")) {
                        all_market_caps.put(cols.get(0).text(), Long.parseLong(cap));
                    }
                }
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }

        System.out.println(this.name);
        System.out.println(this.all_market_caps);
        return this.all_market_caps.size();
    }


    void calculate_minmax() {

        this.all_market_caps.forEach((key, value) -> {
            if (value > this.max)
                this.max = value;
            if (value < this.min)
                this.min = value;
        });
    }


    long getMin() {
        return this.min;
    }

    long getMax() {
        return this.max;
    }
}

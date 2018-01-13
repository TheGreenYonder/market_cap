import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

class Coin {
    private String historic_data_url;               //absolute url path
    private ArrayList<Long> all_market_caps;      //key(date) - long(market_cap)
    private String name;                            //name of coin
    private long[] oldest;                           //store
    private long[] latest;
    private long cap_change;

    Coin(String historic_data_url, String name) {
        this.historic_data_url = historic_data_url;
        this.all_market_caps = new ArrayList<>();
        this.name = name;
        this.oldest = new long[10];
        this.latest = new long[10];
    }

    int fetch_market_caps() {
        System.out.println(this.name);
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
                        all_market_caps.add(Long.parseLong(cap));
                    }
                }
            }
        } catch (IndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }

        //System.out.println(this.all_market_caps);
        return this.all_market_caps.size();
    }


    void oldest_latest() {

        for (int i = 0; i < 10; i++) {
            this.latest[i] = this.all_market_caps.get(i);
            this.oldest[i] = this.all_market_caps.get(this.all_market_caps.size() - 1 - i);
        }


        /*for (int i = 0; i < 5; i++)
            this.

        System.out.println(this.name);
        System.out.println("min: "+ NumberFormat.getNumberInstance(Locale.GERMANY).format(this.min));
        System.out.println("max: "+ NumberFormat.getNumberInstance(Locale.GERMANY).format(this.max));*/
    }


    void market_cap_change() {

        long tmp_oldest = 0;
        long tmp_latest = 0;

        for (int i = 0; i < 10; i++) {
            tmp_latest += this.latest[i];
            tmp_oldest += this.oldest[i];
        }

        this.cap_change = tmp_latest / 10 - tmp_oldest / 10;
    }


    long getCap_change() {
        return this.cap_change;
    }

    String getName() {
        return this.name;
    }

}

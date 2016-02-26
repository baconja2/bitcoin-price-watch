package bacon.john.bitcoinpricewatch.bitcoin;

import com.android.volley.*;
import com.android.volley.toolbox.*;

/**
 * @class BitcoinCurrencyService
 *
 * Fetches data about bitcoin in various currencies
 */
public class BitcoinCurrencyService {
    private static BitcoinCurrencyService sharedInstance;
    private RequestQueue requestQueue;

    private BitcoinCurrencyService() {
        Cache cache = new NoCache();
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public static BitcoinCurrencyService sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new BitcoinCurrencyService();
        }
        return sharedInstance;
    }
}

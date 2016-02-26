package bacon.john.bitcoinpricewatch.bitcoin;

import com.android.volley.*;
import com.android.volley.toolbox.*;

/**
 * @class BitcoinService
 *
 * Fetches data about bitcoin
 */
public class BitcoinService {
    private static BitcoinService sharedInstance;
    private RequestQueue requestQueue;

    private BitcoinService() {
        Cache cache = new NoCache();
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public static BitcoinService sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new BitcoinService();
        }
        return sharedInstance;
    }
}

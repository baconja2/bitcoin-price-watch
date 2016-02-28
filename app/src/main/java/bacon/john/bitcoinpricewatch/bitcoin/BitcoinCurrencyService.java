package bacon.john.bitcoinpricewatch.bitcoin;

import com.android.volley.*;
import com.android.volley.toolbox.*;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @class BitcoinCurrencyService
 *
 * Fetches data about bitcoin in various currencies
 */
public class BitcoinCurrencyService {
    public interface Callback {
        public void callback(JSONObject response, Exception error);
    }

    private static BitcoinCurrencyService sharedInstance;

    private static final String CURRENCY_CODE_BASE_URL = "https://api.bitcoinaverage.com/ticker/global/";

    private RequestQueue requestQueue;

    private Map<String,String> codeMap;

    private BitcoinCurrencyService() {
        Cache cache = new NoCache();
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        codeMap = new HashMap<String,String>();
    }

    public static BitcoinCurrencyService sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new BitcoinCurrencyService();
        }
        return sharedInstance;
    }

    public void fetchCurrencyCodes(final Callback callback) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, CURRENCY_CODE_BASE_URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.callback(response, null);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.callback(null, error);
                    }
                });
        requestQueue.add(req);
    }

    public void fetchCurrencyPrices(String code, final Callback callback) {
        if (code == null) {
            throw new AssertionError("currency code must not be null");
        }
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET, CURRENCY_CODE_BASE_URL + code, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                callback.callback(response, null);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.callback(null, error);
            }
        });
        requestQueue.add(req);
    }
}

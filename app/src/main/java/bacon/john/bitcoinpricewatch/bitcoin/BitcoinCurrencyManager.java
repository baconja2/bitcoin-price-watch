package bacon.john.bitcoinpricewatch.bitcoin;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bacon.john.bitcoinpricewatch.localstorage.LocalStorageService;

/**
 * @class BitcoinCurrencyManager
 *
 * Retrieves information about bitcoin in various currencies
 */
public class BitcoinCurrencyManager {
    public interface CurrencyListCallback {
        public void callback(List<BitcoinCurrencyModel> currencies, Exception error);
    }
    public interface CurrencyCallback {
        public void callback(BitcoinCurrencyModel currency, Exception error);
    }

    private static BitcoinCurrencyManager sharedInstance;

    public static BitcoinCurrencyManager sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new BitcoinCurrencyManager();
        }
        return sharedInstance;
    }

    public List<BitcoinCurrencyModel> getCachedCurrencies() {
        return LocalStorageService.sharedInstance().getRealm().where(BitcoinCurrencyModel.class).findAll();
    }

    public BitcoinCurrencyModel getCachedCurrency(String currencyCode) {
        return LocalStorageService.sharedInstance().getRealm().
                where(BitcoinCurrencyModel.class).
                equalTo("code", currencyCode).findFirst();
    }

    public void getRemoteCurrencyCodes(final CurrencyListCallback callback) {
        BitcoinCurrencyService.sharedInstance().fetchCurrencyCodes(new BitcoinCurrencyService.Callback() {
            @Override
            public void callback(JSONObject response, Exception error) {
                if (error != null) {
                    callback.callback(null, error);
                    return;
                } else {
                    callback.callback(BitcoinCurrencyManager.modelsFromJSON(response), null);
                }
            }
        });
    }

    public void getRemoteCurrencyPrices(BitcoinCurrencyModel currency, final CurrencyCallback callback) {
        BitcoinCurrencyService.sharedInstance().fetchCurrencyPrices(currency.getCode(), new BitcoinCurrencyService.Callback() {
            @Override
            public void callback(JSONObject response, Exception error) {
                if (error != null) {
                    callback.callback(null, error);
                    return;
                } else {
                    callback.callback(BitcoinCurrencyManager.modelFromJSON(response), null);
                }
            }
        });
    }

    // Parsing methods
    private static List<BitcoinCurrencyModel> modelsFromJSON(JSONObject json) {
        List<BitcoinCurrencyModel> models = new ArrayList<BitcoinCurrencyModel>();

        // faking it for now
        BitcoinCurrencyModel model = new BitcoinCurrencyModel();
        model.setCode("USD");

        models.add(model);

        BitcoinCurrencyModel model2 = new BitcoinCurrencyModel();
        model2.setCode("XYZ");

        models.add(model2);

        return models;
    }
    private static BitcoinCurrencyModel modelFromJSON(JSONObject json) {
        // faking it for now
        BitcoinCurrencyModel model = new BitcoinCurrencyModel();
        model.setCode("USD");

        return model;
    }
}

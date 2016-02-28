package bacon.john.bitcoinpricewatch.bitcoin;

import org.json.JSONObject;

import java.util.*;
import org.json.*;
import java.text.*;
import io.realm.*;

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
        RealmResults<BitcoinCurrencyModel> results = LocalStorageService.sharedInstance().getRealm().where(BitcoinCurrencyModel.class).findAll();
        results.sort("code");
        return results;
    }

    public BitcoinCurrencyModel getCachedCurrency(String currencyCode) {
        return LocalStorageService.sharedInstance().getRealm().
                where(BitcoinCurrencyModel.class).
                equalTo("code", currencyCode).findFirst();
    }

    public void getRemoteCurrencyCodes(final CurrencyListCallback callback) {
        BitcoinCurrencyService.sharedInstance().fetchCurrencyCodes(new BitcoinCurrencyService.Callback() {
            @Override
            public void callback(final JSONObject response, Exception error) {
                if (error != null) {
                    callback.callback(null, error);
                    return;
                } else {
                    LocalStorageService.sharedInstance().getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Iterator<String> iter = response.keys();
                            while (iter.hasNext()) {
                                String code = iter.next();
                                // filter out "all" since it really is not a ticker
                                if (code.equals("all")) {
                                    continue;
                                }
                                BitcoinCurrencyModel model = realm.where(BitcoinCurrencyModel.class).
                                        equalTo("code", code).findFirst();
                                if (model == null) {
                                    model = new BitcoinCurrencyModel();
                                    model.setCode(code);
                                    realm.copyToRealm(model);
                                }
                            }
                        }
                    }, new Realm.Transaction.Callback() {
                        @Override
                        public void onSuccess() {
                            callback.callback(BitcoinCurrencyManager.sharedInstance().getCachedCurrencies(), null);
                        }
                    });
                }
            }
        });
    }

    public void getRemoteCurrencyPrices(BitcoinCurrencyModel currency, final CurrencyCallback callback) {
        final String code = currency.getCode();
        BitcoinCurrencyService.sharedInstance().fetchCurrencyPrices(code, new BitcoinCurrencyService.Callback() {
            @Override
            public void callback(final JSONObject response, Exception error) {
                if (error != null) {
                    callback.callback(null, error);
                    return;
                } else {
                    LocalStorageService.sharedInstance().getRealm().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            BitcoinCurrencyModel model = realm.where(BitcoinCurrencyModel.class).
                                    equalTo("code", code).findFirst();

                            try {
                                model.setAsk(response.getDouble("ask"));
                                model.setBid(response.getDouble("bid"));
                                model.setLast(response.getDouble("last"));

                                String updatedAt = response.getString("timestamp");
                                DateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
                                Date date = format.parse(updatedAt);
                                model.setUpdatedAt(date.getTime());
                            } catch (Exception e) {
                                callback.callback(null, e);
                            }
                        }
                    }, new Realm.Transaction.Callback() {
                        @Override
                        public void onSuccess() {
                            callback.callback(BitcoinCurrencyManager.sharedInstance().getCachedCurrency(code), null);
                        }
                    });
                }
            }
        });
    }
}

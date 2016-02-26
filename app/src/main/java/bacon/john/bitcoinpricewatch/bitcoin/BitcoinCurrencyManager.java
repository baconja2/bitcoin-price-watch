package bacon.john.bitcoinpricewatch.bitcoin;

import java.util.List;

import bacon.john.bitcoinpricewatch.localstorage.LocalStorageService;

/**
 * @class BitcoinCurrencyManager
 *
 * Retrieves information about bitcoin in various currencies
 */
public class BitcoinCurrencyManager {
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
}

package bacon.john.bitcoinpricewatch.localstorage;

import io.realm.*;
import android.content.*;

/**
 * @class LocalStorageService
 *
 * Stores and retrieves data that is locally
 * persisted to disk
 */
public class LocalStorageService {
    private static LocalStorageService sharedInstance;
    private Realm realm;

    public void initialize(Context context) {
        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
        realm = Realm.getInstance(config);
    }

    private LocalStorageService() {
        // nothing for now
    }

    public static LocalStorageService sharedInstance() {
        if (sharedInstance == null) {
            sharedInstance = new LocalStorageService();
        }
        return sharedInstance;
    }

    public Realm getRealm() {
        return realm;
    }
}

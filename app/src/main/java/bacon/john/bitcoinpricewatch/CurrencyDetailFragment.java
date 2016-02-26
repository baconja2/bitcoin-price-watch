package bacon.john.bitcoinpricewatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bacon.john.bitcoinpricewatch.bitcoin.BitcoinCurrencyManager;
import bacon.john.bitcoinpricewatch.bitcoin.BitcoinCurrencyModel;

/**
 * A fragment representing a single Currency detail screen.
 * This fragment is either contained in a {@link CurrencyListActivity}
 * in two-pane mode (on tablets) or a {@link CurrencyDetailActivity}
 * on handsets.
 */
public class CurrencyDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String CURRENCY_CODE = "currencyCode";

    /**
     * The content this fragment is presenting.
     */
    private BitcoinCurrencyModel currency;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CurrencyDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(CURRENCY_CODE)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            String currencyCode = getArguments().getString(CURRENCY_CODE);
            currency = BitcoinCurrencyManager.sharedInstance().getCachedCurrency(currencyCode);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency_detail, container, false);

        if (currency != null) {
            ((TextView) rootView.findViewById(R.id.currency_detail)).setText(currency.getCode());
        }

        return rootView;
    }
}

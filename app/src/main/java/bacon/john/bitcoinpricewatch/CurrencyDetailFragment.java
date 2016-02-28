package bacon.john.bitcoinpricewatch;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

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
    private BitcoinCurrencyModel mCurrency;

    /**
     * The timer for fetching the latest info
     */
    private Timer mTimer;

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
            mCurrency = BitcoinCurrencyManager.sharedInstance().getCachedCurrency(currencyCode);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_currency_detail, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.refreshView();
    }

    @Override
    public void onStart() {
        super.onStart();

        long delay = 10000;
        long timeSinceLastUpdate = System.currentTimeMillis() - mCurrency.getUpdatedAt();

        // refresh immediately if it has been more then 10 seconds
        if (timeSinceLastUpdate >= 10000) {
            refresh();
        } else {
            delay = 10000 - timeSinceLastUpdate;
        }

        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                refresh();
            }
        }, delay, 10000);
    }

    @Override
    public void onStop() {
        super.onStop();
        mTimer.cancel();
        mTimer = null;
    }

    private void refresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BitcoinCurrencyManager.sharedInstance().getRemoteCurrencyPrices(mCurrency, new BitcoinCurrencyManager.CurrencyCallback() {
                    @Override
                    public void callback(BitcoinCurrencyModel currency, Exception error) {
                        if (getActivity() != null) {
                            if (error == null) {
                                mCurrency = currency;
                                refreshView();
                            } else {
                                if (getView() != null) {
                                    ((TextView)getView().findViewById(R.id.currency_detail)).setText("There was a problem loading data for this currency - please try again later.");
                                }
                            }
                        }
                    }
                });
            }
        });
    }

    private void refreshView() {
        if (getView() != null) {
            if (mCurrency.getUpdatedAt() > 0) {
                String displayString = "Ask Price: " + mCurrency.getAsk() + "\n\n";
                displayString += "Bid Price: " + mCurrency.getBid() + "\n\n";
                displayString += "Last Price: " + mCurrency.getLast() + "\n\n";
                displayString += "Last Updated: " + mCurrency.getUpdatedAt();

                ((TextView)getView().findViewById(R.id.currency_detail)).setText(displayString);
            } else {
                ((TextView)getView().findViewById(R.id.currency_detail)).setText("Loading...");
            }
        }
    }
}

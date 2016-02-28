package bacon.john.bitcoinpricewatch;

import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.List;
import android.view.*;
import android.widget.*;
import bacon.john.bitcoinpricewatch.bitcoin.BitcoinCurrencyModel;

/**
 * @class CurrencyListAdapter
 *
 * Populates a list view with currency codes based on
 * a list of currency models
 */
public class CurrencyListAdapter extends ArrayAdapter<BitcoinCurrencyModel> {
    public CurrencyListAdapter(Context context, List<BitcoinCurrencyModel> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BitcoinCurrencyModel currency = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_activated_1, parent, false);
        }

        TextView textView = (TextView)convertView.findViewById(android.R.id.text1);
        textView.setText(currency.getCode());

        return convertView;
    }
}

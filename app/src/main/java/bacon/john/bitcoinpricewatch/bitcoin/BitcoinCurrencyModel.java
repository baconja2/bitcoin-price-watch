package bacon.john.bitcoinpricewatch.bitcoin;

import io.realm.*;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * @class BitcoinCurrencyModel
 */
public class BitcoinCurrencyModel extends RealmObject {
    @PrimaryKey
    private String code;

    private double ask;
    private double bid;
    private double last;

    private long updatedAt;

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public double getAsk() {
        return ask;
    }
    public void setAsk(double ask) {
        this.ask = ask;
    }
    public double getBid() {
        return bid;
    }
    public void setBid(double bid) {
        this.bid = bid;
    }
    public double getLast() {
        return last;
    }
    public void setLast(double last) { this.last = last; }
    public long getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package entity;

public class DayTradeOrder {
    private int trade_order_idx;
    private int day;
    private int notional;
    private String traderName;
    private int max_switching_window;


    public int getTrade_order_idx() {
        return trade_order_idx;
    }

    public int getDay() {
        return day;
    }

    public int getNotional() {
        return notional;
    }

    public String getTraderName() {
        return traderName;
    }

    public int getMax_switching_window() {
        return max_switching_window;
    }

    public DayTradeOrder(int trade_order_idx, String traderName, int max_switching_window, int day, int notional){
        this.trade_order_idx = trade_order_idx;
        this.traderName = traderName;
        this.max_switching_window = max_switching_window;
        this.day = day;
        this.notional = notional;
    }
}

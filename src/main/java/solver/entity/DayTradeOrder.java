package solver.entity;

public class DayTradeOrder {
    private int traderIdx;
    private int day;
    private int notional;
    private String traderName;
    private int maxSwitchingWindow;


    public int getTraderIdx() {
        return traderIdx;
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

    public int getMaxSwitchingWindow() {
        return maxSwitchingWindow;
    }

    public DayTradeOrder(int trade_order_idx, String traderName, int maxSwitchingWindow, int day, int notional){
        this.traderIdx = trade_order_idx;
        this.traderName = traderName;
        this.maxSwitchingWindow = maxSwitchingWindow;
        this.day = day;
        this.notional = notional;
    }
}

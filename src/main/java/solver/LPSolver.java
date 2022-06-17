package solver;

import entity.DayTradeOrder;
import entity.TraderOrders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LPSolver {

    // Parse the traders orders and build the objectives and constraints.
    // Find number of Traders
    // Solve each objective sequentially.
    List<TraderOrders> mTraderOrders;
    Map<Integer, List<DayTradeOrder>> mDayTradeOrders;

    Integer mNumberOfTraders;

    Integer mMaxDays = 0;

    public LPSolver(List<TraderOrders> traderOrders){
        this.mTraderOrders = traderOrders;
    }

    public void createDayTradeOrders(){
        mDayTradeOrders = new HashMap<>();
        AtomicInteger idx = new AtomicInteger();
        mTraderOrders.forEach( (t_o) -> {
            idx.incrementAndGet();
            t_o.orders.forEach((o)->{
                DayTradeOrder dayTradeOrder = new DayTradeOrder(idx.get(),
                        t_o.trader, t_o.max_switching_window,
                        o.day, o.notional);
                List<DayTradeOrder> dayTradeList = mDayTradeOrders.get(Integer.valueOf(idx.get()));
                if(dayTradeList == null){
                    dayTradeList = new ArrayList<>();
                }
                dayTradeList.add(dayTradeOrder);
                mDayTradeOrders.put(Integer.valueOf(idx.get()), dayTradeList);
                if(t_o.max_switching_window > mMaxDays){
                    mMaxDays = Integer.valueOf(t_o.max_switching_window);
                }
            });
        });

        mNumberOfTraders = Integer.valueOf(idx.get());
    }

    public List<TraderOrders> getTraderOrders(){
        return mTraderOrders;
    }

    public Map<Integer, List<DayTradeOrder>> getDayTradeOrders(){
        return mDayTradeOrders;
    }

    public int getNumberOfTraders(){
        return mNumberOfTraders.intValue();
    }

    public int getMaxDays(){
        return mMaxDays.intValue();
    }
}

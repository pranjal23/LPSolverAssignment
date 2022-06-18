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
    // Build day trade Orders to solve each objective sequentially.
    List<TraderOrders> mTraderOrders;
    Map<Integer, Map<Integer, DayTradeOrder>> mDayTradeOrders;

    Integer mNumberOfTraders;

    Integer mMaxDays = 0;

    public LPSolver(List<TraderOrders> traderOrders){
        this.mTraderOrders = traderOrders;
    }

    public void createDayTradeOrders(){
        // Create real trade orders
        mDayTradeOrders = new HashMap<>();
        AtomicInteger idx = new AtomicInteger();
        mTraderOrders.forEach( (t_o) -> {
            idx.incrementAndGet();
            t_o.orders.forEach((o)->{
                DayTradeOrder dayTradeOrder = new DayTradeOrder(idx.get(),
                        t_o.trader, t_o.max_switching_window,
                        o.day, o.notional);
                Map<Integer, DayTradeOrder> dayTradeMap = mDayTradeOrders.get(idx.get());
                if(dayTradeMap == null){
                    dayTradeMap = new HashMap<>();
                }
                dayTradeMap.put(dayTradeOrder.getTrade_order_idx(),dayTradeOrder);
                mDayTradeOrders.put(idx.get(), dayTradeMap);
                if(t_o.max_switching_window > mMaxDays){
                    mMaxDays = t_o.max_switching_window;
                }
            });
        });

        // Set number of traders
        mNumberOfTraders = idx.get();
    }

    public List<TraderOrders> getTraderOrders(){
        return mTraderOrders;
    }

    public Map<Integer, Map<Integer, DayTradeOrder>> getDayTradeOrders(){
        return mDayTradeOrders;
    }

    public int getNumberOfTraders(){
        return mNumberOfTraders;
    }

    public int getMaxDays(){
        return mMaxDays;
    }
}

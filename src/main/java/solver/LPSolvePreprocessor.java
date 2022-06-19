/*
Copyright (c) 2022 Pranjal Swarup
All rights reserved
*/

package solver;

import solver.entity.DayTradeOrder;
import solver.entity.TraderOrders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LPSolvePreprocessor {

    // Parse the traders orders and build the objectives and constraints.
    // Find number of Traders
    // Build day trade Orders to solve each objective sequentially.
    private List<TraderOrders> mTraderOrders;
    private Map<Integer, Map<Integer, DayTradeOrder>> mDayTraderOrderMap;
    private Integer mNumberOfTraders;

    private Integer mMaxDays = 0;

    public LPSolvePreprocessor(List<TraderOrders> traderOrders){
        this.mTraderOrders = traderOrders;
    }

    public void createDayTradeOrders(){
        // Create real trade orders
        mDayTraderOrderMap = new HashMap<>();
        AtomicInteger idx = new AtomicInteger();
        mTraderOrders.forEach( (t_o) -> {
            idx.incrementAndGet();
            t_o.orders.forEach((o)->{
                DayTradeOrder dayTradeOrder = new DayTradeOrder(idx.get(),
                        t_o.trader, t_o.max_switching_window,
                        o.day, o.notional);
                Map<Integer, DayTradeOrder> dayTradeMap = mDayTraderOrderMap.get(dayTradeOrder.getDay());
                if(dayTradeMap == null){
                    dayTradeMap = new HashMap<>();
                }
                dayTradeMap.put(dayTradeOrder.getTraderIdx(),dayTradeOrder);
                mDayTraderOrderMap.put(dayTradeOrder.getDay(), dayTradeMap);
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

    public Map<Integer, Map<Integer, DayTradeOrder>> getDayTraderOrderMap(){
        return mDayTraderOrderMap;
    }

    public int getNumberOfTraders(){
        return mNumberOfTraders;
    }

    public int getMaxDays(){
        return mMaxDays;
    }
}

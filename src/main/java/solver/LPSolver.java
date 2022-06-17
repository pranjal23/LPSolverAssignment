package solver;

import entity.DayTradeOrder;
import entity.TraderOrders;

import java.util.List;
import java.util.Map;

public class LPSolver {

    // Parse the traders orders and build the objectives and constraints.
    // Solve each objective sequentially.
    List<TraderOrders> mTraderOrders;
    Map<Integer, List<DayTradeOrder>> mDayOrders;

    public LPSolver(List<TraderOrders> traderOrders){
        this.mTraderOrders = traderOrders;
    }

    public void createDayTradeOrders(){
            int currentDay = 1;
            
    }

}

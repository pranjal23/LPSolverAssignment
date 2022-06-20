package solver;

import app.UIController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import solver.entity.TraderOrders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SolverTask extends Task {

    LPSolvePreprocessor preprocessor;
    UIController controller;

    ObservableList<ObservableList> period_data;

    public SolverTask(LPSolvePreprocessor preprocessor, UIController controller){
        this.preprocessor = preprocessor;
        this.controller = controller;
    }

    @Override
    protected Object call() throws Exception {
        preprocessor.createDayTradeOrders();
        // Build and solve the LP problem from the pre-processed data
        double[] results = LPSolver.solve(preprocessor);
        //System.out.println(Arrays.toString(results));

        // Convert to 2D list for display
        int M = preprocessor.getMaxDays();
        int N = preprocessor.getNumberOfTraders();

        period_data =  FXCollections.observableArrayList();
        // Add extra column for day number
        for(int i=0; i < M; i++){
            ObservableList<String> day_data = FXCollections.observableArrayList();
            for(int j=0; j < N+1; j++) {
                day_data.add(Integer.valueOf(0).toString());
            }
            period_data.add(day_data);
        }

        // fill the day column data
        for(int i=0; i < M; i++){
            int day = i+1;
            period_data.get(i).set(0, Integer.valueOf(day).toString());
            for(int traderIdx=1; traderIdx<=preprocessor.getNumberOfTraders(); traderIdx++) {
                int unknownVariableIndex = LPSolverUtil.getUnknownVariableIndex(M,day, traderIdx);
                int indexInArray = unknownVariableIndex - 1; // as arrays begin from 0
                double value = results[indexInArray];
                period_data.get(i).set(traderIdx, Integer.valueOf((int) value).toString());
            }
        }
        //System.out.println(period_data.toString());
        return new Object();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        controller.initTable(preprocessor,period_data);
    }
}

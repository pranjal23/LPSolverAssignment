package solver;

import app.UIController;
import javafx.concurrent.Task;
import solver.entity.TraderOrders;

import java.util.List;

public class SolverTask extends Task {

    LPSolvePreprocessor preprocessor;
    UIController controller;

    double[] results;

    public SolverTask(LPSolvePreprocessor preprocessor, UIController controller){
        this.preprocessor = preprocessor;
        this.controller = controller;
    }

    @Override
    protected Object call() throws Exception {
        preprocessor.createDayTradeOrders();
        // Build and solve the LP problem from the pre-processed data
        results = LPSolver.solve(preprocessor);
        return new Object();
    }

    @Override
    protected void succeeded() {
        super.succeeded();
        controller.initTable(preprocessor,results);
    }
}

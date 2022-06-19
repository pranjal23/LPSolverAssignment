package solver;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import solver.entity.DayTradeOrder;

public class LPSolver {
    public static double[] solve(LPSolvePreprocessor preprocessor) throws LpSolveException {
        int M = preprocessor.getMaxDays();
        int N = preprocessor.getNumberOfTraders();

        // define the total number of variables
        int unknownVariables = M * N;

        // create solver object with 0 constraints initially and unknown variables
        LpSolve solver = LpSolve.makeLp(0, unknownVariables);

        // set the unknown variables as Integer
        for(int i=1; i <= unknownVariables; i++){
            solver.setInt(i, true);
        }

        // Add variable bounds constraints
        for(int day=1; day<=M; day++){
            // for through the traders
            for(int traderIdx=1; traderIdx<=N; traderIdx++){
                DayTradeOrder dto = preprocessor.getDayTraderOrderMap().get(day).get(traderIdx);
                int varIdx = LPSolverUtil.getVariableIndex(M,day, traderIdx);
                if(dto.getNotional()>0) {
                    solver.setBounds(varIdx,0, dto.getNotional());
                } else {
                    solver.setBounds(varIdx, dto.getNotional(), 0);
                }
            }
        }

        // TODO
        // add switching window constraints
        // add net zero constraints


        // set coefficients of each variable of objective function to 1s
        StringBuilder objectiveFn = new StringBuilder();
        for(int i=1; i <= unknownVariables; i++){
            objectiveFn.append("1");
            if(i<unknownVariables)
                objectiveFn.append(" ");
        }

        // set the objective function
        solver.strSetObjFn(objectiveFn.toString());

        // set to maximize objective function
        solver.setMaxim();

        // print the linear regression problem
        solver.printLp();

        // set to verbose to see messages on screen
        solver.setVerbose(LpSolve.IMPORTANT);

        // solve the problem
        solver.solve();

        // get out value of the maximized objective function
        double Z = solver.getObjective();

        double[] var = solver.getPtrVariables();

        // delete the problem and free memory
        solver.deleteLp();

        return var;
    }
}

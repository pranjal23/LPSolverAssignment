/*
Copyright (c) 2022 Pranjal Swarup
All rights reserved
*/

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

        // set the unknown variables
        for(int i=1; i <= unknownVariables; i++){
            // set the unknown variables as Integer
            solver.setInt(i, true);
            // set the unknown variables to 0, 0 max min initially
            solver.setBounds(i,0, 0);
        }

        // add variable bounds constraints as per the inputs
        // go through the day
        for(int day=1; day<=M; day++){
            // go through the traders
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


        // add net zero constraints per trader
        for(int idx=1; idx<=N; idx++){
            int arr[] = new int[unknownVariables];
            for(int day=1; day<=M; day++){
                for(int traderIdx=1; traderIdx<=N; traderIdx++){

                }
            }
            solver.strAddConstraint("6 9", LpSolve.LE, 72);
        }

        // TODO add switching window constraints


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

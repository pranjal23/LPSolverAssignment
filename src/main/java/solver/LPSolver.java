/*
Copyright (c) 2022 Pranjal Swarup
All rights reserved
*/

package solver;
import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import solver.entity.DayTradeOrder;

import java.util.Arrays;

public class LPSolver {
    public static double[] solve(LPSolvePreprocessor preprocessor) throws LpSolveException {
        int M = preprocessor.getMaxDays();
        int N = preprocessor.getNumberOfTraders();
        System.out.println("Count days: " + M);
        System.out.println("Count traders: " + N);

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
                int unknownVariableIndex = LPSolverUtil.getUnknownVariableIndex(M,day, traderIdx);
                if(dto.getNotional()>0) {
                    solver.setBounds(unknownVariableIndex,0, dto.getNotional());
                } else {
                    solver.setBounds(unknownVariableIndex, dto.getNotional(), 0);
                }
            }
        }
        
        // add net zero constraints per trader
        for(int idx=1; idx<=N; idx++){
            double[] constraintArray = new double[unknownVariables+1]; //LPsolve ignore index 0
            for(int day=1; day<=M; day++){
                for(int traderIdx=1; traderIdx<=N; traderIdx++){
                    int unknownVariableIndex = LPSolverUtil.getUnknownVariableIndex(M,day, traderIdx);
                    if(idx==traderIdx){
                        constraintArray[unknownVariableIndex] = 1;
                    } else {
                        constraintArray[unknownVariableIndex] = 0;
                    }
                }
            }
            solver.addConstraint(constraintArray, LpSolve.EQ,0);
        }

        // add net zero constraints per day
        for(int dDay=1; dDay<=M; dDay++){
            double[] constraintArray = new double[unknownVariables+1]; //LPsolve ignore index 0
            for(int day=1; day<=M; day++){
                for(int traderIdx=1; traderIdx<=N; traderIdx++){
                    int unknownVariableIndex = LPSolverUtil.getUnknownVariableIndex(M,day, traderIdx);
                    if(dDay==day){
                        constraintArray[unknownVariableIndex] = 1;
                    } else {
                        constraintArray[unknownVariableIndex] = 0;
                    }
                }
            }
            solver.addConstraint(constraintArray, LpSolve.EQ,0);
        }

        // TODO add switching window constraints


        // set coefficients of each variable of objective function, +1 for sell, -1 for buy (mock abs fn)
        double[] objFnArray = new double[unknownVariables+1]; //LPsolve ignore index 0
        for(int day=1; day<=M; day++){
            // go through the traders
            for(int traderIdx=1; traderIdx<=N; traderIdx++){
                DayTradeOrder dto = preprocessor.getDayTraderOrderMap().get(day).get(traderIdx);
                int unknownVariableIndex = LPSolverUtil.getUnknownVariableIndex(M,day, traderIdx);
                if(dto.getNotional()>0) {
                    objFnArray[unknownVariableIndex] = 1;
                } else {
                    objFnArray[unknownVariableIndex] = -1;
                }
            }
        }

        // set the objective function
        solver.setObjFn(objFnArray);

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

/*
Copyright (c) 2022 Pranjal Swarup
All rights reserved
*/

package solver;

public class LPSolverUtil {

    public static int getVariableIndex(int maxDays, int day, int traderTdx){
        return day + maxDays * (traderTdx - 1);
    }
}

package org.klomp.cassowary;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CassowaryTest {
    private static double EPSILON = 1.0e-8;

    @Test
    public void simple1() throws ExCLInternalError, ExCLRequiredFailure {
        ClVariable x = new ClVariable(167);
        ClVariable y = new ClVariable(2);
        ClSimplexSolver solver = new ClSimplexSolver();
        ClLinearEquation eq = new ClLinearEquation(x, new ClLinearExpression(y));
        solver.addConstraint(eq);
        assertEquals(x.value(), y.value(), EPSILON);
    }

    @Test
    public void justStay1() throws ExCLInternalError, ExCLRequiredFailure {
        ClVariable x = new ClVariable(5);
        ClVariable y = new ClVariable(10);
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addStay(x);
        solver.addStay(y);
        assertEquals(5, x.value(), EPSILON);
        assertEquals(10, y.value(), EPSILON);
    }

    @Test
    public void addDelete1() throws ExCLInternalError, ExCLRequiredFailure, ExCLConstraintNotFound {
        ClVariable x = new ClVariable("x");
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addConstraint(new ClLinearEquation(x, 100, ClStrength.weak));

        ClLinearInequality c10 = new ClLinearInequality(x, CL.LEQ, 10.0);
        ClLinearInequality c20 = new ClLinearInequality(x, CL.LEQ, 20.0);

        solver.addConstraint(c10).addConstraint(c20);

        assertEquals(10, x.value(), EPSILON);

        solver.removeConstraint(c10);
        assertEquals(20, x.value(), EPSILON);

        solver.removeConstraint(c20);
        assertEquals(100, x.value(), EPSILON);

        ClLinearInequality c10again = new ClLinearInequality(x, CL.LEQ, 10.0);

        solver.addConstraint(c10).addConstraint(c10again);

        assertEquals(10, x.value(), EPSILON);

        solver.removeConstraint(c10);
        assertEquals(10, x.value(), EPSILON);

        solver.removeConstraint(c10again);
        assertEquals(100, x.value(), EPSILON);
    }

    @Test
    public void addDelete2() throws ExCLInternalError, ExCLRequiredFailure, ExCLConstraintNotFound, ExCLNonlinearExpression {
        ClVariable x = new ClVariable("x");
        ClVariable y = new ClVariable("y");
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addConstraint(new ClLinearEquation(x, 100.0, ClStrength.weak)).addConstraint(
                new ClLinearEquation(y, 120.0, ClStrength.strong));

        ClLinearInequality c10 = new ClLinearInequality(x, CL.LEQ, 10.0);
        ClLinearInequality c20 = new ClLinearInequality(x, CL.LEQ, 20.0);

        solver.addConstraint(c10).addConstraint(c20);
        assertEquals(10, x.value(), EPSILON);
        assertEquals(120, y.value(), EPSILON);

        solver.removeConstraint(c10);
        assertEquals(20, x.value(), EPSILON);
        assertEquals(120, y.value(), EPSILON);

        ClLinearEquation cxy = new ClLinearEquation(CL.Times(2.0, x), y);
        solver.addConstraint(cxy);
        assertEquals(20, x.value(), EPSILON);
        assertEquals(40, y.value(), EPSILON);

        solver.removeConstraint(c20);
        assertEquals(60, x.value(), EPSILON);
        assertEquals(120, y.value(), EPSILON);

        solver.removeConstraint(cxy);
        assertEquals(100, x.value(), EPSILON);
        assertEquals(120, y.value(), EPSILON);
    }

    @Test
    public void casso1() throws ExCLInternalError, ExCLRequiredFailure {
        ClVariable x = new ClVariable("x");
        ClVariable y = new ClVariable("y");
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addConstraint(new ClLinearInequality(x, CL.LEQ, y)).addConstraint(new ClLinearEquation(y, CL.Plus(x, 3.0)))
                .addConstraint(new ClLinearEquation(x, 10.0, ClStrength.weak))
                .addConstraint(new ClLinearEquation(y, 10.0, ClStrength.weak));

        if (Math.abs(x.value() - 10.0) < EPSILON) {
            assertEquals(10, x.value(), EPSILON);
            assertEquals(13, y.value(), EPSILON);
        } else {
            assertEquals(7, x.value(), EPSILON);
            assertEquals(10, y.value(), EPSILON);
        }
    }

    @Test(expected = ExCLRequiredFailure.class)
    public void inconsistent1() throws ExCLInternalError, ExCLRequiredFailure {
        ClVariable x = new ClVariable("x");
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addConstraint(new ClLinearEquation(x, 10.0)).addConstraint(new ClLinearEquation(x, 5.0));
    }

    @Test(expected = ExCLRequiredFailure.class)
    public void inconsistent2() throws ExCLInternalError, ExCLRequiredFailure {
        ClVariable x = new ClVariable("x");
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addConstraint(new ClLinearInequality(x, CL.GEQ, 10.0)).addConstraint(new ClLinearInequality(x, CL.LEQ, 5.0));
    }

    @Test(expected = ExCLRequiredFailure.class)
    public void inconsistent3() throws ExCLInternalError, ExCLRequiredFailure {

        ClVariable w = new ClVariable("w");
        ClVariable x = new ClVariable("x");
        ClVariable y = new ClVariable("y");
        ClVariable z = new ClVariable("z");
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addConstraint(new ClLinearInequality(w, CL.GEQ, 10.0)).addConstraint(new ClLinearInequality(x, CL.GEQ, w))
                .addConstraint(new ClLinearInequality(y, CL.GEQ, x)).addConstraint(new ClLinearInequality(z, CL.GEQ, y))
                .addConstraint(new ClLinearInequality(z, CL.GEQ, 8.0)).addConstraint(new ClLinearInequality(z, CL.LEQ, 4.0));
    }

    @Test
    public void multiedit() throws ExCLInternalError, ExCLRequiredFailure, ExCLError {
        ClVariable x = new ClVariable("x");
        ClVariable y = new ClVariable("y");
        ClVariable w = new ClVariable("w");
        ClVariable h = new ClVariable("h");
        ClSimplexSolver solver = new ClSimplexSolver();

        solver.addStay(x).addStay(y).addStay(w).addStay(h);

        solver.addEditVar(x).addEditVar(y).beginEdit();

        solver.suggestValue(x, 10).suggestValue(y, 20).resolve();

        assertEquals(10, x.value(), EPSILON);
        assertEquals(20, y.value(), EPSILON);
        assertEquals(0, w.value(), EPSILON);
        assertEquals(0, h.value(), EPSILON);

        solver.addEditVar(w).addEditVar(h).beginEdit();

        solver.suggestValue(w, 30).suggestValue(h, 40).endEdit();

        assertEquals(10, x.value(), EPSILON);
        assertEquals(20, y.value(), EPSILON);
        assertEquals(30, w.value(), EPSILON);
        assertEquals(40, h.value(), EPSILON);

        solver.suggestValue(x, 50).suggestValue(y, 60).endEdit();

        assertEquals(50, x.value(), EPSILON);
        assertEquals(60, y.value(), EPSILON);
        assertEquals(30, w.value(), EPSILON);
        assertEquals(40, h.value(), EPSILON);
    }

}
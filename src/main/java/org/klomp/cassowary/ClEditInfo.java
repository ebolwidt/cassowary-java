/*
 * Cassowary Incremental Constraint Solver
 * Original Smalltalk Implementation by Alan Borning
 * 
 * Java Implementation by:
 * Greg J. Badros
 * Erwin Bolwidt
 * 
 * (C) 1998, 1999 Greg J. Badros and Alan Borning
 * (C) Copyright 2012 Erwin Bolwidt
 * 
 * See the file LICENSE for legal details regarding this software
 */

package org.klomp.cassowary;

import org.klomp.cassowary.clconstraint.ClConstraint;

/**
 * A privately-used class that just wraps a constraint, its positive and negative error variables, and its prior edit constant. It
 * is used as values in _editVarMap, and replaces the parallel vectors of error variables and previous edit constants from the
 * smalltalk version of the code.
 */
class ClEditInfo {
    public ClEditInfo(ClConstraint cn_, ClAbstractVariable eplus_, ClAbstractVariable eminus_, double prevEditConstant_, int i_) {
        cn = cn_;
        clvEditPlus = eplus_;
        clvEditMinus = eminus_;
        prevEditConstant = prevEditConstant_;
        i = i_;
    }

    public int Index() {
        return i;
    }

    public ClConstraint Constraint() {
        return cn;
    }

    public ClAbstractVariable ClvEditPlus() {
        return clvEditPlus;
    }

    public ClAbstractVariable ClvEditMinus() {
        return clvEditMinus;
    }

    public double PrevEditConstant() {
        return prevEditConstant;
    }

    public void SetPrevEditConstant(double prevEditConstant_) {
        prevEditConstant = prevEditConstant_;
    }

    private ClConstraint cn;
    private ClAbstractVariable clvEditPlus;
    private ClAbstractVariable clvEditMinus;
    private double prevEditConstant;
    private int i;

}

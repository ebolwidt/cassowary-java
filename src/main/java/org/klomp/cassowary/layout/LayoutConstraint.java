package org.klomp.cassowary.layout;

import java.awt.Component;

public class LayoutConstraint {

    private Component left;
    private Attribute leftAttribute;
    private Relation relation;
    private double rightFactor;
    private Component right;
    private Attribute rightAttribute;
    private double rightConstant;

    public LayoutConstraint(Component left, Attribute leftAttribute, Relation relation, double rightFactor, Component right,
            Attribute rightAttribute, double rightConstant) {
        this.left = left;
        this.leftAttribute = leftAttribute;
        this.relation = relation;
        this.rightFactor = rightFactor;
        this.right = right;
        this.rightAttribute = rightAttribute;
        this.rightConstant = rightConstant;
    }

    public LayoutConstraint(Component left, Attribute leftAttribute, Relation relation, double rightConstant) {
        this(left, leftAttribute, relation, 1, null, null, rightConstant);
    }

    public LayoutConstraint(Component left, Attribute leftAttribute, Relation relation, double rightFactor, Component right,
            Attribute rightAttribute) {
        this(left, leftAttribute, relation, rightFactor, right, rightAttribute, 0);
    }

    public LayoutConstraint(Component left, Attribute leftAttribute, Relation relation, Component right, Attribute rightAttribute) {
        this(left, leftAttribute, relation, 1, right, rightAttribute, 0);
    }

    public Component getLeft() {
        return left;
    }

    public Attribute getLeftAttribute() {
        return leftAttribute;
    }

    public Relation getRelation() {
        return relation;
    }

    public double getRightFactor() {
        return rightFactor;
    }

    public Component getRight() {
        return right;
    }

    public Attribute getRightAttribute() {
        return rightAttribute;
    }

    public double getRightConstant() {
        return rightConstant;
    }
}

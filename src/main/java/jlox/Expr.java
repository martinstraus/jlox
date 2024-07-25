package jlox;

import java.util.List;

abstract class Expr {
    static class Binary extends Expr {
        private final Expr left;
        private final Token Operator;
        private final Expr right;
        Binary(Expr left,Token Operator,Expr right) {
            this.left = left;
            this.Operator = Operator;
            this.right = right;
        }
    }
    static class Grouping extends Expr {
        private final Expr expression;
        Grouping(Expr expression) {
            this.expression = expression;
        }
    }
    static class Literal extends Expr {
        private final Object value;
        Literal(Object value) {
            this.value = value;
        }
    }
    static class Unary extends Expr {
        private final Token operator;
        Unary(Token operator) {
            this.operator = operator;
        }
    }
}

package jlox;

import java.util.List;

abstract class Stmt {

    static class Expression extends Stmt {
        final Expr expression;
        Expression(Expr expression) {
            this.expression = expression;
        }
        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    static class Print extends Stmt {
        final Expr expression;
        Print(Expr expression) {
            this.expression = expression;
        }
        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }

    static interface Visitor<T> {

        T visitExpressionStmt(Stmt.Expression expr);
        T visitPrintStmt(Stmt.Print expr);

    }

    abstract <T> T accept(Visitor<T> visitor);

}

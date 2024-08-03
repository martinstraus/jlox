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

    static class Var extends Stmt {
        final Token name;
        final Expr initializer;
        Var(Token name,Expr initializer) {
            this.name = name;
            this.initializer = initializer;
        }
        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    static interface Visitor<T> {

        T visitExpressionStmt(Stmt.Expression stmt);
        T visitPrintStmt(Stmt.Print stmt);
        T visitVarStmt(Stmt.Var stmt);

    }

    abstract <T> T accept(Visitor<T> visitor);

}

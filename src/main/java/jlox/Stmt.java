package jlox;

import java.util.List;

abstract class Stmt {

    static class If extends Stmt {
        final Expr condition;
        final Stmt thenBranch;
        final Stmt elseBranch;
        If(Expr condition,Stmt thenBranch,Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }
        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitIfStmt(this);
        }
    }

    static class Block extends Stmt {
        final List<Stmt> statements;
        Block(List<Stmt> statements) {
            this.statements = statements;
        }
        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitBlockStmt(this);
        }
    }

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

    static class While extends Stmt {
        final Expr condition;
        final Stmt body;
        While(Expr condition,Stmt body) {
            this.condition = condition;
            this.body = body;
        }
        @Override
        <T> T accept(Visitor<T> visitor) {
            return visitor.visitWhileStmt(this);
        }
    }

    static interface Visitor<T> {

        T visitIfStmt(Stmt.If stmt);
        T visitBlockStmt(Stmt.Block stmt);
        T visitExpressionStmt(Stmt.Expression stmt);
        T visitPrintStmt(Stmt.Print stmt);
        T visitVarStmt(Stmt.Var stmt);
        T visitWhileStmt(Stmt.While stmt);

    }

    abstract <T> T accept(Visitor<T> visitor);

}

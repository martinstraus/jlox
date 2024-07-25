package jlox;

public class AstPrinter implements Expr.Visitor<String> {

    String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthezise(expr.operator.lexeme(), expr.left, expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthezise("grouping", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        return expr.value == null 
            ? "nil"
            : expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthezise(expr.operator.lexeme(), expr.expression);
    }

    private String parenthezise(String name, Expr... expressions) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Expr expr : expressions) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }
    
    public static void main(String[] args) {
        System.out.println(
            new AstPrinter().print(
                new Expr.Binary(
                    new Expr.Unary(
                        new Token(TokenType.MINUS,"-", null, 1),
                        new Expr.Literal(123)
                    ),
                    new Token(TokenType.STAR,"*", null, 1),
                    new Expr.Grouping(
                        new Expr.Literal(45.67)
                    )
                )
            )
        );
    }

}

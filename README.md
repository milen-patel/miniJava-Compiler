is it okay that i changed Terminal.java AST

# miniJava-Compiler
A compiler for miniJava

The original miniJava grammar fails to account for operator precedence. The grammar (particularly for the expression rule) had to be stratified to implicitly include operator precedence in the parse structure. The final grammar for the Expression rule is given below.
```
Expression 		-> Precedence0
Precedence0		-> Precedence1 ('||' Precedence1)*
Precedence1		-> Precedence2 ('&&' Precedence2)*
Precedence2		-> Precedence3 (('==' || '!=') Precedence3)*
Precedence3		-> Precedence4 (('<=' || '<' || '>' || '>=')) Precedence4)*
Precedence4		-> Precedence5 (('+' || '-') Precedence5)*
Precedence5		-> Precedence6 (('*' || '/') Precedence6)*
Precedence6		-> (('-' || '!') Precedence6) | Final
Final			-> num | 
			'(' Expression ')' | 
			Reference | 
			Reference[Expression] | 
			Reference(ArguementList?) |
			true | 
			false | 
			new (id() | int[Expression] | id[Expression])
```

The operator precedence is as given (from lowest to highest)
| Class      | Operator |
| ----------- | ----------- |
| Disjunction      | &#124;&#124;       |
| Conjunction   | &&        |
| Equality   | ==,!=        |
| Relational   | <=,<,>,>=  |
| Additive   | +,-        |
| Multiplicative   | \*,/        |
| Unary   | -,!        |


#AST Class Explanations
```
AST
    Terminal
		BooleanLiteral
		IntLiteral
		Identifier
		Operator
	Expression
		Binary Expression
	TypeDenoter
		ArrayType
		BaseType
		ClassType
	Declaration
		ClassDecl
		MemberDecl todo
			FieldDecl
			MethodDecl
.
├── AST (Abstract Class)
|		Top Level Abstract Class
├── ASTDisplay
|		Visitor class for display AST Textual Representation
├── ArrayType extends TypeDenoter (Concrete Class)
|		Maintains a TypeDenoter that represents the type of the Element.
|		Calls the TypeDenoter constructor with the Array TypeKind
├── AssignStmt.java
├── BaseRef.java
├── BaseType extends TypeDenoter (Concrete Class)
|		Concrete wrapper class that just maintains a TypeKind
├── BinaryExpr extends Expression (Concrete Class)
|		Holds an Operator, and Two Expressions (left and right)
├── BlockStmt.java
├── BooleanLiteral extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is a boolean literal
├── CallExpr.java
├── CallStmt.java
├── ClassDecl extends Declaration (Concrete Class)
|		Maintains class name (String), FieldDeclList, and MethodDeclList
├── ClassDeclList implements Iterable<ClassDecl> (Concrete Class)
|		Just maintains a list of ClassDecl's
├── ClassType extends TypeDenoter (Concrete Class)
|		Maintains the class name as an Identifier Objec
|		Class the TypeDenoter constructor with the Class TypeKind
├── Declaration extends AST (Abstract Class)
|		Stores a declaration as a name (String) and type (TypeDenoter)
├── ExprList.java
├── Expression extends AST (Abstract Class)
|		Extends AST but adds nothing new and is still abstract
├── FieldDecl.java
├── FieldDeclList.java
├── IdRef.java
├── Identifier extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is an identifier
├── IfStmt.java
├── IntLiteral extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is an integer literal
├── IxAssignStmt.java
├── IxExpr.java
├── LiteralExpr.java
├── LocalDecl.java
├── MemberDecl.java
├── MethodDecl.java
├── MethodDeclList.java
├── NewArrayExpr.java
├── NewExpr.java
├── NewObjectExpr.java
├── Operator extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is an operator
├── Package extends AST (Concrete Class)
|		Top Level Node in AST, Just has a ClassDeclList
├── ParameterDecl.java
├── ParameterDeclList.java
├── QualRef.java (a dot followed by a particular field according to lecture)
├── RefExpr.java
├── Reference.java
├── ReturnStmt.java
├── Statement.java
├── StatementList.java
├── Terminal extends AST (Abstract Class)
|		Abstract class that just wraps a Token, stores the TokenType and it's spelling, discards the Token
├── ThisRef.java
├── TypeDenoter extends AST (Abstract Class)
|		Abstract wrapper class that contains a TypeKind
├── TypeKind (Enum)
|		Enumeration of the different variable types {void, int, booolean, class, array, unsupported, error}
|			Question: When to use unsupported or error? or even class since ClassDecl doesn't
├── UnaryExpr.java
├── VarDecl.java
├── VarDeclStmt.java
├── Visitor.java
└── WhileStmt.java
```

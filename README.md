added AST visitor and display for NullLiteral
Added NullLiteral class to AST package

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
			null |
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
		CallExpr (Effectively same as CallStmt)
		IxExpr
		LiteralExpr
		UnaryExpr
		RefExpr
		NewExpr
			NewArrayExpr
			NewObjectExpr
	TypeDenoter
		ArrayType
		BaseType
		ClassType
	Declaration
		ClassDecl, ClassDeclList
		MemberDecl todo
			FieldDecl, FieldDeclList
			MethodDecl, MethodDeclList
		LocalDecl
			ParameterDecl, ParameterDeclList
			VarDecl
	Statement, StatementList
		BlockStmt
		IfStmt
		WhileStmt
		ReturnStmt
		AssignStmt
		IxAssignStmt (Indexed AssignStmt)
		CallStmt
		VarDeclStmt
	Reference
		BaseRef
			IdRef
			ThisRef
		QualRef	
.
├── AST (Abstract Class)
|		Top Level Abstract Class
├── ASTDisplay
|		Visitor class for display AST Textual Representation
├── ArrayType extends TypeDenoter (Concrete Class)
|		Maintains a TypeDenoter that represents the type of the Element.
|		Calls the TypeDenoter constructor with the Array TypeKind
├── AssignStmt extends Statement (Concrete Class)
|		Maintains a Reference, Expressino (Reference = Expression)
├── BaseRef extends Reference (Abstract Class)
|		Holds Nothing
├── BaseType extends TypeDenoter (Concrete Class)
|		Concrete wrapper class that just maintains a TypeKind
├── BinaryExpr extends Expression (Concrete Class)
|		Holds an Operator, and Two Expressions (left and right)
├── BlockStmt extends Statement (Concrete Class)
|		Maintains a StatementList
├── BooleanLiteral extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is a boolean literal
├── CallExpr extends Expression (Concrete Class)
|		Maintains a Reference and ExprList (Reference(ArguementList?);)
├── CallStmt extends Statement
|		Maintains a Reference and ExprList (Reference(ArguementList?);)
├── ClassDecl extends Declaration (Concrete Class)
|		Maintains class name (String), FieldDeclList, and MethodDeclList
├── ClassDeclList implements Iterable<ClassDecl> (Concrete Class)
|		Just maintains a list of ClassDecl's
├── ClassType extends TypeDenoter (Concrete Class)
|		Maintains the class name as an Identifier Objec
|		Calls the TypeDenoter constructor with the Class TypeKind. Used in NewObjectExpr.
├── Declaration extends AST (Abstract Class)
|		Stores a declaration as a name (String) and type (TypeDenoter)
├── ExprList implements Iterable<Expression> (Concrete Class)
|		Maintains a list of expressions. Used in function calls.
├── Expression extends AST (Abstract Class)
|		Extends AST but adds nothing new and is still abstract
├── FieldDecl extends MemberDecl (Concrete Class)
|		Just a concrete class that has nothing new from MemberDecl, fields have no more information
├── FieldDeclList implements Iterable<FieldDecl> (Concrete Class)
|		Just a wrapper for having a list of FieldDecl's, used in ClassDecl
├── IdRef extends BaseRef (Concrete Class)
|		Maintains an Identifier
├── Identifier extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is an identifier
├── IfStmt extends Statement (Concrete Class)
|		Maintains an Expression for the condition, Statement for true case, Statement for else case
|		Has two constructors based on presence of else statement, if no else statement then null stored internally
├── IntLiteral extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is an integer literal
├── IxAssignStmt extends Statement (Concrete Class)
|		Maintains Reference, Expression for index, Expression for assign value (Reference[Expression]=Expression)
├── IxExpr extends Expression (Concrete Class)
|		Maintains Reference and Expression (Reference[Expression])
|		Question: Test Ref[Ref[Ref[Expr]]]
├── LiteralExpr extends Expression (Concrete Class)
|		Maintains a Terminal represent a literal
├── LocalDecl extends Declaration (Abstract Class)
|		Abstract filler class, holds no info
├── MemberDecl extends Declaration (Abstract Class)
|		boolean isPrivate, boolean isStatic, TypeDenoter, String name (these are common to both fields and methods)
├── MethodDecl extends MemberDecl
|		Takes in a MemberDecl (which has to be a FieldDecl since that is the only other concrete implementation
|		Also recieves a ParameterList and a StatementList
├── MethodDeclList implements Iterable<MethodDecl> (Concrete Class)
|		Just a wrapper for having a list of MethodDecl's, used in ClassDecl
├── NewArrayExpr extends NewExpr (Concrete Class)
|		Has a TypeDenoter for the element type, Expression for the size of the array
├── NewExpr extends Expression (Abstract Class)
|		Just a top level class for making a new array/object
├── NewObjectExpr extends NewExpr (Concrete Class)
|		Maintains ClassType	
├── Operator extends Terminal (Concrete Class)
|		Concrete class that holds no new information, just signifies that this terminal is an operator
├── Package extends AST (Concrete Class)
|		Top Level Node in AST, Just has a ClassDeclList
├── ParameterDecl extends LocalDecl (Concrete Class)
|		Holds no new info, calls parent constructor with String name and TypeDenoter
├── ParameterDeclList implements Iterable<ParameterDecl> (Concrete Class)
|		Just a wrapper for having a list of ParameterDecl's, used in MethohdDecl
├── QualRef extends Reference (Concrete Class)
|		Mantains a Reference an Identifier
|		A dot followed by a particular field 
|		Question: How to nest these?
├── RefExpr extends Expressin (Concrete Class)
|		Maintains just a Reference
├── Reference extends AST (Abstract Class)
|		Holds Nothing
├── ReturnStmt extends Statement (Concrete Class)
|		Maintains an expression for the return value
|		Question: do we just put null if there is no return value
├── Statement extends AST (Abstract Class)
|		Top Level Class for all the specific statement types, holds no info
├── StatementList implements Iterable<Statement> (Concrete Class)
|		Just a wrapper for having a list of Statements's, used in MethodDecl, BlockStmt
├── Terminal extends AST (Abstract Class)
|		Abstract class that just wraps a Token, stores the TokenType and it's spelling, discards the Token
├── ThisRef extends BaseRef (Concrete Class)
|		Holds nothing, Question: how/when does this get used in this.x.a...
├── TypeDenoter extends AST (Abstract Class)
|		Abstract wrapper class that contains a TypeKind
├── TypeKind (Enum)
|		Enumeration of the different variable types {void, int, booolean, class, array, unsupported, error}
|			Question: When to use unsupported or error? or even class since ClassDecl doesn't
├── UnaryExpr extends Expression (Concrete Class)
|		Mantains Operator and Expression
├── VarDecl extends LocalDecl (Concrete Class)
|		Maintains a TypeDenoter and String name. Used in VarDeclStmt.
├── VarDeclStmt extends Statement (Concrete Class)
|		Maintains a VarDecl, and Expression (VarDecl=Expression;)
├── Visitor
|		Defines the visitor interface for our traversals
└── WhileStmt extends Statement (Concrete Class)
|		Maintains an Expression for the conditioon, and a Statement for the body
```

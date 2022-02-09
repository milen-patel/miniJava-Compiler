# miniJava-Compiler
A compiler for miniJava

The original miniJava grammar fails to account for operator precedence. The grammar (particularly for the expression rule) had to be stratified to implicitly include operator precedence in the parse structure. The final grammar for the Expression rule is given below.
```
Expression 		-> Precedence0
Precedence0 	-> Precedence1 ('||' Precedence1)*
Precedence1		-> Precedence2 ('&&' Precedence2)*
Precedence2		-> Precedence3 (('==' || '!=') Precedence3)*
Precedence3		-> Precedence4 (('<=' || '<' || '>' || '>=')) Precedence4)*
Precedence4		-> Precedence5 (('+' || '-') Precedence5)*
Precedence5		-> Precedence6 (('*' || '/') Precedence6)*
Precedence6		-> (('-' || '!') Precedence6) | Final
Final			-> 	num | 
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
| Disjunction      | `||`       |
| Conjunction   | `&&`        |
| Equality   | `==`,`!=`        |
| Relational   | `<=`,`<`,`>`,`>=`        |
| Additive   | `+`,`-`        |
| Multiplicative   | `*`,`/`        |
| Unary   | `-`,`!`        |

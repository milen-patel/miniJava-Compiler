# miniJava-Compiler
A compiler for miniJava

# Questions
+ I know you cannot have a variable named 'this' but can you have a variable named 'THIS'
+ Check if using scanning approach from (https://www.w3schools.com/java/java_files_read.asp) is okay
+ Make sure I parsed all of the possible TokenType's in the Scanner
+ InputReader always has next, you can read EOF multiple times
+ something like '45fwefpj5' would pass, what should happen?
+ In pullwhitespace, does ' ' count as whitespace since the description seems to imply it doesnt
+ Explore EOF  hhandling in pullNextChar more

+ Make sure i handle spacing in that class{ isnt valid (or is it?) wheras class { is valid
+ Make sure scanner doesn't feed comment tokens to the parser
+ Make sure that CLASS parses as an identifier.
+ Make sure word.ContentEquals is case sensitive
+ Make sure all reserved keywords are being parsed
+ Handle IPE (refactor)
+ Make sure every switch has a break

  0         PUSH         2
  1         LOADL        0
  2         CALL         newarr  
  3         CALL         L11
  4         POP          2
  5         HALT   (0)   
  6  L10:   LOAD         -1[LB]
  7         CALL         putintnl
  8         RETURN (0)   1
  9  L11:   PUSH         1
 10         LOADL        0
 11         STORE        3[LB]
 12         LOAD         3[LB]
 13         LOADL        0
 14         CALL         fieldref
 15         POP          1
 16         CALL         L12
 17         LOAD         1[SB]
 18         LOADL        10
 19         CALLI        L10
 20         RETURN (0)   1
 21  L12:   LOAD         1[SB]
 22         LOADL        5
 23         CALLI        L10
 24         RETURN (0)   0

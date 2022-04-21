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
 10         LOADL        -1
 11         LOADL        1
 12         CALL         newobj  
 13         STORE        3[LB]
 14         LOAD         3[LB]
 15         LOADL        0
 16         LOADL        55
 17         CALL         fieldupd
 18         LOAD         3[LB]
 19         LOAD         3[LB]
 20         CALLI        L13
 21         POP          1
 22         LOADL        -1
 23         LOADL        1
 24         CALL         newobj  
 25         STORE        0[SB]
 26         PUSH         1
 27         LOADL        -1
 28         LOADL        1
 29         CALL         newobj  
 30         STORE        4[LB]
 31         LOAD         4[LB]
 32         LOAD         4[LB]
 33         CALLI        L13
 34         STORE        4[LB]
 35         LOAD         3[LB]
 36         POP          1
 37         LOAD         0[SB]
 38         LOAD         4[LB]
 39         POP          1
 40         LOAD         0[SB]
 41         CALL         eq      
 42         JUMPIF (0)   L12
 43         LOADL        55
 44         LOAD         1[SB]
 45         CALLI        L10
 46  L12:   RETURN (0)   1
 47  L13:   LOAD         -1[LB]
 48         LOADL        0
 49         CALL         fieldref
 50         LOAD         1[SB]
 51         CALLI        L10
 52         LOAD         -1[LB]
 53         POP          1
 54         LOAD         0[SB]
 55         LOAD         0[SB]
 56         CALL         eq      
 57         JUMPIF (0)   L14
 58         LOADL        55
 59         LOAD         1[SB]
 60         CALLI        L10
 61  L14:   LOADA        0[OB]
 62         RETURN (1)   1

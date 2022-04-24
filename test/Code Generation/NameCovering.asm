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
 22         LOAD         3[LB]
 23         POP          1
 24         LOAD         0[SB]
 25         LOADL        -1
 26         LOADL        1
 27         CALL         newobj  
 28         STORE        0[SB]
 29         PUSH         1
 30         LOADL        -1
 31         LOADL        1
 32         CALL         newobj  
 33         STORE        4[LB]
 34         LOAD         4[LB]
 35         LOAD         4[LB]
 36         CALLI        L13
 37         STORE        4[LB]
 38         LOAD         3[LB]
 39         POP          1
 40         LOAD         0[SB]
 41         LOAD         4[LB]
 42         POP          1
 43         LOAD         0[SB]
 44         CALL         eq      
 45         JUMPIF (0)   L12
 46         LOADL        55
 47         LOAD         1[SB]
 48         CALLI        L10
 49  L12:   RETURN (0)   1
 50  L13:   LOAD         -1[LB]
 51         LOADL        0
 52         CALL         fieldref
 53         LOAD         1[SB]
 54         CALLI        L10
 55         LOAD         -1[LB]
 56         POP          1
 57         LOAD         0[SB]
 58         LOAD         0[SB]
 59         CALL         eq      
 60         JUMPIF (0)   L14
 61         LOADL        55
 62         LOAD         1[SB]
 63         CALLI        L10
 64  L14:   LOADA        0[OB]
 65         RETURN (1)   1

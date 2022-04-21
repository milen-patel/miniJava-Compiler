  0         PUSH         1
  1         LOADL        0
  2         CALL         newarr  
  3         CALL         L11
  4         POP          1
  5         HALT   (0)   
  6  L10:   LOAD         -1[LB]
  7         CALL         putintnl
  8         RETURN (0)   1
  9  L11:   PUSH         1
 10         LOADL        -1
 11         LOADL        0
 12         CALL         newobj  
 13         STORE        3[LB]
 14         PUSH         1
 15         LOADL        0
 16         STORE        4[LB]
 17         JUMP         L13
 18  L12:   LOAD         3[LB]
 19         CALLI        L14
 20         POP          1
 21         LOAD         4[LB]
 22         LOADL        1
 23         CALL         add     
 24         STORE        4[LB]
 25  L13:   LOAD         4[LB]
 26         LOADL        50
 27         CALL         lt      
 28         JUMPIF (1)   L12
 29         PUSH         1
 30         LOADL        47
 31         STORE        5[LB]
 32         PUSH         1
 33         LOADL        48
 34         STORE        6[LB]
 35         PUSH         1
 36         LOADL        49
 37         STORE        7[LB]
 38         LOADL        6789
 39         LOAD         0[SB]
 40         CALLI        L10
 41         RETURN (0)   1
 42  L14:   LOADL        5
 43         RETURN (1)   0

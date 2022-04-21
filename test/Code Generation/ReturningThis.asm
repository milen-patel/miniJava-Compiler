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
 10         CALL         L16
 11         STORE        3[LB]
 12         LOAD         3[LB]
 13         LOAD         3[LB]
 14         LOAD         3[LB]
 15         CALLI        L17
 16         CALL         eq      
 17         JUMPIF (0)   L12
 18         LOADL        0
 19         LOAD         0[SB]
 20         CALLI        L10
 21  L12:   LOAD         3[LB]
 22         LOAD         3[LB]
 23         LOAD         3[LB]
 24         LOADL        0
 25         CALL         fieldref
 26         LOAD         3[LB]
 27         CALLI        L18
 28         CALL         eq      
 29         JUMPIF (0)   L13
 30         LOADL        1
 31         LOAD         0[SB]
 32         CALLI        L10
 33  L13:   PUSH         1
 34         LOAD         3[LB]
 35         LOAD         3[LB]
 36         CALLI        L17
 37         STORE        4[LB]
 38         LOAD         3[LB]
 39         LOAD         3[LB]
 40         CALLI        L17
 41         LOAD         4[LB]
 42         LOAD         4[LB]
 43         CALLI        L17
 44         CALL         eq      
 45         LOAD         3[LB]
 46         LOAD         4[LB]
 47         CALL         eq      
 48         CALL         and     
 49         LOAD         3[LB]
 50         LOAD         3[LB]
 51         LOAD         3[LB]
 52         CALLI        L17
 53         CALL         eq      
 54         CALL         and     
 55         JUMPIF (0)   L14
 56         LOADL        2
 57         LOAD         0[SB]
 58         CALLI        L10
 59  L14:   LOAD         3[LB]
 60         LOADL        2
 61         CALL         fieldref
 62         LOAD         4[LB]
 63         LOAD         4[LB]
 64         CALLI        L17
 65         CALL         eq      
 66         JUMPIF (0)   L15
 67         LOADL        3
 68         LOAD         0[SB]
 69         CALLI        L10
 70  L15:   LOADL        4
 71         LOAD         0[SB]
 72         CALLI        L10
 73         RETURN (0)   1
 74  L16:   PUSH         1
 75         LOADL        -1
 76         LOADL        3
 77         CALL         newobj  
 78         STORE        3[LB]
 79         LOAD         3[LB]
 80         LOADL        0
 81         LOADL        10
 82         CALL         fieldupd
 83         LOAD         3[LB]
 84         LOADL        1
 85         LOADL        10
 86         CALL         fieldupd
 87         LOAD         3[LB]
 88         LOADL        2
 89         LOAD         3[LB]
 90         CALL         fieldupd
 91         LOAD         3[LB]
 92         RETURN (1)   0
 93  L17:   LOADA        0[OB]
 94         RETURN (1)   1
 95  L18:   LOAD         -2[LB]
 96         LOADL        0
 97         LOAD         -1[LB]
 98         CALL         fieldupd
 99         LOADA        0[OB]
100         LOADA        0[OB]
101         CALLI        L17
102         RETURN (1)   2

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
 10         LOADL        0
 11         LOADL        0
 12         CALL         sub     
 13         STORE        3[LB]
 14         LOAD         3[LB]
 15         LOAD         0[SB]
 16         CALLI        L10
 17         PUSH         1
 18         LOADL        1
 19         STORE        4[LB]
 20         PUSH         1
 21         LOADL        1
 22         STORE        5[LB]
 23         PUSH         1
 24         LOADL        1
 25         STORE        6[LB]
 26         PUSH         1
 27         LOADL        1
 28         STORE        7[LB]
 29         PUSH         1
 30         LOADL        1
 31         STORE        8[LB]
 32         LOADL        234
 33         LOAD         0[SB]
 34         CALLI        L10
 35         POP          5
 36         PUSH         1
 37         LOADL        2
 38         STORE        4[LB]
 39         PUSH         1
 40         LOADL        3
 41         STORE        5[LB]
 42         PUSH         1
 43         LOADL        4
 44         STORE        6[LB]
 45         PUSH         1
 46         LOADL        5
 47         STORE        7[LB]
 48         PUSH         1
 49         LOADL        6
 50         STORE        8[LB]
 51         LOAD         3[LB]
 52         LOAD         0[SB]
 53         CALLI        L10
 54         LOAD         4[LB]
 55         LOAD         0[SB]
 56         CALLI        L10
 57         LOAD         5[LB]
 58         LOAD         0[SB]
 59         CALLI        L10
 60         LOAD         6[LB]
 61         LOAD         0[SB]
 62         CALLI        L10
 63         LOAD         7[LB]
 64         LOAD         0[SB]
 65         CALLI        L10
 66         LOAD         8[LB]
 67         LOAD         0[SB]
 68         CALLI        L10
 69         LOAD         8[LB]
 70         LOAD         0[SB]
 71         CALLI        L10
 72         POP          1
 73         LOAD         7[LB]
 74         LOAD         0[SB]
 75         CALLI        L10
 76         POP          1
 77         LOAD         6[LB]
 78         LOAD         0[SB]
 79         CALLI        L10
 80         POP          1
 81         LOAD         5[LB]
 82         LOAD         0[SB]
 83         CALLI        L10
 84         POP          1
 85         LOAD         4[LB]
 86         LOAD         0[SB]
 87         CALLI        L10
 88         POP          1
 89         LOAD         3[LB]
 90         LOAD         0[SB]
 91         CALLI        L10
 92         RETURN (0)   1
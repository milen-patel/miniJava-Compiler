  0         PUSH         5
  1         LOADL        0
  2         CALL         newarr  
  3         CALL         L11
  4         POP          5
  5         HALT   (0)   
  6  L10:   LOAD         -1[LB]
  7         CALL         putintnl
  8         RETURN (0)   1
  9  L11:   PUSH         1
 10         LOADL        0
 11         STORE        3[LB]
 12         LOAD         3[LB]
 13         POP          1
 14         LOAD         0[SB]
 15         POP          1
 16         CALL         L12
 17         PUSH         1
 18         LOADL        0
 19         STORE        4[LB]
 20         LOAD         4[LB]
 21         POP          1
 22         LOAD         2[SB]
 23         POP          1
 24         LOAD         2[SB]
 25         POP          1
 26         LOAD         2[SB]
 27         POP          1
 28         LOAD         2[SB]
 29         POP          1
 30         LOAD         2[SB]
 31         POP          1
 32         LOAD         2[SB]
 33         POP          1
 34         LOAD         2[SB]
 35         POP          1
 36         LOAD         2[SB]
 37         POP          1
 38         LOAD         2[SB]
 39         POP          1
 40         LOAD         2[SB]
 41         POP          1
 42         LOAD         2[SB]
 43         POP          1
 44         LOAD         2[SB]
 45         POP          1
 46         LOAD         2[SB]
 47         POP          1
 48         LOAD         3[SB]
 49         LOADL        10
 50         STORE        3[SB]
 51         LOAD         4[LB]
 52         POP          1
 53         LOAD         2[SB]
 54         POP          1
 55         LOAD         2[SB]
 56         POP          1
 57         LOAD         2[SB]
 58         POP          1
 59         LOAD         2[SB]
 60         POP          1
 61         LOAD         2[SB]
 62         POP          1
 63         LOAD         2[SB]
 64         POP          1
 65         LOAD         2[SB]
 66         POP          1
 67         LOAD         2[SB]
 68         POP          1
 69         LOAD         2[SB]
 70         POP          1
 71         LOAD         2[SB]
 72         POP          1
 73         LOAD         2[SB]
 74         POP          1
 75         LOAD         2[SB]
 76         POP          1
 77         LOAD         2[SB]
 78         POP          1
 79         LOAD         3[SB]
 80         LOAD         4[SB]
 81         CALLI        L10
 82         LOADL        15
 83         LOAD         4[SB]
 84         CALLI        L10
 85         RETURN (0)   1
 86  L12:   LOADL        5
 87         LOAD         4[SB]
 88         CALLI        L10
 89         RETURN (0)   0

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
 10         LOADL        -1
 11         LOADL        4
 12         CALL         newobj  
 13         STORE        3[LB]
 14         PUSH         1
 15         LOADL        0
 16         STORE        4[LB]
 17         LOAD         3[LB]
 18         LOADL        0
 19         CALL         fieldref
 20         LOAD         4[SB]
 21         CALLI        L10
 22         PUSH         1
 23         LOADL        0
 24         STORE        5[LB]
 25         LOAD         5[LB]
 26         POP          1
 27         LOAD         1[SB]
 28         LOAD         4[SB]
 29         CALLI        L10
 30         LOAD         4[LB]
 31         POP          1
 32         LOAD         0[SB]
 33         POP          1
 34         LOAD         2[SB]
 35         LOAD         4[SB]
 36         CALLI        L10
 37         LOAD         3[LB]
 38         LOADL        1
 39         LOADL        -1
 40         LOADL        1
 41         CALL         newobj  
 42         CALL         fieldupd
 43         LOAD         3[LB]
 44         LOADL        2
 45         LOADL        -1
 46         LOADL        1
 47         CALL         newobj  
 48         CALL         fieldupd
 49         LOAD         3[LB]
 50         LOADL        3
 51         LOADL        -1
 52         LOADL        1
 53         CALL         newobj  
 54         CALL         fieldupd
 55         LOAD         3[LB]
 56         LOADL        1
 57         CALL         fieldref
 58         LOADL        0
 59         CALL         L13
 60         CALL         fieldupd
 61         LOAD         3[LB]
 62         LOADL        2
 63         CALL         fieldref
 64         LOADL        0
 65         CALL         L13
 66         CALL         fieldupd
 67         LOAD         3[LB]
 68         LOADL        3
 69         CALL         fieldref
 70         LOADL        0
 71         CALL         L13
 72         CALL         fieldupd
 73         LOAD         3[LB]
 74         LOADL        1
 75         CALL         fieldref
 76         LOADL        0
 77         CALL         fieldref
 78         CALLI        L14
 79         LOAD         3[LB]
 80         LOADL        2
 81         CALL         fieldref
 82         LOADL        0
 83         CALL         fieldref
 84         CALLI        L14
 85         CALL         add     
 86         LOAD         3[LB]
 87         LOADL        3
 88         CALL         fieldref
 89         LOADL        0
 90         CALL         fieldref
 91         CALLI        L14
 92         CALL         add     
 93         LOAD         4[SB]
 94         CALLI        L10
 95         LOAD         0[SB]
 96         LOADL        -1
 97         LOADL        1
 98         CALL         newobj  
 99         STORE        0[SB]
100         LOAD         0[SB]
101         LOAD         3[LB]
102         POP          1
103         LOAD         0[SB]
104         CALL         eq      
105         JUMPIF (0)   L12
106         LOADL        4
107         LOAD         4[SB]
108         CALLI        L10
109  L12:   RETURN (0)   1
110  L13:   PUSH         1
111         LOADL        -1
112         LOADL        1
113         CALL         newobj  
114         STORE        3[LB]
115         LOAD         3[LB]
116         LOADL        0
117         LOAD         3[SB]
118         CALL         fieldupd
119         LOAD         3[SB]
120         LOAD         3[SB]
121         LOADL        1
122         CALL         add     
123         STORE        3[SB]
124         LOAD         2[SB]
125         LOAD         2[SB]
126         LOADL        1
127         CALL         add     
128         STORE        2[SB]
129         LOAD         3[LB]
130         RETURN (1)   0
131  L14:   PUSH         1
132         LOADA        0[OB]
133         LOADL        0
134         CALL         fieldref
135         STORE        3[LB]
136         LOAD         3[LB]
137         RETURN (1)   0

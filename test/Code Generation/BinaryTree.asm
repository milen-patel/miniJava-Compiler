  0         PUSH         1
  1         LOADL        0
  2         CALL         newarr  
  3         CALL         L37
  4         POP          1
  5         HALT   (0)   
  6  L10:   LOAD         -1[LB]
  7         CALL         putintnl
  8         RETURN (0)   1
  9  L11:   PUSH         1
 10         LOADL        -1
 11         LOADL        3
 12         CALL         newobj  
 13         STORE        3[LB]
 14         LOAD         3[LB]
 15         LOADL        0
 16         LOAD         -1[LB]
 17         CALL         fieldupd
 18         LOAD         3[LB]
 19         RETURN (1)   1
 20  L12:   LOADA        0[OB]
 21         LOADL        0
 22         LOADA        0[OB]
 23         LOADL        0
 24         CALL         fieldref
 25         LOAD         -1[LB]
 26         LOADA        0[OB]
 27         CALLI        L13
 28         CALL         fieldupd
 29         RETURN (0)   1
 30  L13:   LOAD         -2[LB]
 31         LOADL        0
 32         CALL         eq      
 33         JUMPIF (0)   L14
 34         LOAD         -2[LB]
 35         RETURN (1)   2
 36  L14:   LOAD         -1[LB]
 37         LOAD         -2[LB]
 38         LOADL        0
 39         CALL         fieldref
 40         CALL         lt      
 41         JUMPIF (0)   L15
 42         LOAD         -2[LB]
 43         LOADL        1
 44         LOAD         -2[LB]
 45         LOADL        1
 46         CALL         fieldref
 47         LOAD         -1[LB]
 48         LOADA        0[OB]
 49         CALLI        L13
 50         CALL         fieldupd
 51         JUMP         L19
 52  L15:   LOAD         -1[LB]
 53         LOAD         -2[LB]
 54         LOADL        0
 55         CALL         fieldref
 56         CALL         gt      
 57         JUMPIF (0)   L16
 58         LOAD         -2[LB]
 59         LOADL        2
 60         LOAD         -2[LB]
 61         LOADL        2
 62         CALL         fieldref
 63         LOAD         -1[LB]
 64         LOADA        0[OB]
 65         CALLI        L13
 66         CALL         fieldupd
 67         JUMP         L19
 68  L16:   LOAD         -2[LB]
 69         LOADL        1
 70         CALL         fieldref
 71         LOADL        0
 72         CALL         eq      
 73         JUMPIF (0)   L17
 74         LOAD         -2[LB]
 75         LOADL        2
 76         CALL         fieldref
 77         RETURN (1)   2
 78         JUMP         L18
 79  L17:   LOAD         -2[LB]
 80         LOADL        2
 81         CALL         fieldref
 82         LOADL        0
 83         CALL         eq      
 84         JUMPIF (0)   L18
 85         LOAD         -2[LB]
 86         LOADL        1
 87         CALL         fieldref
 88         RETURN (1)   2
 89  L18:   LOAD         -2[LB]
 90         LOADL        0
 91         LOAD         -2[LB]
 92         LOADL        2
 93         CALL         fieldref
 94         LOADA        0[OB]
 95         CALLI        L20
 96         CALL         fieldupd
 97         LOAD         -2[LB]
 98         LOADL        2
 99         LOAD         -2[LB]
100         LOADL        2
101         CALL         fieldref
102         LOAD         -2[LB]
103         LOADL        0
104         CALL         fieldref
105         LOADA        0[OB]
106         CALLI        L13
107         CALL         fieldupd
108  L19:   LOAD         -2[LB]
109         RETURN (1)   2
110  L20:   PUSH         1
111         LOAD         -1[LB]
112         LOADL        0
113         CALL         fieldref
114         STORE        3[LB]
115         JUMP         L22
116  L21:   LOAD         -1[LB]
117         LOADL        1
118         CALL         fieldref
119         LOADL        0
120         CALL         fieldref
121         STORE        3[LB]
122         LOAD         -1[LB]
123         LOADL        1
124         CALL         fieldref
125         STORE        -1[LB]
126  L22:   LOAD         -1[LB]
127         LOADL        1
128         CALL         fieldref
129         LOADL        0
130         CALL         ne      
131         JUMPIF (1)   L21
132         LOAD         3[LB]
133         RETURN (1)   1
134  L23:   LOADA        0[OB]
135         LOADL        0
136         LOADA        0[OB]
137         LOADL        0
138         CALL         fieldref
139         LOAD         -1[LB]
140         LOADA        0[OB]
141         CALLI        L24
142         CALL         fieldupd
143         RETURN (0)   1
144  L24:   LOAD         -2[LB]
145         LOADL        0
146         CALL         eq      
147         JUMPIF (0)   L25
148         LOAD         -1[LB]
149         CALL         L11
150         STORE        -2[LB]
151         LOAD         -2[LB]
152         RETURN (1)   2
153  L25:   LOAD         -1[LB]
154         LOAD         -2[LB]
155         LOADL        0
156         CALL         fieldref
157         CALL         lt      
158         JUMPIF (0)   L26
159         LOAD         -2[LB]
160         LOADL        1
161         LOAD         -2[LB]
162         LOADL        1
163         CALL         fieldref
164         LOAD         -1[LB]
165         LOADA        0[OB]
166         CALLI        L24
167         CALL         fieldupd
168         JUMP         L27
169  L26:   LOAD         -1[LB]
170         LOAD         -2[LB]
171         LOADL        0
172         CALL         fieldref
173         CALL         gt      
174         JUMPIF (0)   L27
175         LOAD         -2[LB]
176         LOADL        2
177         LOAD         -2[LB]
178         LOADL        2
179         CALL         fieldref
180         LOAD         -1[LB]
181         LOADA        0[OB]
182         CALLI        L24
183         CALL         fieldupd
184  L27:   LOAD         -2[LB]
185         RETURN (1)   2
186  L28:   LOADA        0[OB]
187         LOADL        0
188         CALL         fieldref
189         LOADA        0[OB]
190         CALLI        L29
191         RETURN (0)   0
192  L29:   LOAD         -1[LB]
193         LOADL        0
194         CALL         ne      
195         JUMPIF (0)   L30
196         LOAD         -1[LB]
197         LOADL        1
198         CALL         fieldref
199         LOADA        0[OB]
200         CALLI        L29
201         LOAD         -1[LB]
202         LOADL        0
203         CALL         fieldref
204         LOAD         0[SB]
205         CALLI        L10
206         LOAD         -1[LB]
207         LOADL        2
208         CALL         fieldref
209         LOADA        0[OB]
210         CALLI        L29
211  L30:   RETURN (0)   1
212  L31:   LOADA        0[OB]
213         LOADL        0
214         LOADA        0[OB]
215         LOADL        0
216         CALL         fieldref
217         LOAD         -1[LB]
218         LOADA        0[OB]
219         CALLI        L33
220         CALL         fieldupd
221         LOADA        0[OB]
222         LOADL        0
223         CALL         fieldref
224         LOADL        0
225         CALL         ne      
226         JUMPIF (0)   L32
227         LOADL        1
228         RETURN (1)   1
229  L32:   LOADL        0
230         RETURN (1)   1
231  L33:   LOAD         -2[LB]
232         LOADL        0
233         CALL         eq      
234         JUMPIF (0)   L34
235         LOAD         -2[LB]
236         RETURN (1)   2
237  L34:   LOAD         -2[LB]
238         LOADL        0
239         CALL         fieldref
240         LOAD         -1[LB]
241         CALL         eq      
242         JUMPIF (0)   L35
243         LOAD         -2[LB]
244         RETURN (1)   2
245  L35:   LOAD         -2[LB]
246         LOADL        0
247         CALL         fieldref
248         LOAD         -1[LB]
249         CALL         gt      
250         JUMPIF (0)   L36
251         LOAD         -2[LB]
252         LOADL        1
253         CALL         fieldref
254         LOAD         -1[LB]
255         LOADA        0[OB]
256         CALLI        L33
257         RETURN (1)   2
258  L36:   LOAD         -2[LB]
259         LOADL        2
260         CALL         fieldref
261         LOAD         -1[LB]
262         LOADA        0[OB]
263         CALLI        L33
264         RETURN (1)   2
265  L37:   PUSH         1
266         LOADL        -1
267         LOADL        1
268         CALL         newobj  
269         STORE        3[LB]
270         LOADL        45
271         LOAD         3[LB]
272         CALLI        L23
273         LOADL        10
274         LOAD         3[LB]
275         CALLI        L23
276         LOADL        7
277         LOAD         3[LB]
278         CALLI        L23
279         LOADL        12
280         LOAD         3[LB]
281         CALLI        L23
282         LOADL        90
283         LOAD         3[LB]
284         CALLI        L23
285         LOADL        50
286         LOAD         3[LB]
287         CALLI        L23
288         LOAD         3[LB]
289         CALLI        L28
290         LOADL        12
291         LOAD         3[LB]
292         CALLI        L12
293         LOAD         3[LB]
294         CALLI        L28
295         LOADL        90
296         LOAD         3[LB]
297         CALLI        L12
298         LOAD         3[LB]
299         CALLI        L28
300         LOADL        45
301         LOAD         3[LB]
302         CALLI        L12
303         LOAD         3[LB]
304         CALLI        L28
305         PUSH         1
306         LOADL        50
307         LOAD         3[LB]
308         CALLI        L31
309         STORE        4[LB]
310         LOADL        12
311         LOAD         3[LB]
312         CALLI        L31
313         STORE        4[LB]
314         RETURN (0)   1

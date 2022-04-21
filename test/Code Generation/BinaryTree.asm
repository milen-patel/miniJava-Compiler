  0         PUSH         1
  1         LOADL        0
  2         CALL         newarr  
  3         CALL         L36
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
234         LOAD         -2[LB]
235         LOADL        0
236         CALL         fieldref
237         LOAD         -1[LB]
238         CALL         eq      
239         CALL         or      
240         JUMPIF (0)   L34
241         LOAD         -2[LB]
242         RETURN (1)   2
243  L34:   LOAD         -2[LB]
244         LOADL        0
245         CALL         fieldref
246         LOAD         -1[LB]
247         CALL         gt      
248         JUMPIF (0)   L35
249         LOAD         -2[LB]
250         LOADL        1
251         CALL         fieldref
252         LOAD         -1[LB]
253         LOADA        0[OB]
254         CALLI        L33
255         RETURN (1)   2
256  L35:   LOAD         -2[LB]
257         LOADL        2
258         CALL         fieldref
259         LOAD         -1[LB]
260         LOADA        0[OB]
261         CALLI        L33
262         RETURN (1)   2
263  L36:   PUSH         1
264         LOADL        -1
265         LOADL        1
266         CALL         newobj  
267         STORE        3[LB]
268         LOADL        45
269         LOAD         3[LB]
270         CALLI        L23
271         LOADL        10
272         LOAD         3[LB]
273         CALLI        L23
274         LOADL        7
275         LOAD         3[LB]
276         CALLI        L23
277         LOADL        12
278         LOAD         3[LB]
279         CALLI        L23
280         LOADL        90
281         LOAD         3[LB]
282         CALLI        L23
283         LOADL        50
284         LOAD         3[LB]
285         CALLI        L23
286         LOAD         3[LB]
287         CALLI        L28
288         LOADL        12
289         LOAD         3[LB]
290         CALLI        L12
291         LOAD         3[LB]
292         CALLI        L28
293         LOADL        90
294         LOAD         3[LB]
295         CALLI        L12
296         LOAD         3[LB]
297         CALLI        L28
298         LOADL        45
299         LOAD         3[LB]
300         CALLI        L12
301         LOAD         3[LB]
302         CALLI        L28
303         PUSH         1
304         LOADL        50
305         LOAD         3[LB]
306         CALLI        L31
307         STORE        4[LB]
308         LOADL        12
309         LOAD         3[LB]
310         CALLI        L31
311         STORE        4[LB]
312         RETURN (0)   1

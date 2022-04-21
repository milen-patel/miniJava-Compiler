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
 11         LOADL        2
 12         CALL         newobj  
 13         STORE        3[LB]
 14         LOADL        10
 15         LOAD         3[LB]
 16         CALLI        L12
 17         LOAD         0[SB]
 18         CALLI        L10
 19         RETURN (0)   1
 20  L12:   PUSH         1
 21         LOADL        0
 22         STORE        3[LB]
 23         LOAD         -1[LB]
 24         LOADA        0[OB]
 25         CALLI        L28
 26         STORE        3[LB]
 27         LOADA        0[OB]
 28         CALLI        L25
 29         STORE        3[LB]
 30         LOADL        9999
 31         LOAD         0[SB]
 32         CALLI        L10
 33         LOADA        0[OB]
 34         LOADL        1
 35         CALL         fieldref
 36         LOADL        1
 37         CALL         sub     
 38         STORE        3[LB]
 39         LOADL        0
 40         LOAD         3[LB]
 41         LOADA        0[OB]
 42         CALLI        L13
 43         STORE        3[LB]
 44         LOADA        0[OB]
 45         CALLI        L25
 46         STORE        3[LB]
 47         LOADL        0
 48         RETURN (1)   1
 49  L13:   PUSH         1
 50         LOADL        0
 51         STORE        3[LB]
 52         PUSH         1
 53         LOADL        0
 54         STORE        4[LB]
 55         PUSH         1
 56         LOADL        0
 57         STORE        5[LB]
 58         PUSH         1
 59         LOADL        0
 60         STORE        6[LB]
 61         PUSH         1
 62         LOADL        0
 63         STORE        7[LB]
 64         PUSH         1
 65         LOADL        0
 66         STORE        8[LB]
 67         PUSH         1
 68         LOADL        0
 69         STORE        9[LB]
 70         PUSH         1
 71         LOADL        0
 72         STORE        10[LB]
 73         LOADL        0
 74         STORE        7[LB]
 75         LOAD         -2[LB]
 76         LOAD         -1[LB]
 77         CALL         lt      
 78         JUMPIF (0)   L23
 79         LOADA        0[OB]
 80         LOADL        0
 81         CALL         fieldref
 82         LOAD         -1[LB]
 83         CALL         arrayref
 84         STORE        3[LB]
 85         LOAD         -2[LB]
 86         LOADL        1
 87         CALL         sub     
 88         STORE        4[LB]
 89         LOAD         -1[LB]
 90         STORE        5[LB]
 91         LOADL        1
 92         STORE        8[LB]
 93         JUMP         L22
 94  L14:   LOADL        1
 95         STORE        9[LB]
 96         JUMP         L17
 97  L15:   LOAD         4[LB]
 98         LOADL        1
 99         CALL         add     
100         STORE        4[LB]
101         LOADA        0[OB]
102         LOADL        0
103         CALL         fieldref
104         LOAD         4[LB]
105         CALL         arrayref
106         STORE        10[LB]
107         LOAD         10[LB]
108         LOAD         3[LB]
109         CALL         lt      
110         CALL         not     
111         JUMPIF (0)   L16
112         LOADL        0
113         STORE        9[LB]
114         JUMP         L17
115  L16:   LOADL        1
116         STORE        9[LB]
117  L17:   LOAD         9[LB]
118         JUMPIF (1)   L15
119         LOADL        1
120         STORE        9[LB]
121         JUMP         L20
122  L18:   LOAD         5[LB]
123         LOADL        1
124         CALL         sub     
125         STORE        5[LB]
126         LOADA        0[OB]
127         LOADL        0
128         CALL         fieldref
129         LOAD         5[LB]
130         CALL         arrayref
131         STORE        10[LB]
132         LOAD         3[LB]
133         LOAD         10[LB]
134         CALL         lt      
135         CALL         not     
136         JUMPIF (0)   L19
137         LOADL        0
138         STORE        9[LB]
139         JUMP         L20
140  L19:   LOADL        1
141         STORE        9[LB]
142  L20:   LOAD         9[LB]
143         JUMPIF (1)   L18
144         LOADA        0[OB]
145         LOADL        0
146         CALL         fieldref
147         LOAD         4[LB]
148         CALL         arrayref
149         STORE        7[LB]
150         LOADA        0[OB]
151         LOADL        0
152         CALL         fieldref
153         LOAD         4[LB]
154         LOADA        0[OB]
155         LOADL        0
156         CALL         fieldref
157         LOAD         5[LB]
158         CALL         arrayref
159         CALL         arrayupd
160         LOADA        0[OB]
161         LOADL        0
162         CALL         fieldref
163         LOAD         5[LB]
164         LOAD         7[LB]
165         CALL         arrayupd
166         LOAD         5[LB]
167         LOAD         4[LB]
168         LOADL        1
169         CALL         add     
170         CALL         lt      
171         JUMPIF (0)   L21
172         LOADL        0
173         STORE        8[LB]
174         JUMP         L22
175  L21:   LOADL        1
176         STORE        8[LB]
177  L22:   LOAD         8[LB]
178         JUMPIF (1)   L14
179         LOADA        0[OB]
180         LOADL        0
181         CALL         fieldref
182         LOAD         5[LB]
183         LOADA        0[OB]
184         LOADL        0
185         CALL         fieldref
186         LOAD         4[LB]
187         CALL         arrayref
188         CALL         arrayupd
189         LOADA        0[OB]
190         LOADL        0
191         CALL         fieldref
192         LOAD         4[LB]
193         LOADA        0[OB]
194         LOADL        0
195         CALL         fieldref
196         LOAD         -1[LB]
197         CALL         arrayref
198         CALL         arrayupd
199         LOADA        0[OB]
200         LOADL        0
201         CALL         fieldref
202         LOAD         -1[LB]
203         LOAD         7[LB]
204         CALL         arrayupd
205         LOAD         -2[LB]
206         LOAD         4[LB]
207         LOADL        1
208         CALL         sub     
209         LOADA        0[OB]
210         CALLI        L13
211         STORE        6[LB]
212         LOAD         4[LB]
213         LOADL        1
214         CALL         add     
215         LOAD         -1[LB]
216         LOADA        0[OB]
217         CALLI        L13
218         STORE        6[LB]
219         JUMP         L24
220  L23:   LOADL        0
221         STORE        6[LB]
222  L24:   LOADL        0
223         RETURN (1)   2
224  L25:   PUSH         1
225         LOADL        0
226         STORE        3[LB]
227         LOADL        0
228         STORE        3[LB]
229         JUMP         L27
230  L26:   LOADA        0[OB]
231         LOADL        0
232         CALL         fieldref
233         LOAD         3[LB]
234         CALL         arrayref
235         LOAD         0[SB]
236         CALLI        L10
237         LOAD         3[LB]
238         LOADL        1
239         CALL         add     
240         STORE        3[LB]
241  L27:   LOAD         3[LB]
242         LOADA        0[OB]
243         LOADL        1
244         CALL         fieldref
245         CALL         lt      
246         JUMPIF (1)   L26
247         LOADL        0
248         RETURN (1)   0
249  L28:   LOADA        0[OB]
250         LOADL        1
251         LOAD         -1[LB]
252         CALL         fieldupd
253         LOADA        0[OB]
254         LOADL        0
255         LOAD         -1[LB]
256         CALL         newarr  
257         CALL         fieldupd
258         LOADA        0[OB]
259         LOADL        0
260         CALL         fieldref
261         LOADL        0
262         LOADL        20
263         CALL         arrayupd
264         LOADA        0[OB]
265         LOADL        0
266         CALL         fieldref
267         LOADL        1
268         LOADL        7
269         CALL         arrayupd
270         LOADA        0[OB]
271         LOADL        0
272         CALL         fieldref
273         LOADL        2
274         LOADL        12
275         CALL         arrayupd
276         LOADA        0[OB]
277         LOADL        0
278         CALL         fieldref
279         LOADL        3
280         LOADL        18
281         CALL         arrayupd
282         LOADA        0[OB]
283         LOADL        0
284         CALL         fieldref
285         LOADL        4
286         LOADL        2
287         CALL         arrayupd
288         LOADA        0[OB]
289         LOADL        0
290         CALL         fieldref
291         LOADL        5
292         LOADL        11
293         CALL         arrayupd
294         LOADA        0[OB]
295         LOADL        0
296         CALL         fieldref
297         LOADL        6
298         LOADL        6
299         CALL         arrayupd
300         LOADA        0[OB]
301         LOADL        0
302         CALL         fieldref
303         LOADL        7
304         LOADL        9
305         CALL         arrayupd
306         LOADA        0[OB]
307         LOADL        0
308         CALL         fieldref
309         LOADL        8
310         LOADL        19
311         CALL         arrayupd
312         LOADA        0[OB]
313         LOADL        0
314         CALL         fieldref
315         LOADL        9
316         LOADL        5
317         CALL         arrayupd
318         LOADL        0
319         RETURN (1)   1

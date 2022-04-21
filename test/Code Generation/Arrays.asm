  0         PUSH         3
  1         LOADL        0
  2         CALL         newarr  
  3         CALL         L11
  4         POP          3
  5         HALT   (0)   
  6  L10:   LOAD         -1[LB]
  7         CALL         putintnl
  8         RETURN (0)   1
  9  L11:   PUSH         1
 10         LOADL        40
 11         CALL         newarr  
 12         STORE        3[LB]
 13         LOAD         3[LB]
 14         LOADL        4
 15         CALL         arrayref
 16         LOAD         2[SB]
 17         CALLI        L10
 18         PUSH         1
 19         LOADL        -1
 20         LOADL        2
 21         CALL         newobj  
 22         STORE        4[LB]
 23         LOAD         4[LB]
 24         CALLI        L14
 25         LOADL        25
 26         LOAD         4[LB]
 27         CALLI        L17
 28         LOAD         2[SB]
 29         CALLI        L10
 30         PUSH         1
 31         LOADL        69
 32         CALL         L30
 33         STORE        5[LB]
 34         PUSH         1
 35         LOAD         5[LB]
 36         CALL         arraylen
 37         LOADL        1
 38         CALL         sub     
 39         STORE        6[LB]
 40         JUMP         L13
 41  L12:   LOAD         5[LB]
 42         LOAD         6[LB]
 43         CALL         arrayref
 44         LOAD         2[SB]
 45         CALLI        L10
 46         LOAD         6[LB]
 47         LOADL        1
 48         CALL         sub     
 49         STORE        6[LB]
 50  L13:   LOAD         6[LB]
 51         LOADL        0
 52         CALL         ge      
 53         JUMPIF (1)   L12
 54         LOAD         4[LB]
 55         CALLI        L18
 56         LOAD         4[LB]
 57         CALLI        L26
 58         LOAD         4[LB]
 59         CALLI        L27
 60         RETURN (0)   1
 61  L14:   LOADA        0[OB]
 62         LOADL        0
 63         LOADL        100
 64         CALL         newarr  
 65         CALL         fieldupd
 66         PUSH         1
 67         LOADL        0
 68         STORE        3[LB]
 69         JUMP         L16
 70  L15:   LOADA        0[OB]
 71         LOADL        0
 72         CALL         fieldref
 73         LOAD         3[LB]
 74         LOAD         3[LB]
 75         CALL         arrayupd
 76         LOAD         3[LB]
 77         LOADL        1
 78         CALL         add     
 79         STORE        3[LB]
 80  L16:   LOAD         3[LB]
 81         LOADA        0[OB]
 82         LOADL        0
 83         CALL         fieldref
 84         CALL         arraylen
 85         LOADL        2
 86         CALL         div     
 87         CALL         lt      
 88         JUMPIF (1)   L15
 89         RETURN (0)   0
 90  L17:   LOADA        0[OB]
 91         LOADL        0
 92         CALL         fieldref
 93         LOAD         -1[LB]
 94         CALL         arrayref
 95         RETURN (1)   1
 96  L18:   PUSH         1
 97         LOADL        5
 98         STORE        3[LB]
 99         PUSH         1
100         LOAD         3[LB]
101         LOADA        0[OB]
102         CALLI        L24
103         STORE        4[LB]
104         LOAD         4[LB]
105         LOADL        0
106         LOADL        -1
107         LOADL        1
108         CALL         newobj  
109         LOADA        0[OB]
110         CALLI        L25
111         LOAD         4[LB]
112         LOADL        1
113         LOADL        -1
114         LOADL        1
115         CALL         newobj  
116         LOADA        0[OB]
117         CALLI        L25
118         LOAD         4[LB]
119         LOADL        2
120         LOAD         4[LB]
121         LOADL        0
122         CALL         L38
123         LOADA        0[OB]
124         CALLI        L25
125         LOAD         4[LB]
126         LOADL        3
127         LOAD         4[LB]
128         LOADL        2
129         CALL         arrayref
130         LOADA        0[OB]
131         CALLI        L25
132         LOAD         4[LB]
133         LOADL        4
134         LOAD         4[LB]
135         LOADL        1
136         CALL         arrayref
137         LOADA        0[OB]
138         CALLI        L25
139         PUSH         1
140         LOAD         4[LB]
141         LOADL        0
142         CALL         arrayref
143         STORE        5[LB]
144         LOADL        400
145         LOAD         5[LB]
146         CALLI        L29
147         LOAD         4[LB]
148         CALL         L33
149         PUSH         1
150         LOAD         4[LB]
151         LOADL        1
152         CALL         L38
153         STORE        6[LB]
154         LOAD         6[LB]
155         LOAD         4[LB]
156         LOADL        1
157         CALL         arrayref
158         CALL         ne      
159         JUMPIF (0)   L19
160         PUSH         1
161         LOADL        0
162         LOADL        345
163         CALL         sub     
164         CALL         newarr  
165         STORE        7[LB]
166         POP          1
167  L19:   LOAD         4[LB]
168         LOADL        1
169         CALL         arrayref
170         LOAD         4[LB]
171         LOADL        4
172         CALL         arrayref
173         CALL         ne      
174         JUMPIF (0)   L20
175         PUSH         1
176         LOADL        0
177         LOADL        345
178         CALL         sub     
179         CALL         newarr  
180         STORE        7[LB]
181         POP          1
182  L20:   LOADL        200
183         LOAD         6[LB]
184         CALLI        L29
185         LOAD         4[LB]
186         CALL         L33
187         LOADA        0[OB]
188         LOADL        1
189         LOAD         4[LB]
190         CALL         fieldupd
191         LOADA        0[OB]
192         LOADL        1
193         LOAD         4[LB]
194         CALL         fieldupd
195         LOADA        0[OB]
196         CALLI        L21
197         LOADA        0[OB]
198         LOADL        1
199         CALL         fieldref
200         CALL         L33
201         RETURN (0)   0
202  L21:   PUSH         1
203         LOADL        0
204         STORE        3[LB]
205         JUMP         L23
206  L22:   PUSH         1
207         LOADA        0[OB]
208         LOADL        1
209         CALL         fieldref
210         LOAD         3[LB]
211         CALL         arrayref
212         STORE        4[LB]
213         LOADL        0
214         LOAD         4[LB]
215         CALLI        L29
216         LOAD         3[LB]
217         LOADL        1
218         CALL         add     
219         STORE        3[LB]
220         POP          1
221  L23:   LOAD         3[LB]
222         LOADA        0[OB]
223         LOADL        1
224         CALL         fieldref
225         CALL         arraylen
226         CALL         lt      
227         JUMPIF (1)   L22
228         RETURN (0)   0
229  L24:   LOAD         -1[LB]
230         CALL         newarr  
231         RETURN (1)   1
232  L25:   LOAD         -3[LB]
233         LOAD         -2[LB]
234         LOAD         -1[LB]
235         CALL         arrayupd
236         RETURN (0)   3
237  L26:   LOADL        50
238         CALL         newarr  
239         STORE        1[SB]
240         PUSH         1
241         LOADL        0
242         STORE        3[LB]
243         PUSH         1
244         LOAD         3[LB]
245         POP          1
246         LOAD         0[SB]
247         POP          1
248         LOAD         1[SB]
249         STORE        4[LB]
250         LOAD         4[LB]
251         LOADL        2
252         LOADL        420
253         CALL         arrayupd
254         LOAD         4[LB]
255         LOADL        2
256         CALL         arrayref
257         LOAD         2[SB]
258         CALLI        L10
259         LOAD         1[SB]
260         LOADL        2
261         CALL         arrayref
262         LOAD         2[SB]
263         CALLI        L10
264         LOAD         4[LB]
265         CALL         arraylen
266         LOAD         2[SB]
267         CALLI        L10
268         LOAD         1[SB]
269         CALL         arraylen
270         LOAD         2[SB]
271         CALLI        L10
272         RETURN (0)   0
273  L27:   PUSH         1
274         LOADL        10
275         CALL         newarr  
276         STORE        3[LB]
277         PUSH         1
278         LOADL        5
279         CALL         newarr  
280         STORE        4[LB]
281         LOAD         3[LB]
282         LOADL        0
283         LOADL        1
284         CALL         arrayupd
285         LOAD         4[LB]
286         LOADL        0
287         LOADL        2
288         CALL         arrayupd
289         LOAD         3[LB]
290         LOADL        1
291         LOADL        2
292         CALL         arrayupd
293         LOAD         4[LB]
294         LOADL        1
295         LOADL        4
296         CALL         arrayupd
297         LOAD         3[LB]
298         LOADL        2
299         LOADL        3
300         CALL         arrayupd
301         LOAD         4[LB]
302         LOADL        2
303         LOADL        6
304         CALL         arrayupd
305         LOAD         3[LB]
306         LOADL        3
307         LOADL        4
308         CALL         arrayupd
309         LOAD         4[LB]
310         LOADL        3
311         LOADL        8
312         CALL         arrayupd
313         LOAD         3[LB]
314         LOADL        4
315         LOADL        5
316         CALL         arrayupd
317         LOAD         4[LB]
318         LOADL        4
319         LOADL        10
320         CALL         arrayupd
321         LOAD         3[LB]
322         LOADL        5
323         LOADL        6
324         CALL         arrayupd
325         LOAD         3[LB]
326         LOADL        6
327         LOADL        7
328         CALL         arrayupd
329         LOAD         3[LB]
330         LOADL        7
331         LOADL        8
332         CALL         arrayupd
333         LOAD         3[LB]
334         LOADL        8
335         LOADL        9
336         CALL         arrayupd
337         LOAD         3[LB]
338         LOADL        9
339         LOADL        10
340         CALL         arrayupd
341         PUSH         1
342         LOADL        15
343         CALL         newarr  
344         STORE        5[LB]
345         LOAD         5[LB]
346         LOADL        0
347         LOADL        1
348         CALL         arrayupd
349         LOAD         5[LB]
350         LOADL        1
351         LOADL        2
352         CALL         arrayupd
353         LOAD         5[LB]
354         LOADL        2
355         LOADL        2
356         CALL         arrayupd
357         LOAD         5[LB]
358         LOADL        3
359         LOADL        3
360         CALL         arrayupd
361         LOAD         5[LB]
362         LOADL        4
363         LOADL        4
364         CALL         arrayupd
365         LOAD         5[LB]
366         LOADL        5
367         LOADL        4
368         CALL         arrayupd
369         LOAD         5[LB]
370         LOADL        6
371         LOADL        5
372         CALL         arrayupd
373         LOAD         5[LB]
374         LOADL        7
375         LOADL        6
376         CALL         arrayupd
377         LOAD         5[LB]
378         LOADL        8
379         LOADL        6
380         CALL         arrayupd
381         LOAD         5[LB]
382         LOADL        9
383         LOADL        7
384         CALL         arrayupd
385         LOAD         5[LB]
386         LOADL        10
387         LOADL        8
388         CALL         arrayupd
389         LOAD         5[LB]
390         LOADL        11
391         LOADL        8
392         CALL         arrayupd
393         LOAD         5[LB]
394         LOADL        12
395         LOADL        9
396         CALL         arrayupd
397         LOAD         5[LB]
398         LOADL        13
399         LOADL        10
400         CALL         arrayupd
401         LOAD         5[LB]
402         LOADL        14
403         LOADL        10
404         CALL         arrayupd
405         LOAD         5[LB]
406         LOAD         3[LB]
407         LOAD         4[LB]
408         CALL         L45
409         CALL         L39
410         LOAD         5[LB]
411         CALL         arraylen
412         LOAD         2[SB]
413         CALLI        L10
414         RETURN (0)   0
415  L28:   LOADA        0[OB]
416         LOADL        0
417         CALL         fieldref
418         RETURN (1)   0
419  L29:   LOADA        0[OB]
420         LOADL        0
421         LOAD         -1[LB]
422         CALL         fieldupd
423         RETURN (0)   1
424  L30:   PUSH         1
425         LOAD         -1[LB]
426         LOADL        1
427         CALL         add     
428         CALL         newarr  
429         STORE        3[LB]
430         JUMP         L32
431  L31:   LOAD         3[LB]
432         LOAD         -1[LB]
433         LOAD         -1[LB]
434         CALL         arrayupd
435         LOAD         -1[LB]
436         LOADL        1
437         CALL         sub     
438         STORE        -1[LB]
439  L32:   LOAD         -1[LB]
440         LOADL        0
441         CALL         ge      
442         JUMPIF (1)   L31
443         LOAD         3[LB]
444         RETURN (1)   1
445  L33:   PUSH         1
446         LOADL        0
447         STORE        3[LB]
448         JUMP         L35
449  L34:   PUSH         1
450         LOAD         -1[LB]
451         LOAD         3[LB]
452         CALL         arrayref
453         STORE        4[LB]
454         LOAD         4[LB]
455         CALLI        L28
456         LOAD         2[SB]
457         CALLI        L10
458         LOAD         3[LB]
459         LOADL        1
460         CALL         add     
461         STORE        3[LB]
462         POP          1
463  L35:   LOAD         3[LB]
464         LOAD         -1[LB]
465         CALL         arraylen
466         CALL         lt      
467         JUMPIF (1)   L34
468         RETURN (0)   1
469         PUSH         1
470         LOADL        0
471         STORE        3[LB]
472         JUMP         L37
473  L36:   LOAD         -1[LB]
474         LOAD         3[LB]
475         CALL         arrayref
476         LOAD         2[SB]
477         CALLI        L10
478         LOAD         3[LB]
479         LOADL        1
480         CALL         add     
481         STORE        3[LB]
482  L37:   LOAD         3[LB]
483         LOAD         -1[LB]
484         CALL         arraylen
485         CALL         lt      
486         JUMPIF (1)   L36
487         RETURN (0)   1
488  L38:   LOAD         -2[LB]
489         LOAD         -1[LB]
490         CALL         arrayref
491         RETURN (1)   2
492  L39:   LOAD         -2[LB]
493         CALL         arraylen
494         LOAD         -1[LB]
495         CALL         arraylen
496         CALL         ne      
497         JUMPIF (0)   L40
498         CALL         L44
499  L40:   PUSH         1
500         LOADL        0
501         STORE        3[LB]
502         JUMP         L43
503  L41:   LOAD         -2[LB]
504         LOAD         3[LB]
505         CALL         arrayref
506         LOAD         -1[LB]
507         LOAD         3[LB]
508         CALL         arrayref
509         CALL         ne      
510         JUMPIF (0)   L42
511         CALL         L44
512  L42:   LOAD         3[LB]
513         LOADL        1
514         CALL         add     
515         STORE        3[LB]
516  L43:   LOAD         3[LB]
517         LOAD         -2[LB]
518         CALL         arraylen
519         CALL         lt      
520         JUMPIF (1)   L41
521         RETURN (0)   2
522  L44:   PUSH         1
523         LOADL        0
524         LOADL        55555555
525         CALL         sub     
526         CALL         newarr  
527         STORE        3[LB]
528         RETURN (0)   0
529  L45:   PUSH         1
530         LOAD         -2[LB]
531         CALL         arraylen
532         LOAD         -1[LB]
533         CALL         arraylen
534         CALL         add     
535         CALL         newarr  
536         STORE        3[LB]
537         PUSH         1
538         LOADL        0
539         STORE        4[LB]
540         PUSH         1
541         LOADL        0
542         STORE        5[LB]
543         PUSH         1
544         LOADL        0
545         STORE        6[LB]
546         JUMP         L49
547  L46:   LOAD         -2[LB]
548         LOAD         4[LB]
549         CALL         arrayref
550         LOAD         -1[LB]
551         LOAD         5[LB]
552         CALL         arrayref
553         CALL         le      
554         JUMPIF (0)   L47
555         LOAD         3[LB]
556         LOAD         6[LB]
557         LOAD         -2[LB]
558         LOAD         4[LB]
559         CALL         arrayref
560         CALL         arrayupd
561         LOAD         4[LB]
562         LOADL        1
563         CALL         add     
564         STORE        4[LB]
565         JUMP         L48
566  L47:   LOAD         3[LB]
567         LOAD         6[LB]
568         LOAD         -1[LB]
569         LOAD         5[LB]
570         CALL         arrayref
571         CALL         arrayupd
572         LOAD         5[LB]
573         LOADL        1
574         CALL         add     
575         STORE        5[LB]
576  L48:   LOAD         6[LB]
577         LOADL        1
578         CALL         add     
579         STORE        6[LB]
580  L49:   LOAD         4[LB]
581         LOAD         -2[LB]
582         CALL         arraylen
583         CALL         lt      
584         LOAD         5[LB]
585         LOAD         -1[LB]
586         CALL         arraylen
587         CALL         lt      
588         CALL         and     
589         JUMPIF (1)   L46
590         LOAD         4[LB]
591         LOAD         -2[LB]
592         CALL         arraylen
593         CALL         lt      
594         JUMPIF (0)   L52
595         JUMP         L51
596  L50:   LOAD         3[LB]
597         LOAD         6[LB]
598         LOAD         -2[LB]
599         LOAD         4[LB]
600         CALL         arrayref
601         CALL         arrayupd
602         LOAD         6[LB]
603         LOADL        1
604         CALL         add     
605         STORE        6[LB]
606         LOAD         4[LB]
607         LOADL        1
608         CALL         add     
609         STORE        4[LB]
610  L51:   LOAD         4[LB]
611         LOAD         -2[LB]
612         CALL         arraylen
613         CALL         lt      
614         JUMPIF (1)   L50
615         JUMP         L55
616  L52:   LOAD         5[LB]
617         LOAD         -1[LB]
618         CALL         arraylen
619         CALL         lt      
620         JUMPIF (0)   L55
621         JUMP         L54
622  L53:   LOAD         3[LB]
623         LOAD         6[LB]
624         LOAD         -1[LB]
625         LOAD         5[LB]
626         CALL         arrayref
627         CALL         arrayupd
628         LOAD         6[LB]
629         LOADL        1
630         CALL         add     
631         STORE        6[LB]
632         LOAD         5[LB]
633         LOADL        1
634         CALL         add     
635         STORE        5[LB]
636  L54:   LOAD         5[LB]
637         LOAD         -1[LB]
638         CALL         arraylen
639         CALL         lt      
640         JUMPIF (1)   L53
641  L55:   LOAD         3[LB]
642         RETURN (1)   2

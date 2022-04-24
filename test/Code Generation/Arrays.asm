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
161         LOADL        345
162         CALL         neg     
163         CALL         newarr  
164         STORE        7[LB]
165         POP          1
166  L19:   LOAD         4[LB]
167         LOADL        1
168         CALL         arrayref
169         LOAD         4[LB]
170         LOADL        4
171         CALL         arrayref
172         CALL         ne      
173         JUMPIF (0)   L20
174         PUSH         1
175         LOADL        345
176         CALL         neg     
177         CALL         newarr  
178         STORE        7[LB]
179         POP          1
180  L20:   LOADL        200
181         LOAD         6[LB]
182         CALLI        L29
183         LOAD         4[LB]
184         CALL         L33
185         LOADA        0[OB]
186         LOADL        1
187         LOAD         4[LB]
188         CALL         fieldupd
189         LOADA        0[OB]
190         LOADL        1
191         LOAD         4[LB]
192         CALL         fieldupd
193         LOADA        0[OB]
194         CALLI        L21
195         LOADA        0[OB]
196         LOADL        1
197         CALL         fieldref
198         CALL         L33
199         RETURN (0)   0
200  L21:   PUSH         1
201         LOADL        0
202         STORE        3[LB]
203         JUMP         L23
204  L22:   PUSH         1
205         LOADA        0[OB]
206         LOADL        1
207         CALL         fieldref
208         LOAD         3[LB]
209         CALL         arrayref
210         STORE        4[LB]
211         LOADL        0
212         LOAD         4[LB]
213         CALLI        L29
214         LOAD         3[LB]
215         LOADL        1
216         CALL         add     
217         STORE        3[LB]
218         POP          1
219  L23:   LOAD         3[LB]
220         LOADA        0[OB]
221         LOADL        1
222         CALL         fieldref
223         CALL         arraylen
224         CALL         lt      
225         JUMPIF (1)   L22
226         RETURN (0)   0
227  L24:   LOAD         -1[LB]
228         CALL         newarr  
229         RETURN (1)   1
230  L25:   LOAD         -3[LB]
231         LOAD         -2[LB]
232         LOAD         -1[LB]
233         CALL         arrayupd
234         RETURN (0)   3
235  L26:   LOAD         1[SB]
236         LOADL        50
237         CALL         newarr  
238         STORE        1[SB]
239         PUSH         1
240         LOADL        0
241         STORE        3[LB]
242         PUSH         1
243         LOAD         3[LB]
244         POP          1
245         LOAD         0[SB]
246         POP          1
247         LOAD         1[SB]
248         STORE        4[LB]
249         LOAD         4[LB]
250         LOADL        2
251         LOADL        420
252         CALL         arrayupd
253         LOAD         4[LB]
254         LOADL        2
255         CALL         arrayref
256         LOAD         2[SB]
257         CALLI        L10
258         LOAD         1[SB]
259         LOADL        2
260         CALL         arrayref
261         LOAD         2[SB]
262         CALLI        L10
263         LOAD         4[LB]
264         CALL         arraylen
265         LOAD         2[SB]
266         CALLI        L10
267         LOAD         1[SB]
268         CALL         arraylen
269         LOAD         2[SB]
270         CALLI        L10
271         RETURN (0)   0
272  L27:   PUSH         1
273         LOADL        10
274         CALL         newarr  
275         STORE        3[LB]
276         PUSH         1
277         LOADL        5
278         CALL         newarr  
279         STORE        4[LB]
280         LOAD         3[LB]
281         LOADL        0
282         LOADL        1
283         CALL         arrayupd
284         LOAD         4[LB]
285         LOADL        0
286         LOADL        2
287         CALL         arrayupd
288         LOAD         3[LB]
289         LOADL        1
290         LOADL        2
291         CALL         arrayupd
292         LOAD         4[LB]
293         LOADL        1
294         LOADL        4
295         CALL         arrayupd
296         LOAD         3[LB]
297         LOADL        2
298         LOADL        3
299         CALL         arrayupd
300         LOAD         4[LB]
301         LOADL        2
302         LOADL        6
303         CALL         arrayupd
304         LOAD         3[LB]
305         LOADL        3
306         LOADL        4
307         CALL         arrayupd
308         LOAD         4[LB]
309         LOADL        3
310         LOADL        8
311         CALL         arrayupd
312         LOAD         3[LB]
313         LOADL        4
314         LOADL        5
315         CALL         arrayupd
316         LOAD         4[LB]
317         LOADL        4
318         LOADL        10
319         CALL         arrayupd
320         LOAD         3[LB]
321         LOADL        5
322         LOADL        6
323         CALL         arrayupd
324         LOAD         3[LB]
325         LOADL        6
326         LOADL        7
327         CALL         arrayupd
328         LOAD         3[LB]
329         LOADL        7
330         LOADL        8
331         CALL         arrayupd
332         LOAD         3[LB]
333         LOADL        8
334         LOADL        9
335         CALL         arrayupd
336         LOAD         3[LB]
337         LOADL        9
338         LOADL        10
339         CALL         arrayupd
340         PUSH         1
341         LOADL        15
342         CALL         newarr  
343         STORE        5[LB]
344         LOAD         5[LB]
345         LOADL        0
346         LOADL        1
347         CALL         arrayupd
348         LOAD         5[LB]
349         LOADL        1
350         LOADL        2
351         CALL         arrayupd
352         LOAD         5[LB]
353         LOADL        2
354         LOADL        2
355         CALL         arrayupd
356         LOAD         5[LB]
357         LOADL        3
358         LOADL        3
359         CALL         arrayupd
360         LOAD         5[LB]
361         LOADL        4
362         LOADL        4
363         CALL         arrayupd
364         LOAD         5[LB]
365         LOADL        5
366         LOADL        4
367         CALL         arrayupd
368         LOAD         5[LB]
369         LOADL        6
370         LOADL        5
371         CALL         arrayupd
372         LOAD         5[LB]
373         LOADL        7
374         LOADL        6
375         CALL         arrayupd
376         LOAD         5[LB]
377         LOADL        8
378         LOADL        6
379         CALL         arrayupd
380         LOAD         5[LB]
381         LOADL        9
382         LOADL        7
383         CALL         arrayupd
384         LOAD         5[LB]
385         LOADL        10
386         LOADL        8
387         CALL         arrayupd
388         LOAD         5[LB]
389         LOADL        11
390         LOADL        8
391         CALL         arrayupd
392         LOAD         5[LB]
393         LOADL        12
394         LOADL        9
395         CALL         arrayupd
396         LOAD         5[LB]
397         LOADL        13
398         LOADL        10
399         CALL         arrayupd
400         LOAD         5[LB]
401         LOADL        14
402         LOADL        10
403         CALL         arrayupd
404         LOAD         5[LB]
405         LOAD         3[LB]
406         LOAD         4[LB]
407         CALL         L45
408         CALL         L39
409         LOAD         5[LB]
410         CALL         arraylen
411         LOAD         2[SB]
412         CALLI        L10
413         RETURN (0)   0
414  L28:   LOADA        0[OB]
415         LOADL        0
416         CALL         fieldref
417         RETURN (1)   0
418  L29:   LOADA        0[OB]
419         LOADL        0
420         LOAD         -1[LB]
421         CALL         fieldupd
422         RETURN (0)   1
423  L30:   PUSH         1
424         LOAD         -1[LB]
425         LOADL        1
426         CALL         add     
427         CALL         newarr  
428         STORE        3[LB]
429         JUMP         L32
430  L31:   LOAD         3[LB]
431         LOAD         -1[LB]
432         LOAD         -1[LB]
433         CALL         arrayupd
434         LOAD         -1[LB]
435         LOADL        1
436         CALL         sub     
437         STORE        -1[LB]
438  L32:   LOAD         -1[LB]
439         LOADL        0
440         CALL         ge      
441         JUMPIF (1)   L31
442         LOAD         3[LB]
443         RETURN (1)   1
444  L33:   PUSH         1
445         LOADL        0
446         STORE        3[LB]
447         JUMP         L35
448  L34:   PUSH         1
449         LOAD         -1[LB]
450         LOAD         3[LB]
451         CALL         arrayref
452         STORE        4[LB]
453         LOAD         4[LB]
454         CALLI        L28
455         LOAD         2[SB]
456         CALLI        L10
457         LOAD         3[LB]
458         LOADL        1
459         CALL         add     
460         STORE        3[LB]
461         POP          1
462  L35:   LOAD         3[LB]
463         LOAD         -1[LB]
464         CALL         arraylen
465         CALL         lt      
466         JUMPIF (1)   L34
467         RETURN (0)   1
468         PUSH         1
469         LOADL        0
470         STORE        3[LB]
471         JUMP         L37
472  L36:   LOAD         -1[LB]
473         LOAD         3[LB]
474         CALL         arrayref
475         LOAD         2[SB]
476         CALLI        L10
477         LOAD         3[LB]
478         LOADL        1
479         CALL         add     
480         STORE        3[LB]
481  L37:   LOAD         3[LB]
482         LOAD         -1[LB]
483         CALL         arraylen
484         CALL         lt      
485         JUMPIF (1)   L36
486         RETURN (0)   1
487  L38:   LOAD         -2[LB]
488         LOAD         -1[LB]
489         CALL         arrayref
490         RETURN (1)   2
491  L39:   LOAD         -2[LB]
492         CALL         arraylen
493         LOAD         -1[LB]
494         CALL         arraylen
495         CALL         ne      
496         JUMPIF (0)   L40
497         CALL         L44
498  L40:   PUSH         1
499         LOADL        0
500         STORE        3[LB]
501         JUMP         L43
502  L41:   LOAD         -2[LB]
503         LOAD         3[LB]
504         CALL         arrayref
505         LOAD         -1[LB]
506         LOAD         3[LB]
507         CALL         arrayref
508         CALL         ne      
509         JUMPIF (0)   L42
510         CALL         L44
511  L42:   LOAD         3[LB]
512         LOADL        1
513         CALL         add     
514         STORE        3[LB]
515  L43:   LOAD         3[LB]
516         LOAD         -2[LB]
517         CALL         arraylen
518         CALL         lt      
519         JUMPIF (1)   L41
520         RETURN (0)   2
521  L44:   PUSH         1
522         LOADL        55555555
523         CALL         neg     
524         CALL         newarr  
525         STORE        3[LB]
526         RETURN (0)   0
527  L45:   PUSH         1
528         LOAD         -2[LB]
529         CALL         arraylen
530         LOAD         -1[LB]
531         CALL         arraylen
532         CALL         add     
533         CALL         newarr  
534         STORE        3[LB]
535         PUSH         1
536         LOADL        0
537         STORE        4[LB]
538         PUSH         1
539         LOADL        0
540         STORE        5[LB]
541         PUSH         1
542         LOADL        0
543         STORE        6[LB]
544         JUMP         L49
545  L46:   LOAD         -2[LB]
546         LOAD         4[LB]
547         CALL         arrayref
548         LOAD         -1[LB]
549         LOAD         5[LB]
550         CALL         arrayref
551         CALL         le      
552         JUMPIF (0)   L47
553         LOAD         3[LB]
554         LOAD         6[LB]
555         LOAD         -2[LB]
556         LOAD         4[LB]
557         CALL         arrayref
558         CALL         arrayupd
559         LOAD         4[LB]
560         LOADL        1
561         CALL         add     
562         STORE        4[LB]
563         JUMP         L48
564  L47:   LOAD         3[LB]
565         LOAD         6[LB]
566         LOAD         -1[LB]
567         LOAD         5[LB]
568         CALL         arrayref
569         CALL         arrayupd
570         LOAD         5[LB]
571         LOADL        1
572         CALL         add     
573         STORE        5[LB]
574  L48:   LOAD         6[LB]
575         LOADL        1
576         CALL         add     
577         STORE        6[LB]
578  L49:   LOAD         4[LB]
579         LOAD         -2[LB]
580         CALL         arraylen
581         CALL         lt      
582         LOAD         5[LB]
583         LOAD         -1[LB]
584         CALL         arraylen
585         CALL         lt      
586         CALL         and     
587         JUMPIF (1)   L46
588         LOAD         4[LB]
589         LOAD         -2[LB]
590         CALL         arraylen
591         CALL         lt      
592         JUMPIF (0)   L52
593         JUMP         L51
594  L50:   LOAD         3[LB]
595         LOAD         6[LB]
596         LOAD         -2[LB]
597         LOAD         4[LB]
598         CALL         arrayref
599         CALL         arrayupd
600         LOAD         6[LB]
601         LOADL        1
602         CALL         add     
603         STORE        6[LB]
604         LOAD         4[LB]
605         LOADL        1
606         CALL         add     
607         STORE        4[LB]
608  L51:   LOAD         4[LB]
609         LOAD         -2[LB]
610         CALL         arraylen
611         CALL         lt      
612         JUMPIF (1)   L50
613         JUMP         L55
614  L52:   LOAD         5[LB]
615         LOAD         -1[LB]
616         CALL         arraylen
617         CALL         lt      
618         JUMPIF (0)   L55
619         JUMP         L54
620  L53:   LOAD         3[LB]
621         LOAD         6[LB]
622         LOAD         -1[LB]
623         LOAD         5[LB]
624         CALL         arrayref
625         CALL         arrayupd
626         LOAD         6[LB]
627         LOADL        1
628         CALL         add     
629         STORE        6[LB]
630         LOAD         5[LB]
631         LOADL        1
632         CALL         add     
633         STORE        5[LB]
634  L54:   LOAD         5[LB]
635         LOAD         -1[LB]
636         CALL         arraylen
637         CALL         lt      
638         JUMPIF (1)   L53
639  L55:   LOAD         3[LB]
640         RETURN (1)   2

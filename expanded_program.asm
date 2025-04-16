MACRO
-------------------------------------------------
1         M1 &ARG1 &ARG2      1
2         MOVER AREG D1       2
3         ADD AREG D2         3
4         MEND                4
5         M3 &ARG3 &ARG4      1
6         MOVER AREG #3       2
7         ADD AREG #4         3
8         MEND                4
-------------------------------------------------
MOVER AREG &ARG1
ADD AREG &ARG2
MEND
MACRO
MOVER AREG &ARG3
ADD AREG &ARG4
MEND
START
READ D1
-------------------------------------------------
1         M1 &ARG1 &ARG2      1
2         MOVER AREG D1       2
3         ADD AREG D2         3
4         MEND                4
5         M3 &ARG3 &ARG4      1
6         MOVER AREG #3       2
7         ADD AREG #4         3
8         MEND                4
-------------------------------------------------
PRINT D1
END

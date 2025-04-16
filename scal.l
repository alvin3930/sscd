%{
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "y.tab.h"
void yyerror(char *error);
%}
%%
[0-9]+ { yylval.intval = atoi(yytext); return NUMBER; }
[a-z]+ { yylval.fchar = strdup(yytext); return NAME; }

[\t ];
\n return 0;
. { return yytext[0]; }
%%
int yywrap() { return 1; }

%{
#include <stdio.h>
int count = 0;
%}

L [A-Z]     /* Matches a single capital letter */
N [^A-Z\n]  /* Matches any non-capital character */
R \n        /* Matches a newline */

%%

{L} { printf("%s - Capital Letter\t", yytext); count++; }
{N} { printf("%s - Not a Capital Letter\t", yytext); }
{R} { printf("New Line Detected\t"); }

%%

int yywrap() { return 1; }

int main() {
    printf("Enter text (Press Ctrl+D to stop input):\n");
    yylex();  // Process input from stdin
    printf("\nTotal Capital Letters: %d\n", count);
    return 0;
}

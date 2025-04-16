%{
    int count = 0;
%}
L [A-Za-z]
DI [0-9]+
DF [0-9]+"."[0-9]+
ID [A-Za-z_][A-Za-z0-9_]*
KW (if|else|while|return|for|int|float|char|void)
R [+\*\/-]
S (==|<>|<=|<|>)
SC (\"\"|''|\!|\?|\*|\$|\#|\@|\%)
LOP (&&|\|\|)
ASSIGN [=]
TERM (;|,)
BR [\(\)\{\}\[\]]
WS [ \t]+
NL [\n]
DEF [ ]
%%
{KW} { printf("%s is a keyword.\n", yytext); count++; }
{ID} { printf("%s is an identifier.\n", yytext); count++; }
{DI} { printf("%s is an integer.\n", yytext); count++; }
{DF} { printf("%s is a float.\n", yytext); count++; }
{R} { printf("%s is an arithmetic operator.\n", yytext); count++; }
{SC} { printf("%s is a special character.\n", yytext); count++; }
{S} { printf("%s is a comparison operator.\n", yytext); count++; }
{LOP} { printf("%s is a logical operator.\n", yytext); count++; }
{ASSIGN} { printf("%s is an assignment operator.\n", yytext); count++; }
{TERM} { printf("%s is a terminator (semicolon/comma).\n", yytext); count++; }
{BR} { printf("%s is a bracket.\n", yytext); count++; }
{WS} { printf("Encountered a white space\n"); }
{NL} { printf("Encountered a new line character\n"); count++; }
. { printf("%s is an unknown character.\n", yytext); count++; }
%%

int yywrap() { return 1; }
int main() {
    yylex();
    printf("\nTotal tokens: %d\n", count);
    return 0;
}

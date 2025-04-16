%{
    int i=0;
%}
%%
[0-9]+ {
    i=atoi(yytext);
    if(i%2==0)
        printf("%s is Even", yytext);
    else
        printf("%s is Odd", yytext); 
    }
%%

int yywrap() {}
int main() {
    yylex();
    return 0;
}
#include <stdio.h>
#include <string.h>

#define SUCCESS 1
#define FAILED 0
#define MAX_STACK_SIZE 100

int E(), Edash(), T(), Tdash(), F();

const char *cursor;
char string[64];

char stack[MAX_STACK_SIZE];
int top = -1;

void push(char c) {
    if (top < MAX_STACK_SIZE -1) {
        stack[++top] = c;    
    }
}

void pop() {
    if (top >= 0) {
        top--;
    }
}

void printStack() {
    for(int i = 0; i <= top; i++) {
        printf("%c", stack[i]);
    }
    printf("\n");
}

void printAction(const char *rule) {
    printf("%-16s %-20s", cursor, rule);
    printStack();
}

int main() {
    strcpy(string, "i*i");

    cursor = string;

    printf("\nInput          Action                Stack\n");
    printf("-------------------------------------------------\n");

    if (E() && *cursor == '\0') { 
        printf("-------------------------------------------------\n");
        printf("String is successfully parsed ✅\n");
        return 0;
    } else {
        printf("-------------------------------------------------\n");
        printf("Error in parsing the string ❌\n");
        return 1;
    }
}

int E() {
    printAction("Deriving E -> TE'");
    push('E');  

    if (T() && Edash()) {
        printAction("Reducing E");
        return SUCCESS;
    }

    pop(); 
    return FAILED;
}


int Edash() {
    if (*cursor == '+') {  
        printAction("Pushing '+'");
        cursor++;  
        push('+');

        if (T() && Edash()) {
            printAction("Reducing E'");
            pop();  
            return SUCCESS;
        }
        return FAILED;
    } else {
        printAction("Reducing E' -> ep");  
        return SUCCESS;
    }
}


int T() {
    printAction("Deriving T -> FT'");
    push('T');

    if (F() && Tdash()) {
        printAction("Reducing T");
        return SUCCESS;
    }

    pop(); 
    return FAILED;
}


int Tdash() {
    if (*cursor == '*') {  
        printAction("Pushing '*'");
        cursor++;  
        push('*');

        if (F() && Tdash()) {
            printAction("Reducing T'");
            pop();  
            return SUCCESS;
        }
        return FAILED;
    } else {
        printAction("Reducing T' -> ε");  
        return SUCCESS;
    }
}


int F() {
    if (*cursor == '(') {  
        printAction("Pushing '('");
        cursor++;  
        push('(');

        if (E() && *cursor == ')') {  
            printAction("Pushing ')'");
            cursor++;  
            pop(); 
            printAction("Reducing F -> (E)");
            return SUCCESS;
        }
        return FAILED;
    } else if (*cursor == 'i') {  
        printAction("Pushing 'i'");
        cursor++;  
        push('i');
        printAction("Reducing F -> i");
        pop();
        return SUCCESS;
    } else {
        return FAILED;
    }
}

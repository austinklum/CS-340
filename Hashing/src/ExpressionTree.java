import java.io.IOException;
import java.util.Stack;

public class ExpressionTree { 

    private class Node {
        private Node left; 
        private String data; 
        private Node right; 
        
        private Node(Node l, String d, Node r) { 
            left = l; 
            data = d; 
            right = r; 
        } 
    } 
    private Node root;
    
    private ExpressionTree(Node r) {
        root = r;
    }
    
    public ExpressionTree(String exp) { 
    //PRE: exp is a legal infix expression 
    //Build an expression tree from the expression exp 
        /* +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * CASE 1: PUSH
         *      When "("
         *      When operator being pushed on is greater than .peek()
         *          Always push "^" and "!" since they are greater
         *          EX: .peek() is a + and thing to push on is a *
         *              Since * is greater than + push the * on the stack 
         *              
         *   A private method that will take in .peek().data and arr[i] and determines
         *   which is greater would be useful
         *   
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         * 
         * CASE 2: POP
         *      When ")"
         *      When operator being pushed on is lesser than .peek()
         *          Always pop if .peek() is a "^" or "!" and arr[i] is not
         *          EX: .peek is a "*" and thing to push on is a "+"
         *              Since "+" is less than "*" pop the "*" from the stack
         * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         *              
         *  PROCESS FOR POPPING
         *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         *      1.) Pop off 1 operator
         *      2.) Pop off 2 operands
         *      3.) Merge them with the operator as root
         *      4.) Push results ontop operands stack 
         * 
         * */
        //Build up a tree
        Stack<String> operators = new Stack<>();
        Stack<ExpressionTree> operands = new Stack<>();
        boolean sawParen = false;
        //Loop through everything
        for (String item: exp.split(" ")) {
//            System.out.println("Operators: " + operators);
//            System.out.println("Operands: " + operands + "\n");
            sawParen = false;
            if(isOperator(item.charAt(0))) {
                //Operator stack is empty or the thing being pushed on has greater precedence
                if(operators.isEmpty() || shouldPush(operators.peek(),item)){
                    operators.push(item);
                //Thing being pushed on has lesser precendence
                } else {
                    // While there is enough stuff on stack and should keep popping
                    while(hasEnough(operators,operands) && !shouldPush(operators.peek(),item) && !sawParen) {
//                        System.out.println("IN WHILE: Operators: " + operators);
//                        System.out.println("IN WHILE: Operands: " + operands + "\n");

                        if(item.charAt(0) == ')') { 
                            sawParen = true; 
                        }
                        pop(operators,operands,sawParen);
                    }
                    //Dont push on a )
                    if(!sawParen) {
                        operators.push(item);
                    }
                }
            //Must be an operand
            } else {
                operands.push(new ExpressionTree(new Node(null,item,null)));
            }
        }
        sawParen = false;
        //Empty the Operator stack
        while(!operators.isEmpty()) {
            pop(operators, operands, sawParen);
        }
        
        //Use that tree to query postfix and infix
        root = operands.pop().root;
    }
    

    
    public int evaluate(SymbolTable t) { 
    //return the int value of the expression tree 
    //t is used to lookup values of variables 
    return evaluate(t, root); 
    } 
    
    private int evaluate(SymbolTable t,Node r) {
    //return the int value of the expression tree with root r 
    //t is used to lookup values of variables 
        if(r == null) {
            return 0;
        }
        //If its a number
            if (isNumeric(r.data)) {
            return Integer.parseInt(r.data);
        //If its a variable from the table
        } else if (t.find(r.data)) {
            return Integer.parseInt(t.getData(r.data).toString());
        } else if(isOperator(r.data.charAt(0))){
            //Must be an Operator
            return doOp(r.data, evaluate(t, r.left), evaluate(t, r.right));
        //Uninitalized variable
        } else {
            return 0;
        }
    }
    //if is digit return value
    //if is varible return value from symbol table
    //else is operator return doOperator(r.data,r.left,r.right)
    
    public String toString() {
        return toPostfix();
    }
    
    public String toPostfix() { 
        //return the postfix representaTIon of the expression tree 
        return toPostfix(root); 
    } 
    
    private String toPostfix(Node r) { 
    //return the postfix representaTIon of the tree with root r  
        if(r == null) {
            return "";
        }
        return toPostfix(r.left) + toPostfix(r.right) + r.data;
    } 
    
    public String toInfix() { 
    //return the fully parenthesized infix representaTIon of the expression tree 
        return toInfix(root); 
    } 
    
    private String toInfix(Node r) { 
    //return the fully parenthesized infix representaTIon of the tree with root r
        if(r == null) {
            return "";
        }
        
        return "(" + toInfix(r.left) + r.data + toInfix(r.right) + ")";
    }
    
    private boolean isOperator(char c) {
        return 
               c == '+' || c == '-' || c == '*' || 
               c == '/' || c == '^' || c == '%' ||
               c == '!' || c == '(' || c == ')';
    }
    
    /**
     * Tells the user weather or not to push
     * @param op top of stack
     * @param str character trying to push
     * @return if pushing is okay
     */
    private boolean shouldPush(String op, String str) {
        char newChar = str.charAt(0);
        int peek = getLevel(op);
        
        //If peek is an '(' dont pop yet
        if(peek == 5) {
            peek = 0;
        }
        int c = getLevel(str);
        //If next thing is greater value
        if(peek < c) {
            return true;
        //If next thing is a ! or ^
        } else if (peek == c && (newChar == '!' || newChar == '^')) {
                return true;
        }
        
        return false;
    }
    
    private int getLevel(String str) {
        return getLevel(str.charAt(0));
    }
    /**
     * Gets the precendence level of given char
     * @param c character to check
     * @return level
     */
    private int getLevel(char c) {
        switch(c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
            case '%':
                return 2;
            case '^':
                return 3;
            case '!':
                return 4;
            case '(':
                return 5;
            case ')':
                return -1;
        }
        return 0;
    }
    
    /**
     * Logic to deal when encountering a operator of lesser precendence 
     * Pops 2 operands and 1 operator; Merges them and puts it back onto operands stack.
     * If a ! pops 1 operand and 1 operator; Merges them with data being stored on right child
     * @param operators
     * @param operands
     * @param sawParen Used to loop until seeing a opening '('
     */
    private void pop(Stack<String> operators, Stack<ExpressionTree> operands, boolean sawParen) {
        do {
            String operator = operators.pop();
            
            //Stop looping if we see a '('
            if(operator.charAt(0) == '(')
                return;
            
            //Get the peices to be merged together
            ExpressionTree rightItem = operands.pop();
            ExpressionTree leftItem = null;
            
            //If it's not a ! pop another thing off
            if(operator.charAt(0) != '!') {
                leftItem = operands.pop();
            }
            //Put the merged tree back onto the operands stack
            //Standard case
            if(leftItem != null) {
                operands.push(new ExpressionTree(new Node(leftItem.root, operator, rightItem.root)));
            // it must be a ! case   
            } else {
                operands.push(new ExpressionTree(new Node(null, operator, rightItem.root)));
            }
        } while (sawParen);
        
    }
    /**
     * Returns if there is enough stuff on the stack to pop off of
     * @param operators
     * @param operands
     * @return 
     */
    private boolean hasEnough(Stack<String> operators, Stack<ExpressionTree> operands) {
        if(operands.isEmpty() || operators.isEmpty()) {
            return false;
        }
        if(operators.peek().charAt(0) == '!') {
            return operands.size() >= 1;
        }else {
            return operands.size() >= 2;
        }
    }
    /**
     * Used to test if string of characters are numeric
     * @param str
     * @return if string is numeric
     */
    private boolean isNumeric(String str){
      return str.matches("-?\\d+(\\.\\d+)?");
    }
    /**
     * Used to do the operation in evaluation
     * @param op operator 
     * @param left left hand side of operation
     * @param right right hand side op operation
     * @return calculated operation from left, operator, right
     */
    private int doOp(String op, int left, int right) {
        switch(op.charAt(0)){
            case '+':
                return left + right;
            case '-':
                return left - right;
            case '*':
                return left * right;
            case '/':
                return left / right;
            case '%':
                return left % right;
            case '^':
                return (int)Math.pow(left, right);
            case '!':
                return right * -1;
        }
        return 0;
    }
    
    public static void main(String args[]){ 
    //used to test expression tree 
//        ExpressionTree t = new ExpressionTree("! A + B * C ^ ! ( D - E ) ^ F + H / I");
//        System.out.println(t.toPostfix());
//        System.out.println(t.toInfix());
//        SymbolTable st = new SymbolTable(5);
//        ExpressionTree t = new ExpressionTree("A + 1");
//        System.out.println(t);
//        System.out.println(t.evaluate(st));
    } 
    
}

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
    
    public ExpressionTree(String exp) { 
    //PRE: exp is a legal infix expression 
    //Build an expression tree from the expression exp 
        /* Case 1: Push
         *      When "("
         *      When operator being pushed on is greater than .peek()
         *          Always push "^" and "!" since they are greater
         *          EX: .peek() is a + and thing to push on is a *
         *              Since * is greater than + push the * on the stack 
         *              
         *   A private method that will take in .peek().data and arr[i] and determines
         *   which is greater would be useful
         * 
         * Case 2: Pop
         *      When ")"
         *      When operator being pushed on is lesser than .peek()
         *          Always pop if .peek() is a "^" or "!" and arr[i] is not
         *          EX: .peek is a "*" and thing to push on is a "+"
         *              Since "+" is less than "*" pop the "*" from the stack
         *              
         *  PROCESS FOR POPPING
         *  +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
         *      1.) Pop off 1 operator
         *      2.) Pop off 2 operands
         *      3.) Merge them with the operator as root
         *      4.) Push results ontop operands stack 
         * 
         * */
        //Process exp
        String[] arr = exp.split(" ");
        //Build up a tree
        Stack<Node> operators = new Stack<>();
        Stack<String> operands = new Stack<>();
        
        for (int i = 0; i < arr.length; i++) {
            //
            if(isOperator(arr[i].charAt(0))) {
                //If the top of the stack has greater precendence pop some values
                if(operators.isEmpty() || opComp(operators.peek().data.charAt(0), arr[i].charAt(0)) == 1){
                    operators.push(new Node(null,arr[i],null));
                }
            }
        }
        
        //Use that tree to query postfix and infix
    }
    
    private ExpressionTree() {
        
    }
    
    public int evaluate(SymbolTable t) { 
    //return the int value of the expression tree 
    //t is used to lookup values of variables 
    return evaluate(t, root); 
    } 
    
    private int evaluate(SymbolTable t,Node r) { 
    //return the int value of the expression tree with root r 
    //t is used to lookup values of variables 
        
    }
    
    public String toPostfix() { 
        //return the postfix representaTIon of the expression tree 
        return toPostfix(root); 
    } 
    
    private String toPostfix(Node r) { 
    //return the postfix representaTIon of the tree with root r  
        if(r == null) 
            return "";
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
        return c == '+' || c == '-'|| c == '*' || c == '/' || c == '^';
    }
    
    private int opComp(char op1, char op2) {
        
       op1 = toOneSign(op1);
       op2 = toOneSign(op2);
          
           //IF we end up return a 1 we should NOT pop anything
         if ((op1 == '+' && op2 == '*') || (op2 == '^')) {
            return 1;
            //We should pop some values
        } else if ((op1 == '*' && op2 == '+') || (op1 == '^')) {
            return -1;
        }else {
            //They are equal thus we should pop things
            return -1;
        }
    }
    
    private char toOneSign(char op) {
        if(op == '-') {
            op = '+';
        }else if (op == '/'){
            op = '*';
        }
        
        return op;
    }
    
    public static void main(String artis[]) throws IOException { 
    //used to test expression tree 
    } 
    
}

package homework3;

import java.util.ArrayList;

public class ProjectEuler {
    //public static ArrayList<Integer> al = new ArrayList<>();
    public static void main(String[] args){
       for(int i=1; i<=10; i++){
           System.out.println(fiborecur(i));
       }
       
    }
    
    public static int fiborecur(int num){
        if(num ==1 || num == 2){
            return 1;
        }
        return fiborecur(num-1) + fiborecur(num-2);
    }
    
    public static void fibonacciLoop(int number){
        if(number == 1|| number ==2 ){
           // return 1;
        }
        int f1 = 1; int f2 = 1; int fibo = 1;
        for(int i=3; i<=number; i++){
            fibo = f1 + f2;
            f1 = f2;
            f2 = fibo;
            System.out.println(fibo);
        }
        
    }
}

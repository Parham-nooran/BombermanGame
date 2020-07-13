package ir.ac.kntu.logic;

import java.util.Scanner;

public class ScannerWrapper {
    private static ScannerWrapper instance = new ScannerWrapper();
    private Scanner scanner;
    private ScannerWrapper(){
        scanner = new Scanner(System.in);
    }
    public static ScannerWrapper getInstance(){
        return instance;
    }
    public int nextInt(){
        return scanner.nextInt();
    }
    public String next(){
        return scanner.next();
    }
    public double nextDouble(){
        return scanner.nextDouble();
    }
    public String nextLine(){
        return scanner.nextLine();
    }
}

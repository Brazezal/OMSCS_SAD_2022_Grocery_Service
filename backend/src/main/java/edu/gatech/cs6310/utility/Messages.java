package edu.gatech.cs6310.utility;

public final class Messages {

    private Messages() {
    }

    public static void changeCompleted() {
        System.out.println("OK:change_completed");
    }

    public static void displayCompleted() {
        System.out.println("OK:display_completed");
    }

    public static void exceptionMessage(String error) {System.out.println("ERROR:" + error);}

    public static void displayErrorMessage(String error) { System.out.println("ERROR:" + error); }




}


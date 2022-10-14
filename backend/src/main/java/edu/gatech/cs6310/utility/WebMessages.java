package edu.gatech.cs6310.utility;


public final class WebMessages {

    private WebMessages() {
    }

    public static String changeCompleted() {
        return "OK:change_completed";
    }


    public static String SuccessMessage(String info) {
        return "Success:" + info;
    }

    public static String ErrorMessage(String error) {
        return "ERROR:" + error;
    }


}


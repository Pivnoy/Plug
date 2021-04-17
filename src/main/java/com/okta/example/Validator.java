package com.okta.example;

public class Validator implements IValidator {

    private boolean noZeroLength(String [] fileNames) {
        return fileNames.length != 0;
    }

    private boolean noNullMembers(String [] args) {
        for (String s: args){
            if (s == null) return false;
        }
        return true;
    }

    public boolean validate(String [] fileNames,String [] words) {
        return noZeroLength(fileNames) && noNullMembers(fileNames) && noNullMembers(words);
    }

}

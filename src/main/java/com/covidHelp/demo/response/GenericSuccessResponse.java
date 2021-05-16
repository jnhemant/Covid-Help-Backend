package com.covidHelp.demo.response;

public class GenericSuccessResponse {
    private String message;

    public GenericSuccessResponse() { message = "Action was successfully performed";}
    public GenericSuccessResponse(String message){
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}

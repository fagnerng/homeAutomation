package br.edu.ufcg.ccc.homeautomation.networking;

public enum HTTPType {
    
    POST("POST"), PUT("PUT"), DELETE("DELETE");

    private String name;

    HTTPType(String name) {
    	this.name = name;
    }
    
    @Override
    public String toString(){
        return this.name;
    }
    
    public int toInt() {
        return ordinal();
    }
        
}
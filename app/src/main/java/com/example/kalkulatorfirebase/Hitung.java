package com.example.kalkulatorfirebase;

public class Hitung {

    private String ID, Var1, Var2, Operator, Result;

    public Hitung(){

    }
    public Hitung(String ID, String Var1, String Var2, String Operator, String Result){
        this.ID = ID;
        this.Var1 = Var1;
        this.Var2 = Var2;
        this.Operator = Operator;
        this.Result = Result;
    }
    public String getID(){
        return ID;
    }
    public void setID(String ID){
        this.ID = ID;
    }
    public String getVar1(){
        return Var1;
    }
    public void setVar1(String Var1){
        this.Var1 = Var1;
    }
    public String getVar2(){
        return Var2;
    }
    public void setVar2(String Var2){
        this.Var2 = Var2;
    }
    public String getOperator(){
        return Operator;
    }
    public void setOperator(String Operator){
        this.Operator = Operator;
    }
    public String getResult(){
        return Result;
    }
    public void setResult(String Result){
        this.Result = Result;
    }
}

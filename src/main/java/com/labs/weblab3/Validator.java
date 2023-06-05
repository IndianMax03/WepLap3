package com.labs.weblab3;

import static java.lang.Math.pow;

public class Validator {
    private Double xValue;
    private Double yValue;
    private Double rValue;
    private String hitResult;
    private boolean checkFirstQuarterHit(){
        return false;
    }
    private boolean checkThirdQuarterHit(){
        return false;
    }
    private boolean checkFourQuarterHit(){
        return false;
    }
    public boolean checkHit(){
        if (checkFirstQuarterHit() || checkThirdQuarterHit() || checkFourQuarterHit()) {
            setHitResult("Попадание");
            return true;
        } else {
            setHitResult("Промах");
            return false;
        }
    }

    public Double getValueX() {
        return xValue;
    }

    public Double getValueY() {
        return yValue;
    }

    public Double getValueR() {
        return rValue;
    }

    public void setValueX(Double xValue) {
    }

    public void setValueY(Double yValue) {
    }

    public void setValueR(Double rValue) {
    }


    public String getHitResult() {
        return hitResult;
    }

    public void setHitResult(String hitResult) {
        this.hitResult = hitResult;
    }
}

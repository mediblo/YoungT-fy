package com.youngtfy.common;

import java.io.Serializable;

/*
command 명령어
1 = 로그인
 */

public class DataObject implements Serializable {
    private static final long serialVersionUID = 1L; // 직렬화 버전 ID
    private int command; // 명령어 코드
    private int[] numbers; // 정수 배열
    private String[] strings; // 문자열 배열

    public DataObject(int command, int[] numbers, String[] strings) {
        this.command = command;
        this.numbers = numbers;
        this.strings = strings;
    }

    public DataObject(int command, int[] numbers){
        this.command = command;
        this.numbers = numbers;
    }

    public DataObject(int command, String[] strings){
        this.command = command;
        this.strings = strings;
    }

    public DataObject(int command){
        this.command = command;
    }

    public int getCommand() { return this.command; }

    public int[] getNumbers() {
        return numbers;
    }

    public String[] getStrings() {
        return strings;
    }
}

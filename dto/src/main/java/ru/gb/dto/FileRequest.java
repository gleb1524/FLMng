package ru.gb.dto;

public class FileRequest implements BasicRequest{

    @Override
    public String getType() {
        return "file";
    }
}

package ru.gb.dto;

public class GetFileListRequest implements BasicRequest{
    @Override
    public String getType() {
        return "getFileList";
    }
}

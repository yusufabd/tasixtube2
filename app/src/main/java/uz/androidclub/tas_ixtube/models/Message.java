package uz.androidclub.tas_ixtube.models;

/**
 * Created by yusufabd on 4/22/2017.
 */
public class Message {
    private Integer progress;
    private String message;

    public Message(Integer progress, String message) {
        this.progress = progress;
        this.message = message;
    }

    public Message(Integer progress) {
        this.progress = progress;
        this.message = "";
    }

    public Message(String message) {
        this.progress = 0;
        this.message = message;
    }

    public Integer getProgress() {
        return progress;
    }


    public String getMessage() {
        return message;
    }
}
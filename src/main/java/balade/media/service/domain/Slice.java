package balade.media.service.domain;

import java.text.MessageFormat;

public class Slice {
    private  String name;
    private String type;
    private long size;
    private byte[] data;

    public Slice() {
    }


    public Slice(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public Slice(String name, String type, long size, byte[] data) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String toString(){
        return  MessageFormat.format("[Name: {0}, size: {1}, data: {2}, type : {3}]",getName(), getSize(), getData(), getType());
    }
}

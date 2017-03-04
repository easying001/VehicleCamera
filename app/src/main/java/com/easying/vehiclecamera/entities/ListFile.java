package com.easying.vehiclecamera.entities;

import java.io.File;
import java.io.Serializable;

/**
 * Created by think on 2016/11/4.
 */

public class ListFile implements Serializable {
    private File file;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public ListFile() {

    }

    public ListFile(File file, String type) {
        this.file = file;
        this.type = type;
    }
}
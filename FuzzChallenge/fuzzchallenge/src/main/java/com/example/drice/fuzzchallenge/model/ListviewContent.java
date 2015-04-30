package com.example.drice.fuzzchallenge.model;

import java.io.Serializable;

/**
 * Created by DrIce on 4/29/15.
 */
public class ListviewContent implements Serializable {

    private String id;
    private String date;
    private String data;
    private String type;

    public ListviewContent() {
        id = "";
        date = "";
        data = "";
        type = "";
    }

    public ListviewContent(Builder builder) {
        this.id = builder.id;
        this.date = builder.date;
        this.data = builder.data;
        this.type = builder.type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public int hashCode() {
        int hash = 31;
        int result = 17;
        result = hash * result + ((id==null) ? 0 : id.hashCode());
        result = hash * result + ((type == null) ? 0 : type.hashCode());
        result = hash * result + ((data == null) ? 0 : data.hashCode());
        result = hash * result + ((date == null) ? 0 : date.hashCode());
        return result;
    }


    @Override
    public String toString() {
        return ("id: " + id + " type: " + type + " date: " + date +
                " data: " + data);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ListviewContent){
            if (((ListviewContent)o).toString().compareTo(this.toString()) == 0)
                return true;
        }
        return false;
    }

    public static class Builder {
        private String id;
        private String date;
        private String data;
        private String type;

        public Builder(String id) {
            this.id = id;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public Builder data(String data) {
            this.data = data;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public ListviewContent build() {
            return new ListviewContent(this);
        }
    }
}

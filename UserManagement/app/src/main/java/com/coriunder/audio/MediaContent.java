package com.coriunder.audio;

public class MediaContent {
    private int id;
    private String uri;
    private String name;

    public MediaContent(int id, String name, String uri) {
        this.id = id;
        this.name = name;
        this.uri = uri;
    }

    public int getID() { return this.id; }
    public String getName() { return this.name; }
    public String getUri() { return this.uri; }
}
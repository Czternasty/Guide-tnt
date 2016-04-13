package com.example.rmi.guide_tnt.model;

/**
 * Created by Rémi on 13/04/2016.
 */
public class Channel implements Comparable {

    private int id;
    protected String name;
    protected int channelKey;

    public Channel(int id, String name, int channelKey) {
        this.id = id;
        this.name = name;
        this.channelKey = channelKey;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getChannelKey() {
        return channelKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Channel channel = (Channel) o;

        return id == channel.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    public int compareTo(Object another) {
        if (another instanceof Channel)
            return Integer.compare(id, ((Channel) another).getId());
        else return 1;
    }
}

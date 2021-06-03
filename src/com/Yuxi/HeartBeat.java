package com.Yuxi;

import java.io.Serializable;
import java.sql.Time;

public class HeartBeat implements Serializable {
    private final byte[] beat;
    Time time;
    HeartBeat() {
        beat = new byte[]{Byte.parseByte("hearbeat")};
    }


}

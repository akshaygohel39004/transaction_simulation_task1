package org.example.business;

import java.util.UUID;

public class Utility {
    public static long generateUniqueLongId() {
        UUID uuid = UUID.randomUUID();
        long uniqueLong = uuid.getMostSignificantBits() & Long.MAX_VALUE;
        return uniqueLong;
    }
}

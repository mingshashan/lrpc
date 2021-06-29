package com.mss.gtr.lrpc.protocol.protocol;

/**
 * response status
 */
public enum Status {

    /**
     * success
     */
    SUCCESS(0x01),

    /**
     * failure
     */
    FAILURE(0x02);
    private int type;

    Status(int type) {
        this.type = type;
    }
}

package com.tpasystem;

import org.bukkit.entity.Player;

public class TPARequest {

    private final Player requester;
    private final Player target;
    private final long requestTime;

    public TPARequest(Player requester, Player target) {
        this.requester = requester;
        this.target = target;
        this.requestTime = System.currentTimeMillis();
    }

    public Player getRequester() {
        return requester;
    }

    public Player getTarget() {
        return target;
    }

    public long getRequestTime() {
        return requestTime;
    }
}

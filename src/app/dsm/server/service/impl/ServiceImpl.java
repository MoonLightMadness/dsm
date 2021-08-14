package app.dsm.server.service.impl;

import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.service.Service;

public class ServiceImpl implements Service {

    private ListenerAdapter listenerAdapter;

    @Override
    public void invoke(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }

    @Override
    public void setArgs(Object obj, String... args) {
        this.listenerAdapter = (ListenerAdapter) obj;
    }
}

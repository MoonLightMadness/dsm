package app.dsm.server;

import app.dsm.server.container.Container;

public interface BeatChecker extends Runnable {

    void startBeat(Container container, long timeOut, int maxBeat);


}

package app.dsm.swarm.service;

import app.dsm.server.adapter.ListenerAdapter;
import app.dsm.server.annotation.Path;
import app.log.LogSystem;
import app.log.LogSystemFactory;

/**
 * @ClassName : app.dsm.swarm.service.QueenBaseService
 * @Description :
 * @Date 2021-08-19 08:44:36
 * @Author ZhangHL
 */
@Path("/queen")
public class QueenBaseService {

    private ListenerAdapter listenerAdapter;

    private LogSystem log = LogSystemFactory.getLogSystem();



}

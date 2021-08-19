package app.dsm.swarm.nest;

import app.dsm.config.Configer;
import app.dsm.server.Server;
import app.dsm.server.impl.ServerImpl;

import java.util.Locale;

/**
 * @ClassName : app.dsm.swarm.nest.Bee
 * @Description :
 * @Date 2021-08-19 08:52:04
 * @Author ZhangHL
 */
public class Bee {

    private String character;

    private Server server;

    private Configer configer;

    public Bee(String character){
        this.character = character;
    }

    public void initialize(){
        configer = new Configer();
        server = new ServerImpl();
        String ip = character.toLowerCase(Locale.ROOT)+".ip";
        String port = character.toLowerCase(Locale.ROOT)+".port";
        server.initialize(configer.readConfig(ip), configer.readConfig(port) );
    }
}

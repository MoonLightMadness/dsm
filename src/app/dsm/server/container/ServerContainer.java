package app.dsm.server.container;

import lombok.Data;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Data
public class ServerContainer implements Container{

    private List<ServerEntity> servers;

    public void initialize(){
        servers = new ArrayList<ServerEntity>();
    }

    public void add(SocketChannel socketChannel){
        ServerEntity server = new ServerEntity();
        server.setSocketChannel(socketChannel);
        ListIterator<ServerEntity> listIterator = servers.listIterator();
        listIterator.add(server);
    }

    public void delete(ServerEntity serverEntity){
        ListIterator<ServerEntity> listIterator = servers.listIterator();
        while (listIterator.hasNext()){
            if (listIterator.next() == serverEntity){
                listIterator.remove();
                return;
            }
        }
    }

    @Override
    public List getList() {
        return servers;
    }
}

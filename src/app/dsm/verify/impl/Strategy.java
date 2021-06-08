package app.dsm.verify.impl;

import app.dsm.config.ConfigReader;
import app.dsm.config.impl.IPConfigReader;
import app.dsm.config.impl.StrategyConfigReader;
import app.dsm.verify.IStrategy;

public class Strategy implements IStrategy {

    ConfigReader configReader ;

    String[] strategies;


    @Override
    public String[] read() {
        configReader = new StrategyConfigReader();
        strategies = configReader.read();
        return null;
    }

    @Override
    public int verify(String ip) {
        for (int i = 0; i < strategies.length; i++) {
            //全同意策略
            if(strategies[i].equals("*")){
                return 1;
            }
            if (strategies[i].equals(ip)){
                return 1;
            }
        }
        return -1;
    }
}

package app.dsm.config.impl;

import app.dsm.config.ConfigReader;

import java.io.*;

public class StrategyConfigReader implements ConfigReader {

    private final String name = "strategy";

    @Override
    public String[] read() {
        File f = new File("./config.txt");
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(f));
            String temp;
            while ((temp= reader.readLine())!=null){
                if(temp.toLowerCase().startsWith("strategy")){
                    temp = temp.split("=")[1].trim();
                    sb.append(temp).append("\n");
                }
            }
            return sb.toString().split("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

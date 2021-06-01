package app.dsm.bili;

public enum STATECODE {
    /*
        It used to show everything is Done
     */
    DONE("DONE",1),
    /*
        It used to show there has Tags to be loaded
     */
    UNDONE("UNDONE",2),
    /*
        Everything is OK
     */
    NORMAL("NORMAL",200),
    /*
        You are banned by this website
     */
    BANNED("BANNED",403),
    /*
        Redirect to another url
     */
    REDIRECT("REDIRECT",302);
    private String code;
    private int id;
    STATECODE(String code, int id){
        this.code=code;
        this.id=id;
    }
    public String getStateCodeById(int id){
        for(STATECODE sc:STATECODE.values()){
            if(sc.id==id){
                return sc.code;
            }
        }
        return null;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

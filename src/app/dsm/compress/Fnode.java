package app.dsm.compress;

/**
 * @ClassName : app.dsm.compress.Fnode
 * @Description :
 * @Date 2021-06-03 14:46:45
 * @Author ZhangHL
 */
public class Fnode {

    private int len;

    private int index;

    private int priority;

    public Fnode(int index,int len){
        this.index = index;
        this.len = len;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}

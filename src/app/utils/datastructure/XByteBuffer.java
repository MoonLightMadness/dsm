package app.utils.datastructure;

/**
 * @ClassName : app.utils.datastructure.XByteBuffer
 * @Description :
 * @Date 2021-06-01 14:33:21
 * @Author ZhangHL
 */
public class XByteBuffer {
    byte[] bytes;
    int size;

    public XByteBuffer() {
        bytes = new byte[1024];
        size = 0;
    }

    public XByteBuffer append(byte[] newBytes) {
        while ((size+ newBytes.length)>= bytes.length){
            mutiple();
        }
        System.arraycopy(newBytes, 0, bytes, size, newBytes.length);
        size+= newBytes.length;
        return this;
    }

    public byte[] getBytes(){
        byte[] rb = new byte[size];
        System.arraycopy(bytes,0,rb,0,size);
        return rb;
    }

    public int getSize(){
        return size;
    }

    private void mutiple() {
        int len = bytes.length;
        byte[] newBytes = new byte[len*2];
        System.arraycopy(bytes,0,newBytes,0,len);
    }
}

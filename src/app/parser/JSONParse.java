package app.parser;

public interface JSONParse<T> {

    T parser(byte[] json,Class clazz);

    Object propertiesParser(byte[] data,Class clazz);
}

package screenmatch.screenmatch.service;

public interface IDataConversion {
    <T> T obtainData(String json, Class<T> clazz);
}

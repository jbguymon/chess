package service;
import dataaccess.dataaccess;

public class ClearService {
    private final dataaccess data;
    public ClearService(dataaccess data){
        this.data = data;
    }

    public void clear() throws Exception{
        data.clear();
    }

}

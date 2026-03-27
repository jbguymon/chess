package service;
import dataaccess.DataAccess;

public class ClearService {
    private final DataAccess data;
    public ClearService(DataAccess data){
        this.data = data;
    }

    public void clear() throws Exception{
        data.clear();
    }

}

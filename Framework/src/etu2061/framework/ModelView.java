package etu2061.framework;
import java.util.*;

public class ModelView {
    String url;
    HashMap<String, Object> data = new HashMap<String, Object>();

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public HashMap<String, Object> getData() {
        return data;
    }
    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }
  
    public ModelView(){}

    public ModelView(String url){
        this.setUrl(url);
    }

    public void addItem(String key, Object value){
        this.getData().put(key, value);
    }
}

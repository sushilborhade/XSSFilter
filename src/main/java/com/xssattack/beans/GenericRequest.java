package com.xssattack.beans;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;

@Data
@ToString
public class GenericRequest {
    private String queryId;
    private ArrayList<Object> params;
//    public String getQueryId() {
//        return queryId;
//    }
//    public void setQueryId(String queryId) {
//        this.queryId = queryId;
//    }
//    public List<Object> getParams() {
//        return params;
//    }
//    public void setParams(List<Object> params) {
//        this.params = params;
//    }
//    @Override
//    public String toString() {
//        return "GenericRequest [queryId=" + queryId + ", params=" + params + "]";
//    }
}


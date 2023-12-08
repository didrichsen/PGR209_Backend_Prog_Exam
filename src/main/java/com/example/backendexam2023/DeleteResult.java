package com.example.backendexam2023;

import java.util.List;

public class DeleteResult {

    private boolean isDeletable;

    private List<Long> IdsInUse;

    public DeleteResult(boolean success, List<Long> IdsInUse){
        this.isDeletable = success;
        this.IdsInUse = IdsInUse;
    }

    public boolean isDeletable(){
        return isDeletable;
    }

    public List<Long> getIdsInUse(){
        return IdsInUse;
    }

}

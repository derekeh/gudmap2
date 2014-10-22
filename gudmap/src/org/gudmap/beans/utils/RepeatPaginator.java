package org.gudmap.beans.utils;

import java.util.List;

import org.gudmap.impl.RepeatPaginatorImpl;

public class RepeatPaginator extends RepeatPaginatorImpl{

    public List<?> model;
    //add  models via the constructor or postconstruct of your bean as required. See InsituTableBean as an example

    public RepeatPaginator(List<?> model) {
        super(model);
    }
}


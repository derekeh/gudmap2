package org.gudmap.utils;

import java.util.List;
/*
 * This paginator takes in and processes the whole model. EG. All the subsequent work is done  client side on the model with no return to the DB server.
 * This is not the preferred option at present.
 * We use PagerImpl.java so that a definite number of records are retrieved per request.
 */

import org.gudmap.impl.RepeatPaginatorImpl;

public class RepeatPaginator extends RepeatPaginatorImpl{

    public List<?> model;
    //add  models via the constructor or postconstruct of your bean as required. See InsituTableBean as an example

    public RepeatPaginator(List<?> model) {
        super(model);
    }
}


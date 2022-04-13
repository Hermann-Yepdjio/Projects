package com.example.linconnu.carapp2;
/*
 *Member#1: Hermann Yepdjio
 * SID: 40917845
 * Member#2: Pushkin Feleke
 * SID: ??
 */
import java.io.Serializable;

/**
 * Created by linconnu on 07/11/17.
 */

public class car implements Serializable
{
    public String id, name, price, year;

    public car(String id, String name, String year, String price)
    {
        this.id=id;
        this.name=name;
        this.price=price;
        this.year=year;
    }
}

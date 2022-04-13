package com.example.linconnu.sqlitekino;

/*
 *Name: Hermann Yepdjio
 *SID: 40917845
 */
import java.io.Serializable;

/**
 * Created by linconnu on 03/11/17.
 */

public class movie_instance implements Serializable
{
    public String id;
    public String name;
    public String genre;
    public String price;

    public movie_instance(String id, String name, String genre, String price)
    {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.price = price;
    }
}

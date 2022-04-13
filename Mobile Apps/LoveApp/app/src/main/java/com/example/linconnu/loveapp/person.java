package com.example.linconnu.loveapp;
/*
 *member#1: Hermann Yepdjio
 *SID: 40917845
 * member#2: Pushkin Feleke
 * SID: ??
 */

import java.io.Serializable;

/**
 * Created by linconnu on 14/11/17.
 */

public class person implements Serializable
{
    public String id, name, age, sex, interests;

    public person(String id, String name, String age, String sex, String interests)
    {
        this.id=id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.interests = interests;
    }
}

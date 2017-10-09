package me.henry.ziggslab.greendblab.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zj on 2017/10/9.
 * me.henry.ziggslab.greendblab
 */
@Entity(nameInDb = "heros")
public class Heros {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String age;
    private Boolean sex;
    private Integer phone;
    public Heros(String name, String age, Boolean sex, Integer phone) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
    }
    @Generated(hash = 1116193761)
    public Heros(Long id, String name, String age, Boolean sex, Integer phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
    }
    @Generated(hash = 1535359116)
    public Heros() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAge() {
        return this.age;
    }
    public void setAge(String age) {
        this.age = age;
    }
    public Boolean getSex() {
        return this.sex;
    }
    public void setSex(Boolean sex) {
        this.sex = sex;
    }
    public Integer getPhone() {
        return this.phone;
    }
    public void setPhone(Integer phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Heros{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age='" + age + '\'' +
                ", sex=" + sex +
                ", phone=" + phone +
                '}';
    }
}

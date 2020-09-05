package com.github.codingdebugallday.pojo;

/**
 * <p>
 * description
 * </p>
 *
 * @author isaac 2020/9/6 6:06
 * @since 1.0.0
 */
public class Company {

    private String name;
    private String address;
    private int scale;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", scale=" + scale +
                '}';
    }
}

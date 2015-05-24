package fr.xebia.blog.model;

public class Article {

    private String name;
    private double priceHT;

    public Article() {
    }

    public Article(String name, double priceHT) {
        this.name = name;
        this.priceHT = priceHT;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceHT() {
        return priceHT;
    }

    public void setPriceHT(double priceHT) {
        this.priceHT = priceHT;
    }
}

package com.microBusiness.manage.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by afei.
 * User: mingbai
 * Date: 2018/3/14 上午11:53
 * Describe:
 * Update:
 */
public class GoodsInfo implements Serializable {

    public GoodsInfo() {
    }

    private static final long serialVersionUID = 966723926915021846L;

    public enum StorageConditions {
        roomTemperature,//常温
        refrigeration,//冷藏
        frozen            //冰冻
    }

    public enum WeightUnit {
        g,
        kg
    }

    public enum VolumeUnit {
        cubicCentimeters,//立方厘米
        cubicMeter   //立方米
    }

    public enum Nature {
        gas,//气体
        solid,//固体
        liquid//液体
    }

    public enum Unit {
        box,//箱
        bottle,//瓶
        bag,//袋
        frame,    //盒
        pack //包
    }

    private String sn;

    private String name;

    private String caption;

    private BigDecimal price;

    private BigDecimal marketPrice;

    private Unit unit;

    private Integer weight;

    private Long shelfLife;

    private StorageConditions storageConditions;

    private Long packagesNum;

    private WeightUnit weightUnit;

    private BigDecimal volume;

    private VolumeUnit volumeUnit;

    private Nature nature;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Long getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(Long shelfLife) {
        this.shelfLife = shelfLife;
    }

    public StorageConditions getStorageConditions() {
        return storageConditions;
    }

    public void setStorageConditions(StorageConditions storageConditions) {
        this.storageConditions = storageConditions;
    }

    public Long getPackagesNum() {
        return packagesNum;
    }

    public void setPackagesNum(Long packagesNum) {
        this.packagesNum = packagesNum;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public VolumeUnit getVolumeUnit() {
        return volumeUnit;
    }

    public void setVolumeUnit(VolumeUnit volumeUnit) {
        this.volumeUnit = volumeUnit;
    }

    public Nature getNature() {
        return nature;
    }

    public void setNature(Nature nature) {
        this.nature = nature;
    }


    @Override
    public String toString() {
        return "GoodsInfo{" +
                "sn='" + sn + '\'' +
                ", name='" + name + '\'' +
                ", caption='" + caption + '\'' +
                ", price=" + price +
                ", marketPrice=" + marketPrice +
                ", unit=" + unit +
                ", weight=" + weight +
                ", shelfLife=" + shelfLife +
                ", storageConditions=" + storageConditions +
                ", packagesNum=" + packagesNum +
                ", weightUnit=" + weightUnit +
                ", volume=" + volume +
                ", volumeUnit=" + volumeUnit +
                ", nature=" + nature +
                '}';
    }
}

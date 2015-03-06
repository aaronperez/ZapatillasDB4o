package com.aaron.zapatillasdb4o;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Aaron Perez on 20/11/2014.
 */

public class Zapatillas implements Parcelable, Comparable<Zapatillas> {
    private String modelo, caract, peso;
    private int marca;

    public Zapatillas() {
    }

    public Zapatillas(String modelo, String caract, String peso, int marca) {
        this.modelo = modelo;
        this.caract = caract;
        this.peso = peso;
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCaract() {
        return caract;
    }

    public void setCaract(String caract) {
        this.caract = caract;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public int getMarca() {
        return marca;
    }

    public void setMarca(int marca) {
        this.marca = marca;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Zapatillas)) return false;

        Zapatillas that = (Zapatillas) o;

        if (marca != that.marca) return false;
        if (!caract.equals(that.caract)) return false;
        if (!modelo.equals(that.modelo)) return false;
        if (!peso.equals(that.peso)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = modelo.hashCode();
        result = 31 * result + caract.hashCode();
        result = 31 * result + peso.hashCode();
        result = 31 * result + marca;
        return result;
    }

    @Override
    public int compareTo(Zapatillas zapatillas) {
        return this.getModelo().toLowerCase().compareTo(zapatillas.getModelo().toLowerCase());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(modelo);
        dest.writeString(caract);
        dest.writeString(peso);
        dest.writeInt(marca);
    }

    public Zapatillas(Parcel parcel){
        this.modelo = parcel.readString();
        this.caract = parcel.readString();
        this.peso = parcel.readString();
        this.marca =parcel.readInt();
    }

    public static final Creator<Zapatillas> CREATOR =
            new Creator<Zapatillas>() {
                public Zapatillas createFromParcel(Parcel parcel) {
                    return new Zapatillas(parcel);
                }

                public Zapatillas[] newArray(int size) {
                    return new Zapatillas[size];
                }
            };

}
package org.sopac.web.rest;

import java.util.Objects;

public class CountryCount {

    String country;
    int count;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CountryCount that = (CountryCount) o;
        return count == that.count &&
            Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {

        return Objects.hash(country, count);
    }
}

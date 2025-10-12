package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shipping_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRate extends IdBasedEntity
{
    private float rate;

    private int days;

    @Column(name = "cod_supported")
    private boolean codSupported;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(nullable = false, length = 45)
    private String state;

    @Override
    public String toString() {
        return "ShippingRate [id=" + id + ", rate=" + rate + ", days=" + days + ", codSupported=" + codSupported
                + ", country=" + country.getName() + ", state=" + state + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;

        result = prime * result + ((id == null) ? 0 : id.hashCode());

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }

        ShippingRate other = (ShippingRate) obj;
        if (id == null)
        {
            return other.id == null;
        }
        else
        {
            return id.equals(other.id);
        }
    }

}

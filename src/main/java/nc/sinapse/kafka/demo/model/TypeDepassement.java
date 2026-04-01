package nc.sinapse.kafka.demo.model;

import lombok.Getter;

@Getter
public enum TypeDepassement {
    /** Mesure inférieure à la valeur minimale autorisée. */
    SEUIL_MIN,
    /** Mesure supérieure à la valeur maximale autorisée. */
    SEUIL_MAX
}

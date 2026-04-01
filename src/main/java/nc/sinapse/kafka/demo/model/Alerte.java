package nc.sinapse.kafka.demo.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Alerte {
    public UUID uuid;
    public Long version;
    public LocalDateTime horodatage;
    public TypeDepassement typeDepassement;
    public Double valeurMesuree;
    public Double seuilDeclenche;
    public UUID capteurId;
    public UUID mesureId;
}

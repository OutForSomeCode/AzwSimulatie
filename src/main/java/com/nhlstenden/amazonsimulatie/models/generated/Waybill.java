
package com.nhlstenden.amazonsimulatie.models.generated;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "destination",
    "status",
    "racks"
})
public class Waybill {

    @JsonProperty("id")
    private String id;
    @JsonProperty("destination")
    private Waybill.Destination destination;
    @JsonProperty("status")
    private Waybill.Status status;
    @JsonProperty("racks")
    private List<String> racks = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("destination")
    public Waybill.Destination getDestination() {
        return destination;
    }

    @JsonProperty("destination")
    public void setDestination(Waybill.Destination destination) {
        this.destination = destination;
    }

    @JsonProperty("status")
    public Waybill.Status getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Waybill.Status status) {
        this.status = status;
    }

    @JsonProperty("racks")
    public List<String> getRacks() {
        return racks;
    }

    @JsonProperty("racks")
    public void setRacks(List<String> racks) {
        this.racks = racks;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Waybill.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("destination");
        sb.append('=');
        sb.append(((this.destination == null)?"<null>":this.destination));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("racks");
        sb.append('=');
        sb.append(((this.racks == null)?"<null>":this.racks));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.destination == null)? 0 :this.destination.hashCode()));
        result = ((result* 31)+((this.racks == null)? 0 :this.racks.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Waybill) == false) {
            return false;
        }
        Waybill rhs = ((Waybill) other);
        return ((((((this.destination == rhs.destination)||((this.destination!= null)&&this.destination.equals(rhs.destination)))&&((this.racks == rhs.racks)||((this.racks!= null)&&this.racks.equals(rhs.racks))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

    public enum Destination {

        WAREHOUSE("WAREHOUSE"),
        MELKFACTORY("MELKFACTORY");
        private final String value;
        private final static Map<String, Waybill.Destination> CONSTANTS = new HashMap<String, Waybill.Destination>();

        static {
            for (Waybill.Destination c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Destination(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Waybill.Destination fromValue(String value) {
            Waybill.Destination constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Status {

        UNRESOLVED("UNRESOLVED"),
        RESOLVING("RESOLVING"),
        RESOLVED("RESOLVED");
        private final String value;
        private final static Map<String, Waybill.Status> CONSTANTS = new HashMap<String, Waybill.Status>();

        static {
            for (Waybill.Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Status(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static Waybill.Status fromValue(String value) {
            Waybill.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}

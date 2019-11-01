
package com.nhlstenden.amazonsimulatie.models.generated;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * NOTE: XY pos are used for parking pos
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rack",
    "waybill",
    "wkt",
    "status"
})
public class Robot
    extends Object3D
{

    @JsonProperty("rack")
    private Rack rack;
    @JsonProperty("waybill")
    private Waybill waybill;
    /**
     * pos
     * 
     */
    @JsonProperty("wkt")
    @JsonPropertyDescription("pos")
    private String wkt;
    /**
     * this determines the state of the object of the robot
     * 
     */
    @JsonProperty("status")
    @JsonPropertyDescription("this determines the state of the object of the robot")
    private Robot.Status status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("rack")
    public Rack getRack() {
        return rack;
    }

    @JsonProperty("rack")
    public void setRack(Rack rack) {
        this.rack = rack;
    }

    @JsonProperty("waybill")
    public Waybill getWaybill() {
        return waybill;
    }

    @JsonProperty("waybill")
    public void setWaybill(Waybill waybill) {
        this.waybill = waybill;
    }

    /**
     * pos
     * 
     */
    @JsonProperty("wkt")
    public String getWkt() {
        return wkt;
    }

    /**
     * pos
     * 
     */
    @JsonProperty("wkt")
    public void setWkt(String wkt) {
        this.wkt = wkt;
    }

    /**
     * this determines the state of the object of the robot
     * 
     */
    @JsonProperty("status")
    public Robot.Status getStatus() {
        return status;
    }

    /**
     * this determines the state of the object of the robot
     * 
     */
    @JsonProperty("status")
    public void setStatus(Robot.Status status) {
        this.status = status;
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
        sb.append(Robot.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        int baseLength = sb.length();
        String superString = super.toString();
        if (superString!= null) {
            int contentStart = superString.indexOf('[');
            int contentEnd = superString.lastIndexOf(']');
            if ((contentStart >= 0)&&(contentEnd >contentStart)) {
                sb.append(superString, (contentStart + 1), contentEnd);
            } else {
                sb.append(superString);
            }
        }
        if (sb.length()>baseLength) {
            sb.append(',');
        }
        sb.append("rack");
        sb.append('=');
        sb.append(((this.rack == null)?"<null>":this.rack));
        sb.append(',');
        sb.append("waybill");
        sb.append('=');
        sb.append(((this.waybill == null)?"<null>":this.waybill));
        sb.append(',');
        sb.append("wkt");
        sb.append('=');
        sb.append(((this.wkt == null)?"<null>":this.wkt));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
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
        result = ((result* 31)+((this.wkt == null)? 0 :this.wkt.hashCode()));
        result = ((result* 31)+((this.rack == null)? 0 :this.rack.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.waybill == null)? 0 :this.waybill.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        result = ((result* 31)+ super.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Robot) == false) {
            return false;
        }
        Robot rhs = ((Robot) other);
        return (((((super.equals(rhs)&&((this.wkt == rhs.wkt)||((this.wkt!= null)&&this.wkt.equals(rhs.wkt))))&&((this.rack == rhs.rack)||((this.rack!= null)&&this.rack.equals(rhs.rack))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.waybill == rhs.waybill)||((this.waybill!= null)&&this.waybill.equals(rhs.waybill))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

    public enum Status {

        IDLE("IDLE"),
        WORKING("WORKING");
        private final String value;
        private final static Map<String, Robot.Status> CONSTANTS = new HashMap<String, Robot.Status>();

        static {
            for (Robot.Status c: values()) {
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
        public static Robot.Status fromValue(String value) {
            Robot.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}


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
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "loadingBay",
    "destination",
    "status",
    "racksAmount",
    "racksType",
    "todoList",
    "racks"
})
public class Waybill
    extends Object3D
{

    /**
     * this is the id of the waybill
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("this is the id of the waybill")
    private String id;
    /**
     * which loading bay the cargo needs to be dropped
     * 
     */
    @JsonProperty("loadingBay")
    @JsonPropertyDescription("which loading bay the cargo needs to be dropped")
    private int loadingBay;
    /**
     * this reference to the destination of the waybill , where do they need to go
     * 
     */
    @JsonProperty("destination")
    @JsonPropertyDescription("this reference to the destination of the waybill , where do they need to go")
    private Waybill.Destination destination;
    /**
     * the deference types of state it can have
     * 
     */
    @JsonProperty("status")
    @JsonPropertyDescription("the deference types of state it can have")
    private Waybill.Status status;
    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racksAmount")
    @JsonPropertyDescription("what types of racks it needs to ship to the warehouse")
    private int racksAmount;
    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racksType")
    @JsonPropertyDescription("what types of racks it needs to ship to the warehouse")
    private String racksType;
    /**
     * the array of racks that are not on the right location
     * 
     */
    @JsonProperty("todoList")
    @JsonPropertyDescription("the array of racks that are not on the right location")
    private List<String> todoList = new ArrayList<String>();
    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racks")
    @JsonPropertyDescription("what types of racks it needs to ship to the warehouse")
    private List<String> racks = new ArrayList<String>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * this is the id of the waybill
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * this is the id of the waybill
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * which loading bay the cargo needs to be dropped
     * 
     */
    @JsonProperty("loadingBay")
    public int getLoadingBay() {
        return loadingBay;
    }

    /**
     * which loading bay the cargo needs to be dropped
     * 
     */
    @JsonProperty("loadingBay")
    public void setLoadingBay(int loadingBay) {
        this.loadingBay = loadingBay;
    }

    /**
     * this reference to the destination of the waybill , where do they need to go
     * 
     */
    @JsonProperty("destination")
    public Waybill.Destination getDestination() {
        return destination;
    }

    /**
     * this reference to the destination of the waybill , where do they need to go
     * 
     */
    @JsonProperty("destination")
    public void setDestination(Waybill.Destination destination) {
        this.destination = destination;
    }

    /**
     * the deference types of state it can have
     * 
     */
    @JsonProperty("status")
    public Waybill.Status getStatus() {
        return status;
    }

    /**
     * the deference types of state it can have
     * 
     */
    @JsonProperty("status")
    public void setStatus(Waybill.Status status) {
        this.status = status;
    }

    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racksAmount")
    public int getRacksAmount() {
        return racksAmount;
    }

    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racksAmount")
    public void setRacksAmount(int racksAmount) {
        this.racksAmount = racksAmount;
    }

    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racksType")
    public String getRacksType() {
        return racksType;
    }

    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racksType")
    public void setRacksType(String racksType) {
        this.racksType = racksType;
    }

    /**
     * the array of racks that are not on the right location
     * 
     */
    @JsonProperty("todoList")
    public List<String> getTodoList() {
        return todoList;
    }

    /**
     * the array of racks that are not on the right location
     * 
     */
    @JsonProperty("todoList")
    public void setTodoList(List<String> todoList) {
        this.todoList = todoList;
    }

    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
    @JsonProperty("racks")
    public List<String> getRacks() {
        return racks;
    }

    /**
     * what types of racks it needs to ship to the warehouse
     * 
     */
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
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("loadingBay");
        sb.append('=');
        sb.append(this.loadingBay);
        sb.append(',');
        sb.append("destination");
        sb.append('=');
        sb.append(((this.destination == null)?"<null>":this.destination));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("racksAmount");
        sb.append('=');
        sb.append(this.racksAmount);
        sb.append(',');
        sb.append("racksType");
        sb.append('=');
        sb.append(((this.racksType == null)?"<null>":this.racksType));
        sb.append(',');
        sb.append("todoList");
        sb.append('=');
        sb.append(((this.todoList == null)?"<null>":this.todoList));
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
        result = ((result* 31)+((this.racksType == null)? 0 :this.racksType.hashCode()));
        result = ((result* 31)+((this.todoList == null)? 0 :this.todoList.hashCode()));
        result = ((result* 31)+((this.destination == null)? 0 :this.destination.hashCode()));
        result = ((result* 31)+((this.racks == null)? 0 :this.racks.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+ this.loadingBay);
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        result = ((result* 31)+ this.racksAmount);
        result = ((result* 31)+ super.hashCode());
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
        return (((((((((super.equals(rhs)&&((this.racksType == rhs.racksType)||((this.racksType!= null)&&this.racksType.equals(rhs.racksType))))&&((this.todoList == rhs.todoList)||((this.todoList!= null)&&this.todoList.equals(rhs.todoList))))&&((this.destination == rhs.destination)||((this.destination!= null)&&this.destination.equals(rhs.destination))))&&((this.racks == rhs.racks)||((this.racks!= null)&&this.racks.equals(rhs.racks))))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&(this.loadingBay == rhs.loadingBay))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))))&&(this.racksAmount == rhs.racksAmount));
    }

    public enum Destination {

        WAREHOUSE("WAREHOUSE"),
        MILKFACTORY("MILKFACTORY");
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

        POOLED("POOLED"),
        MOVING("MOVING"),
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

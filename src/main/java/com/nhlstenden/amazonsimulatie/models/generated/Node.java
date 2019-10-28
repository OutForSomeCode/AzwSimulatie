
package com.nhlstenden.amazonsimulatie.models.generated;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "gridX",
    "gridY",
    "occupied"
})
public class Node {

    @JsonProperty("gridX")
    private int gridX;
    @JsonProperty("gridY")
    private int gridY;
    @JsonProperty("occupied")
    private boolean occupied;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("gridX")
    public int getGridX() {
        return gridX;
    }

    @JsonProperty("gridX")
    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    @JsonProperty("gridY")
    public int getGridY() {
        return gridY;
    }

    @JsonProperty("gridY")
    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    @JsonProperty("occupied")
    public boolean isOccupied() {
        return occupied;
    }

    @JsonProperty("occupied")
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
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
        sb.append(Node.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("gridX");
        sb.append('=');
        sb.append(this.gridX);
        sb.append(',');
        sb.append("gridY");
        sb.append('=');
        sb.append(this.gridY);
        sb.append(',');
        sb.append("occupied");
        sb.append('=');
        sb.append(this.occupied);
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
        result = ((result* 31)+ this.gridX);
        result = ((result* 31)+ this.gridY);
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+(this.occupied? 1 : 0));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Node) == false) {
            return false;
        }
        Node rhs = ((Node) other);
        return ((((this.gridX == rhs.gridX)&&(this.gridY == rhs.gridY))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&(this.occupied == rhs.occupied));
    }

}

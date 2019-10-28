
package com.nhlstenden.amazonsimulatie.models.generated;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "x",
    "y",
    "z",
    "rotationX",
    "rotationY",
    "rotationZ"
})
public class Object3D {

    /**
     * dit is de object3D id
     * 
     */
    @JsonProperty("id")
    @JsonPropertyDescription("dit is de object3D id")
    private String id;
    /**
     * this is the x coordinate of object3D
     * 
     */
    @JsonProperty("x")
    @JsonPropertyDescription("this is the x coordinate of object3D")
    private int x;
    /**
     * this is the y coordinate of object3D
     * 
     */
    @JsonProperty("y")
    @JsonPropertyDescription("this is the y coordinate of object3D")
    private int y;
    /**
     * this is the z coordinate of object3D
     * 
     */
    @JsonProperty("z")
    @JsonPropertyDescription("this is the z coordinate of object3D")
    private int z;
    /**
     * this is the rotation of the x coordinate of object3D
     * 
     */
    @JsonProperty("rotationX")
    @JsonPropertyDescription("this is the rotation of the x coordinate of object3D")
    private int rotationX;
    /**
     * this is the rotation of the y coordinate of object3D
     * 
     */
    @JsonProperty("rotationY")
    @JsonPropertyDescription("this is the rotation of the y coordinate of object3D")
    private int rotationY;
    /**
     * this is the rotation of the z coordinate of object3D
     * 
     */
    @JsonProperty("rotationZ")
    @JsonPropertyDescription("this is the rotation of the z coordinate of object3D")
    private int rotationZ;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * dit is de object3D id
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * dit is de object3D id
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * this is the x coordinate of object3D
     * 
     */
    @JsonProperty("x")
    public int getX() {
        return x;
    }

    /**
     * this is the x coordinate of object3D
     * 
     */
    @JsonProperty("x")
    public void setX(int x) {
        this.x = x;
    }

    /**
     * this is the y coordinate of object3D
     * 
     */
    @JsonProperty("y")
    public int getY() {
        return y;
    }

    /**
     * this is the y coordinate of object3D
     * 
     */
    @JsonProperty("y")
    public void setY(int y) {
        this.y = y;
    }

    /**
     * this is the z coordinate of object3D
     * 
     */
    @JsonProperty("z")
    public int getZ() {
        return z;
    }

    /**
     * this is the z coordinate of object3D
     * 
     */
    @JsonProperty("z")
    public void setZ(int z) {
        this.z = z;
    }

    /**
     * this is the rotation of the x coordinate of object3D
     * 
     */
    @JsonProperty("rotationX")
    public int getRotationX() {
        return rotationX;
    }

    /**
     * this is the rotation of the x coordinate of object3D
     * 
     */
    @JsonProperty("rotationX")
    public void setRotationX(int rotationX) {
        this.rotationX = rotationX;
    }

    /**
     * this is the rotation of the y coordinate of object3D
     * 
     */
    @JsonProperty("rotationY")
    public int getRotationY() {
        return rotationY;
    }

    /**
     * this is the rotation of the y coordinate of object3D
     * 
     */
    @JsonProperty("rotationY")
    public void setRotationY(int rotationY) {
        this.rotationY = rotationY;
    }

    /**
     * this is the rotation of the z coordinate of object3D
     * 
     */
    @JsonProperty("rotationZ")
    public int getRotationZ() {
        return rotationZ;
    }

    /**
     * this is the rotation of the z coordinate of object3D
     * 
     */
    @JsonProperty("rotationZ")
    public void setRotationZ(int rotationZ) {
        this.rotationZ = rotationZ;
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
        sb.append(Object3D.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("x");
        sb.append('=');
        sb.append(this.x);
        sb.append(',');
        sb.append("y");
        sb.append('=');
        sb.append(this.y);
        sb.append(',');
        sb.append("z");
        sb.append('=');
        sb.append(this.z);
        sb.append(',');
        sb.append("rotationX");
        sb.append('=');
        sb.append(this.rotationX);
        sb.append(',');
        sb.append("rotationY");
        sb.append('=');
        sb.append(this.rotationY);
        sb.append(',');
        sb.append("rotationZ");
        sb.append('=');
        sb.append(this.rotationZ);
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
        result = ((result* 31)+ this.rotationX);
        result = ((result* 31)+ this.rotationY);
        result = ((result* 31)+ this.rotationZ);
        result = ((result* 31)+ this.x);
        result = ((result* 31)+ this.y);
        result = ((result* 31)+ this.z);
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Object3D) == false) {
            return false;
        }
        Object3D rhs = ((Object3D) other);
        return ((((((((this.rotationX == rhs.rotationX)&&(this.rotationY == rhs.rotationY))&&(this.rotationZ == rhs.rotationZ))&&(this.x == rhs.x))&&(this.y == rhs.y))&&(this.z == rhs.z))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))));
    }

}

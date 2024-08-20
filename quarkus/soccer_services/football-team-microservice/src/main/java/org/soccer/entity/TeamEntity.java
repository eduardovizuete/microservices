package org.soccer.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;

import java.util.Objects;
import java.util.Set;

public class TeamEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    public ObjectId id;

    private String name;

    private String city;

    private Integer footballManagerId;

    private Set<Integer> footballPlayersId;

    public TeamEntity() {
    }

    public TeamEntity(ObjectId id, String name, String city, String name1, String city1, Integer footballManagerId, Set<Integer> footballPlayersId) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.footballManagerId = footballManagerId;
        this.footballPlayersId = footballPlayersId;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getFootballManagerId() {
        return footballManagerId;
    }

    public void setFootballManagerId(Integer footballManagerId) {
        this.footballManagerId = footballManagerId;
    }

    public Set<Integer> getFootballPlayersId() {
        return footballPlayersId;
    }

    public void setFootballPlayersId(Set<Integer> footballPlayersId) {
        this.footballPlayersId = footballPlayersId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamEntity that = (TeamEntity) o;

        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(city, that.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, city);
    }

    @Override
    public String toString() {
        return "TeamEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", footballManagerId=" + footballManagerId +
                ", footballPlayersId=" + footballPlayersId +
                '}';
    }

}

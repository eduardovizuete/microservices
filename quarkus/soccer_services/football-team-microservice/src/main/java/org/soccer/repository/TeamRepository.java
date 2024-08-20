package org.soccer.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.soccer.entity.TeamEntity;

import java.util.List;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.inc;

@ApplicationScoped
public class TeamRepository {

    private final MongoClient mongoClient;
    private final MongoCollection<TeamEntity> teamsCollection;

    public TeamRepository(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
        this.teamsCollection = mongoClient.getDatabase("football_team_registry").getCollection("teams", TeamEntity.class);
    }

    public String add(TeamEntity team) {
        return teamsCollection
                .insertOne(team)
                .getInsertedId()
                .asObjectId()
                .getValue()
                .toHexString();
    }

    public List<TeamEntity> getTeams() {
        return teamsCollection.find().into(new ArrayList<>());
    }

    public void updateTeam(String id, TeamEntity team) {
        Bson filter = eq("_id", new ObjectId(id));
        Bson newTeam = toDocument(team);
        teamsCollection.updateOne(filter, new Document("$set", newTeam));
    }

    public long deleteTeam(String id) {
        Bson filter = eq("_id", new ObjectId(id));
        return teamsCollection.deleteOne(filter).getDeletedCount();
    }

    /**
     * This method converts Team POJOs to MongoDB Documents, normally we would
     * place this in a DAO
     *
     * @param team
     * @return
     */
    private Document toDocument(TeamEntity team) {
        return new Document()
                .append("name", team.getName())
                .append("city", team.getCity())
                .append("footballManagerId", team.getFootballManagerId())
                .append("footballPlayersId", team.getFootballPlayersId());
    }

}

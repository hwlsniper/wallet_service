package it.etoken.component.eosblock.mongo.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.BasicDBObject;

@Document(collection="ram_large_user_rank")
public class RamLargeRank  extends BasicDBObject implements Serializable {

	private static final long serialVersionUID = 1L;

}

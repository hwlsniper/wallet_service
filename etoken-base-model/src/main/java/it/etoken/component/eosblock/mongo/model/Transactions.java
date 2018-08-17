package it.etoken.component.eosblock.mongo.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.BasicDBObject;

@Document(collection="transactions")
public class Transactions  extends BasicDBObject implements Serializable {

	private static final long serialVersionUID = 1L;

}

package it.etoken.component.eosblock.mongo.model;

import java.io.Serializable;

import org.springframework.data.mongodb.core.mapping.Document;

import com.mongodb.BasicDBObject;

@Document(collection="actions")
//@CompoundIndexes({
//    @CompoundIndex(name = "idx_username", def = "{'username': 1, 'password': -1}")
//})
public class EOSAction extends BasicDBObject implements Serializable {
//	/**
//	 * 
//	 */
	private static final long serialVersionUID = 1L;
}

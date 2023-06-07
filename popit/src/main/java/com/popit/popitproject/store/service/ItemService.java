package com.popit.popitproject.store.service;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.popit.popitproject.store.Sequence.StoreBoardSequenceGeneratorService;
import com.popit.popitproject.store.model.Item;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

  private static final String COLLECTION = "item";
  private final MongoDatabase mongoDatabase;
  private final StoreBoardSequenceGeneratorService storeBoardSequenceGeneratorService;





  public void addItem(Item item) {

    MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION);
    org.bson.Document document = new Document();


    document.append("_id", storeBoardSequenceGeneratorService.generateSequence(item.getId()));
    document.append("itemName", item.getItemName());
    document.append("price", item.getPrice());
    document.append("itemDetail", item.getItemDetail());
    document.append("stock", item.getStock());
    document.append("itemStatus", item.getItemStatus());
    document.append("registerDt", LocalDateTime.now());
    document.append("changeDt", LocalDateTime.now());
    document.append("postCnt", item.getPostCnt());
    document.append("likeCnt", item.getLikeCnt());

    collection.insertOne(document);

  }

  public void deleteItem(Long id){
    MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION);


    Bson query = eq("_id", id);
    collection.deleteOne(query);

    System.out.println(id + "상품을 삭제 하였습니다.");
  }

  public void updateItem(Item item, Long id) {
    MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION);

    Bson query = eq("_id", id);
    Bson updates = Updates.combine(
        Updates.set("itemName", item.getItemName()),
        Updates.set("price", item.getPrice()),
        Updates.set("itemDetail", item.getItemDetail()),
        Updates.set("stock", item.getStock()),
        Updates.set("changeDt", LocalDateTime.now()));

    collection.updateOne(query, updates);
    System.out.println("수정이 완료되었습니다. ");
  }

  public void findItem(Long id){
    MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION);
    Document doc = collection.find(eq("_id", id)).first();
    System.out.println(doc);
  }

}

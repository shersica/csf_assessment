package vttp.batch4.csf.ecommerce.controllers;


import java.io.StringReader;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.batch4.csf.ecommerce.models.Cart;
import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;
import vttp.batch4.csf.ecommerce.services.PurchaseOrderService;

@Controller
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class OrderController {

  @Autowired
  private PurchaseOrderService poSvc;

  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  @PostMapping(path = "/order", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<String> postOrder(@RequestBody String payload) {

    // TODO Task 3

    JsonReader reader = Json.createReader(new StringReader(payload));
    JsonObject jsonObject = reader.readObject();

    Order order = new Order();
    Cart cart = new Cart();
    List<LineItem> lineItems = new LinkedList<>();
    
    order.setName(jsonObject.getString("name"));
    order.setAddress(jsonObject.getString("address"));
    order.setPriority(jsonObject.getBoolean("priority"));
    order.setComments(jsonObject.getString("comments"));

    JsonArray array = jsonObject.getJsonObject("cart").getJsonArray("lineItems");
    for(JsonValue j : array){
      LineItem lineItem = new LineItem();
      lineItem.setProductId(j.asJsonObject().getString("prodId"));
      lineItem.setName(j.asJsonObject().getString("name"));
      lineItem.setPrice(j.asJsonObject().getJsonNumber("price").longValue());
      lineItem.setQuantity(j.asJsonObject().getInt("quantity"));
      lineItems.add(lineItem);
    }
    cart.setLineItems(lineItems);
    order.setCart(cart);
    String orderId = order.getOrderId();

    try {
      poSvc.createNewPurchaseOrder(order);

      JsonObject success = Json.createObjectBuilder()
      .add("orderId", orderId)
      .build();

	    return ResponseEntity.ok(success.toString());
    } catch (Exception e) {
      JsonObject error = Json.createObjectBuilder()
      .add("message", "fail to post order")
      .build();

	    return ResponseEntity.ok(error.toString());
    }

  }
}

package vttp.batch4.csf.ecommerce.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch4.csf.ecommerce.exception.OrderException;
import vttp.batch4.csf.ecommerce.models.LineItem;
import vttp.batch4.csf.ecommerce.models.Order;

@Repository
public class PurchaseOrderRepository {

  @Autowired
  private JdbcTemplate template;

  public static final String SQL_INSERT_ORDER = """
    insert into orders(order_id, date, name, address, priority, comments)
       values (?, ?, ?, ?, ?, ?)
  """;

  public static final String SQL_INSERT_LINE_ITEM = """
    insert into line_item(prod_id, name, quantity, order_id)
       values (?, ?, ?, ?)
  """;


  // IMPORTANT: DO NOT MODIFY THIS METHOD.
  // If this method is changed, any assessment task relying on this method will
  // not be marked
  // You may only add Exception to the method's signature
  public void create(Order order) throws OrderException {
    // TODO Task 3

    template.update(SQL_INSERT_ORDER, order.getOrderId(), order.getDate(), order.getName(), order.getAddress(),order.getPriority(),order.getComments());
    
    List<LineItem> lineItems = order.getCart().getLineItems();
    for(LineItem li: lineItems){
      template.update(SQL_INSERT_LINE_ITEM, li.getProductId(), li.getName(),li.getQuantity(), order.getOrderId());
    }
  }
}

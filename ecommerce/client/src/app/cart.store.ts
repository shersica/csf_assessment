
// TODO Task 2

import { LineItem} from "./models";
import Dexie from "dexie";
import { Subject } from "rxjs";
  
const PRODUCTS = 'products'

// Use the following class to implement your store
export class CartStore extends Dexie{

    private product!: Dexie.Table<LineItem, string>

    onEntries = new Subject<LineItem[]>
  
    constructor() {
      super('cart') //dbname
      this.version(1).stores({
          //collection name
        [PRODUCTS]: '++orderId, prodId, quantity, name, price'
      })
      this.product = this.table(PRODUCTS)
      this.getAllProducts().then(
        (result) => this.onEntries.next(result)
      )
    }

    add(item: LineItem): Promise<any> {
        return this.product.add(item)
            .then(pk => { 
                console.log('pk: ', pk)
                return this.getAllProducts()
            })
            .then(result => this.onEntries.next(result))
      }
  
    getAllProducts(): Promise<LineItem[]> {
        return this.product.toArray()
    }
    

    

    
}

import { Component, OnInit, inject } from '@angular/core';
import {Observable, Subscription} from 'rxjs';
import {Router} from '@angular/router';
import { LineItem } from './models';
import { CartStore } from './cart.store';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {

  // NOTE: you are free to modify this component

  private router = inject(Router)
  private cartStore = inject(CartStore)

  itemCount = 0
  entries$!: Observable<LineItem[]>
  sub$!: Subscription
  products: string[] = []

  ngOnInit(): void {
    this.entries$ = this.cartStore.onEntries.asObservable()
    this.sub$ = this.cartStore.onEntries.asObservable().subscribe({
      next: (entries) => entries.forEach(li => {
        if(!this.products.includes(li.name)){
          this.itemCount += li.quantity
          this.products.push(li.name)
        }
      })
    })
  }

  checkout(): void {
    if(this.itemCount == 0 || this.itemCount == null){
      alert('Your cart is empty.')
    } else{
      this.router.navigate([ '/checkout' ])
    }
  }
}

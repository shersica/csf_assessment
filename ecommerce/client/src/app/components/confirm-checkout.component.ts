import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CartStore } from '../cart.store';
import { Observable, Subscription } from 'rxjs';
import { Cart, LineItem, Order } from '../models';
import { CommonModule } from '@angular/common';
import { ProductService } from '../product.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrl: './confirm-checkout.component.css'
})
export class ConfirmCheckoutComponent implements OnInit {

  // TODO Task 3

  private fb = inject(FormBuilder)
  private cartStore = inject(CartStore)
  private productSvc = inject(ProductService)
  private router = inject(Router)

  form!: FormGroup
  // items$!: Observable<LineItem[]>
  items: LineItem[] = []
  total = 0
  sub$!: Subscription

  ngOnInit(): void {
    this.form = this.createForm()
    this.cartStore.getAllProducts()
      .then((value) => {return this.items = value})
      .then((value) => value.forEach(v => this.total += v.price*v.quantity))
    
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [Validators.required]),
      address: this.fb.control<string>('', [Validators.required, Validators.minLength(3)]),
      priority: this.fb.control<boolean>(false),
      comments: this.fb.control<string>('')
    })
  }

  process(){
    const order: Order = this.form.value
    const cart: Cart = { lineItems: this.items}
    order.cart = cart

    this.productSvc.checkout(order).then(
      (resp)=> { 
        alert(resp)
        this.router.navigate(['/'])
      }
    )
    .catch(
      err => alert(err.message)
    )
    console.log('>>>order processed: ', order)
  }
}

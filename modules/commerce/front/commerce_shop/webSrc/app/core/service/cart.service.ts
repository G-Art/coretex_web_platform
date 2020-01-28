import {EventEmitter, Injectable} from '@angular/core';

@Injectable()
export class CartService {

  updateMiniCart: EventEmitter<any>;


  constructor() { }
}

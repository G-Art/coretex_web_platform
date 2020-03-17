import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss']
})
export class PaginationComponent implements OnInit {

  @Input()
  currentPage = 0;

  @Input()
  totalPages = 0;

  // private

  constructor() { }

  ngOnInit(): void {

  }

}

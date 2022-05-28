import { Component, OnInit } from '@angular/core';
import { posts } from '../../data';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.css']
})
export class PostListComponent implements OnInit {
  posts = posts;

  constructor() { }

  ngOnInit(): void {
  }

}

import { Component, Input, OnInit } from '@angular/core';
import { Post } from '../data';

@Component({
  selector: 'app-post-single',
  templateUrl: './post-single.component.html',
  styleUrls: ['./post-single.component.css']
})
export class PostSingleComponent implements OnInit {

  @Input() post: Post = {
    id: '',
    title: '',
    thumbnail: '',
    description: ''
  }

  constructor() { }

  ngOnInit(): void {
  }

}

import { Component, Input, OnInit } from '@angular/core';
import { Post } from '../../data';

@Component({
  selector: 'app-post-snippet',
  templateUrl: './post-snippet.component.html',
  styleUrls: ['./post-snippet.component.css']
})
export class PostSnippetComponent implements OnInit {

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

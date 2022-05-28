import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Post, posts } from '../../data';

@Component({
  selector: 'app-page-post',
  templateUrl: './page-post.component.html',
  styleUrls: ['./page-post.component.css']
})
export class PagePostComponent implements OnInit {

  id: String | null = null
  post: Post = {
    id: '',
    title: '',
    thumbnail: '',
    description: ''
  }
  postFound = false

  constructor(
    private route: ActivatedRoute,
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.id = params.get('id');
      let postFound = posts.find( element => element.id == this.id)
      if (postFound == null) {
        this.postFound = false
      } else {
        this.postFound = true
        this.post = postFound
      }
    });
  }

}

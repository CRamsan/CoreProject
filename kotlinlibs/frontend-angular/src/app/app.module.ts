import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
// Pages
import { PagePostComponent } from './pages/page-post/page-post.component';
import { PageHomeComponent } from './pages/page-home/page-home.component';
import { PageNotFoundComponent } from './pages/page-not-found/page-not-found.component';
// Sections
import { HeaderComponent } from './sections/header/header.component';
import { FooterComponent } from './sections/footer/footer.component';
import { PostListComponent } from './sections/post-list/post-list.component';
import { PostSingleComponent } from './sections/post-single/post-single.component';
import { PostSnippetComponent } from './sections/post-snippet/post-snippet.component';
import { PostNotFoundComponent } from './sections/post-not-found/post-not-found.component';

@NgModule({
  declarations: [
    AppComponent,
    // Pages
    PagePostComponent,
    PageHomeComponent,
    PageNotFoundComponent,
    // Sections
    HeaderComponent,
    FooterComponent,
    PostListComponent,
    PostSingleComponent,
    PostSnippetComponent,
    PostNotFoundComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot([
      {path: '', component: PageHomeComponent},
      {path: ':id', component: PagePostComponent},
      { path: '**', component: PageNotFoundComponent },
    ]),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

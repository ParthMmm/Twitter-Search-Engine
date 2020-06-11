import { Component, OnInit } from '@angular/core';

import { Tweet } from './tweet';
import { TweetService } from './tweet-service'
import { Observable, Subject } from 'rxjs';

@Component({
  selector: 'app-searchbar',
  templateUrl: './searchbar.component.html',
  styleUrls: ['./searchbar.component.css']
})
export class SearchbarComponent implements OnInit {
  tweets: Tweet[];
//   = [
//    { id: 1, title: 'test1', body: 'test1bod', user: 'user1', location: 'usa', date: '6/9/2020' },
//    { id: 2, title: 'test2', body: 'test2bod', user: 'user2', location: 'usa', date: '6/9/2020' },
//    { id: 3, title: 'test3', body: 'test3bod', user: 'user3', location: 'usa', date: '6/9/2020' },
//    { id: 4, title: 'test4', body: 'test4bod', user: 'user4', location: 'usa', date: '6/9/2020' }
//  ];
  searchQ: string;

  constructor(private tweetService: TweetService) { }

  ngOnInit(): void {
  }
  search(q: string) {
    this.searchQ = q;
    //this.tweets = Tweet[];

    this.tweetService.getTweets(q)
      .subscribe(tweets => this.tweets = tweets);
  }

}

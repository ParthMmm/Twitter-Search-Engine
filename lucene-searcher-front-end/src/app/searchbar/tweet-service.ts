import { Injectable } from '@angular/core';
import { Tweet } from './tweet';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class TweetService {
  private url = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getTweets(q: string="Riverside"): Observable<Tweet[]> {
    return this.http.get<Tweet[]>(this.url + '/api/tweets?query=' + q);
  }
}

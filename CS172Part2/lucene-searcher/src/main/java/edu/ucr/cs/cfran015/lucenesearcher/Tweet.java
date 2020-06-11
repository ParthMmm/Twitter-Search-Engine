package edu.ucr.cs.cfran015.lucenesearcher;

// export class Tweet {
//    id: number;
//    title: string;
//    body: string;
//    user: string;
//    date: string;
//    location: string;
// }



public class Tweet {

    public int id;
    public String user;
    public String title;
    public String date;
    public String body;
    public String location;

    public Tweet(){}

    public Tweet(int id, String user, String title, String date, String body, String location) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.date = date;
        this.body = body;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }





    @Override
    public String toString() {
        return String.format("Tweet[id=%d, user=%s, title=%s, date=%s, body=%s, location=%s]", id, user, title, date, body, location);
    }
}

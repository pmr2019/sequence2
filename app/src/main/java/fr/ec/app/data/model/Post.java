package fr.ec.app.data.model;

import android.content.ContentValues;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts")
public class Post {

  @PrimaryKey private long id;
  private String title;
  private String subTitle;
  private String imageUrl;
  private String postUrl;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getPostUrl() {
    return postUrl;
  }

  public void setPostUrl(String postUrl) {
    this.postUrl = postUrl;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }


}

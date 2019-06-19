package fr.ec.app.data.api;

import com.google.gson.annotations.SerializedName;

public class PostResponse {
  @SerializedName("id")
  public int id;

  @SerializedName("name")
  public String title;

  @SerializedName("tagline")
  public String subTitle;

  public Thumbnail thumbnail;

}

package fr.ec.app.data.convertr;

import fr.ec.app.data.api.PostResponse;
import fr.ec.app.data.model.Post;
import java.util.ArrayList;
import java.util.List;

public class Converter {

  public Post from(PostResponse postResponse) {
    Post post = new Post();
    post.setId(postResponse.id);
    post.setImageUrl(postResponse.thumbnail.imageUrl);
    post.setSubTitle(postResponse.subTitle);
    post.setTitle(postResponse.title);
    post.setPostUrl(postResponse.thumbnail.imageUrl);
    return post;
  }

  public List<Post> from(List<PostResponse> postResponseList) {
    List<Post> posts = new ArrayList<>(postResponseList.size());
    for (PostResponse postResponse : postResponseList) {
      posts.add(from(postResponse));
    }
    return posts;
  }
}

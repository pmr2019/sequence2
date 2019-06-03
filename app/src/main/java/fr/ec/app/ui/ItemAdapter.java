package fr.ec.app.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import fr.ec.app.R;
import fr.ec.app.data.api.PostResponse;
import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<PostResponse> items = new ArrayList<>(0);
  private final ActionListener actionListener;

  public ItemAdapter(ActionListener actionListener) {
    this.actionListener = actionListener;
  }

  @NonNull @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater inflater = LayoutInflater.from(parent.getContext());

    if (viewType == R.layout.item_header) {
      View itemView = inflater.inflate(viewType, parent, false);

      return new LargeItemViewHolder(itemView);
    } else {
      View itemView = inflater.inflate(viewType, parent, false);

      return new ItemViewHolder(itemView);
    }
  }

  @Override public int getItemViewType(int position) {
    if (position == 0) {
      return R.layout.item_header;
    } else {
      return R.layout.item;
    }
  }

  @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    PostResponse itemData = items.get(position);
    Log.d("ItemAdapter",
        "onBindViewHolder() called with: holder = [" + holder + "], position = [" + position + "]");
    if (holder instanceof LargeItemViewHolder) {
      ((LargeItemViewHolder) holder).bind(itemData);
    } else {
      ((ItemViewHolder) holder).bind(itemData);
    }
  }

  @Override public int getItemCount() {
    return items.size();
  }

  public void show(List<PostResponse> posts) {
    items = posts;
    notifyDataSetChanged();
  }

  class LargeItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView textView;
    private final ImageView imageView;
    private final TextView subTitle;

    public LargeItemViewHolder(@NonNull View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.title);
      imageView = itemView.findViewById(R.id.image);
      subTitle = itemView.findViewById(R.id.subTitle);
    }

    public void bind(PostResponse dataForHeader) {
      textView.setText(dataForHeader.title);
      subTitle.setText(dataForHeader.subTitle);
      String url = dataForHeader.thumbnail.imageUrl;
      Picasso.get().load(url).into(imageView);
    }
  }

  class ItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView textView;
    private final ImageView imageView;

    public ItemViewHolder(@NonNull View itemView) {
      super(itemView);
      textView = itemView.findViewById(R.id.title);
      imageView = itemView.findViewById(R.id.image);

      itemView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          if (getAdapterPosition() != RecyclerView.NO_POSITION) {
            //  String data = items.get(getAdapterPosition());
            //  actionListener.onItemClicked(data);
          }
        }
      });
    }

    public void bind(PostResponse dataForHeader) {
      textView.setText(dataForHeader.title);
      String url = dataForHeader.thumbnail.imageUrl;
      Picasso.get().load(url).into(imageView);    }
  }

  interface ActionListener {
    public void onItemClicked(String data);
  }
}

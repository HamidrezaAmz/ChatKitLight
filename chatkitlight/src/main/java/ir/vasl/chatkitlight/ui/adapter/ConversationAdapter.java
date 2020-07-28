package ir.vasl.chatkitlight.ui.adapter;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.databinding.ViewConversationClientAudioBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationClientBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationClientFileBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationClientImageBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationClientVideoBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationEmptyBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationServerAudioBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationServerBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationServerFileBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationServerImageBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationServerVideoBinding;
import ir.vasl.chatkitlight.databinding.ViewConversationUnsupportedBinding;
import ir.vasl.chatkitlight.model.ConversationModel;
import ir.vasl.chatkitlight.ui.base.BaseViewHolder;
import ir.vasl.chatkitlight.ui.callback.ConversationListListener;
import ir.vasl.chatkitlight.utils.ConversationDiffCallback;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;

public class ConversationAdapter extends PagedListAdapter<ConversationModel, BaseViewHolder> implements ConversationListListener {

    private ConversationListListener conversationListListener;

    public ConversationAdapter(ConversationListListener conversationListListener) {
        super(new ConversationDiffCallback());
        this.setHasStableIds(true);
        this.conversationListListener = conversationListListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if(viewType >= 10000){
            switch (ConversationType.get(viewType - 10000)) {
                case CLIENT:
                    ViewConversationClientAudioBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_client_audio, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    ViewConversationServerAudioBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_server_audio, parent, false);
                    return new ConversationViewHolder(serverBinding);

                case EMPTY:
                    ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                    return new ConversationViewHolder(emptyBinding);

                default:
                    ViewConversationUnsupportedBinding unsupportedBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_unsupported, parent, false);
                    return new ConversationViewHolder(unsupportedBinding);
            }
        } else if(viewType >= 1000){
            switch (ConversationType.get(viewType - 1000)) {
                case CLIENT:
                    ViewConversationClientImageBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_client_image, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    ViewConversationServerImageBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_server_image, parent, false);
                    return new ConversationViewHolder(serverBinding);

                case EMPTY:
                    ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                    return new ConversationViewHolder(emptyBinding);

                default:
                    ViewConversationUnsupportedBinding unsupportedBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_unsupported, parent, false);
                    return new ConversationViewHolder(unsupportedBinding);
            }
        } else if(viewType >= 100){
            switch (ConversationType.get(viewType - 100)) {
                case CLIENT:
                    ViewConversationClientVideoBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_client_video, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    ViewConversationServerVideoBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_server_video, parent, false);
                    return new ConversationViewHolder(serverBinding);

                case EMPTY:
                    ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                    return new ConversationViewHolder(emptyBinding);

                default:
                    ViewConversationUnsupportedBinding unsupportedBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_unsupported, parent, false);
                    return new ConversationViewHolder(unsupportedBinding);
            }
        } else if (viewType >= 10){
            switch (ConversationType.get(viewType - 10)) {
                case CLIENT:
                    ViewConversationClientFileBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_client_file, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    ViewConversationServerFileBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_server_file, parent, false);
                    return new ConversationViewHolder(serverBinding);

                case EMPTY:
                    ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                    return new ConversationViewHolder(emptyBinding);

                default:
                    ViewConversationUnsupportedBinding unsupportedBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_unsupported, parent, false);
                    return new ConversationViewHolder(unsupportedBinding);
            }
        } else {
            switch (ConversationType.get(viewType)) {
                case CLIENT:
                    ViewConversationClientBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_client, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    ViewConversationServerBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_server, parent, false);
                    return new ConversationViewHolder(serverBinding);

                case EMPTY:
                    ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                    return new ConversationViewHolder(emptyBinding);

                default:
                    ViewConversationUnsupportedBinding unsupportedBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_unsupported, parent, false);
                    return new ConversationViewHolder(unsupportedBinding);
            }
        }
    }

    @Override
    public long getItemId(int position) { //todo -> must be unique
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {

        if (getCurrentList() == null || getCurrentList().snapshot().size() == 0 || getItem(position) == null)
            return;

        if (holder.getItemViewType() >= 10000) {
            switch (ConversationType.get(holder.getItemViewType() - 10000)) {
                case CLIENT: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).clientAudioBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).clientAudioBinding.setConversationListListener(this);
                    break;
                }

                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).serverAudioBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).serverAudioBinding.setConversationListListener(this);
                    break;
                }

                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;

                case UNDEFINE:
                default:
                    ((ConversationViewHolder) holder).unsupportedBinding.setConversationListListener(this);
                    break;
            }
        } else if (holder.getItemViewType() >= 1000) {
            switch (ConversationType.get(holder.getItemViewType() - 1000)) {
                case CLIENT: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).clientImageBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).clientImageBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).clientImageBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);
                    break;
                }

                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).serverImageBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).serverImageBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).serverImageBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);

                    break;
                }

                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;

                case UNDEFINE:
                default:
                    ((ConversationViewHolder) holder).unsupportedBinding.setConversationListListener(this);
                    break;
            }
        } else if (holder.getItemViewType() >= 100) {
            switch (ConversationType.get(holder.getItemViewType() - 100)) {
                case CLIENT: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).clientVideoBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).clientVideoBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).clientVideoBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);

                    break;
                }

                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).serverVideoBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).serverVideoBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).serverVideoBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);

                    break;
                }

                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;

                case UNDEFINE:
                default:
                    ((ConversationViewHolder) holder).unsupportedBinding.setConversationListListener(this);
                    break;
            }
        } else if (holder.getItemViewType() >= 10) {
            switch (ConversationType.get(holder.getItemViewType() - 10)) {
                case CLIENT: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).clientFileBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).clientFileBinding.setConversationListListener(this);
                    break;
                }

                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).serverFileBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).serverFileBinding.setConversationListListener(this);
                    break;
                }

                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;

                case UNDEFINE:
                default:
                    ((ConversationViewHolder) holder).unsupportedBinding.setConversationListListener(this);
                    break;
            }
        } else {
            switch (ConversationType.get(holder.getItemViewType())) {
                case CLIENT: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).clientTextBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).clientTextBinding.setConversationListListener(this);
                    break;
                }

                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).serverTextBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).serverTextBinding.setConversationListListener(this);
                    break;
                }

                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;

                case UNDEFINE:
                default:
                    ((ConversationViewHolder) holder).unsupportedBinding.setConversationListListener(this);
                    break;
            }
        }
    }

    @Override
    public void submitList(@Nullable PagedList<ConversationModel> pagedList) {
        super.submitList(pagedList);
    }

    @Override
    public int getItemViewType(int position) {

        if (getCurrentList() == null)
            return ConversationType.EMPTY.getValue();

        if (getCurrentList().snapshot().size() == 0)
            return ConversationType.EMPTY.getValue();

        if (getItem(position) == null)
            return ConversationType.EMPTY.getValue();

        ConversationModel model = getItem(position);
        if (model == null || getItemCount() == 0)
            return ConversationType.EMPTY.getValue();
        if(getItem(position).getFileType() != null) {
            switch (getItem(position).getFileType()) {
                case NONE:
                    return model.getConversationType().getValue();
                case DOCUMENT:
                case VIDEO:
                case IMAGE:
                case AUDIO:
                    return model.getConversationType().getValue() + model.getFileType().getValue();
            }
        }
        return model.getConversationType().getValue();
    }

    @Override
    public int getItemCount() {

        if (getCurrentList() == null || getCurrentList().snapshot().size() == 0)
            return 1;

        return getCurrentList().snapshot().size();
    }

    private class ConversationViewHolder extends BaseViewHolder {

        private ViewConversationClientBinding clientTextBinding;
        private ViewConversationClientImageBinding clientImageBinding;
        private ViewConversationClientVideoBinding clientVideoBinding;
        private ViewConversationClientAudioBinding clientAudioBinding;
        private ViewConversationClientFileBinding clientFileBinding;

        private ViewConversationServerBinding serverTextBinding;
        private ViewConversationServerImageBinding serverImageBinding;
        private ViewConversationServerVideoBinding serverVideoBinding;
        private ViewConversationServerAudioBinding serverAudioBinding;
        private ViewConversationServerFileBinding serverFileBinding;

        private ViewConversationEmptyBinding emptyBinding;
        private ViewConversationUnsupportedBinding unsupportedBinding;

        public ConversationViewHolder(@NonNull ViewConversationClientBinding clientTextBinding) {
            super(clientTextBinding.getRoot());
            this.clientTextBinding = clientTextBinding;
        }

        public ConversationViewHolder(@NonNull ViewConversationServerBinding serverTextBinding) {
            super(serverTextBinding.getRoot());
            this.serverTextBinding = serverTextBinding;
        }

        public ConversationViewHolder(@NonNull ViewConversationEmptyBinding emptyBinding) {
            super(emptyBinding.getRoot());
            this.emptyBinding = emptyBinding;
        }

        public ConversationViewHolder(@NonNull ViewConversationUnsupportedBinding unsupportedBinding) {
            super(unsupportedBinding.getRoot());
            this.unsupportedBinding = unsupportedBinding;
        }

        public ConversationViewHolder(ViewConversationClientImageBinding clientImageBinding) {
            super(clientImageBinding.getRoot());
            this.clientImageBinding = clientImageBinding;
            this.clientImageBinding.imageViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "image/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.clientImageBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }

        public ConversationViewHolder(ViewConversationClientVideoBinding clientVideoBinding) {
            super(clientVideoBinding.getRoot());
            this.clientVideoBinding = clientVideoBinding;
            this.clientVideoBinding.imageViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "video/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.clientVideoBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }

        public ConversationViewHolder(ViewConversationClientAudioBinding clientAudioBinding) {
            super(clientAudioBinding.getRoot());
            this.clientAudioBinding = clientAudioBinding;
            this.clientAudioBinding.frameLayoutFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "*/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.clientAudioBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }

        public ConversationViewHolder(ViewConversationClientFileBinding clientFileBinding) {
            super(clientFileBinding.getRoot());
            this.clientFileBinding = clientFileBinding;
            this.clientFileBinding.frameLayoutFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "*/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.clientFileBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }

        public ConversationViewHolder(ViewConversationServerImageBinding serverImageBinding) {
            super(serverImageBinding.getRoot());
            this.serverImageBinding = serverImageBinding;
            this.serverImageBinding.imageViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "image/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.serverImageBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }

        public ConversationViewHolder(ViewConversationServerVideoBinding serverVideoBinding) {
            super(serverVideoBinding.getRoot());
            this.serverVideoBinding = serverVideoBinding;
            this.serverVideoBinding.imageViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "video/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.serverVideoBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }

        public ConversationViewHolder(ViewConversationServerAudioBinding serverAudioBinding) {
            super(serverAudioBinding.getRoot());
            this.serverAudioBinding = serverAudioBinding;
            this.serverAudioBinding.frameLayoutFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "*/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.serverAudioBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }

        public ConversationViewHolder(ViewConversationServerFileBinding serverFileBinding) {
            super(serverFileBinding.getRoot());
            this.serverFileBinding = serverFileBinding;
            this.serverFileBinding.frameLayoutFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getBindingAdapterPosition() == -1)
                        return;
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                    viewIntent.setDataAndType(Uri.parse(getItem(getBindingAdapterPosition()).getFileAddress()), "*/*");
                    Intent chooserIntent = Intent.createChooser(viewIntent, "Open in...");
                    ConversationViewHolder.this.serverFileBinding.getRoot().getContext().startActivity(chooserIntent);
                }
            });
        }
    }

    @Override
    public void onConversationItemClicked(Object object) {
        if (conversationListListener != null)
            conversationListListener.onConversationItemClicked(object);
    }

}

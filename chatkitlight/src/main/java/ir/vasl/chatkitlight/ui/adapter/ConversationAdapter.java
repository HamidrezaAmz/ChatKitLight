package ir.vasl.chatkitlight.ui.adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientAudioBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientFileBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientImageBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerAudioBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerFileBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerImageBinding;
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
import ir.vasl.chatkitlight.ui.dialogs.PermissionDialog;
import ir.vasl.chatkitlight.utils.AndroidUtils;
import ir.vasl.chatkitlight.utils.ConversationDiffCallback;
import ir.vasl.chatkitlight.utils.FileHelper;
import ir.vasl.chatkitlight.utils.PermissionHelper;
import ir.vasl.chatkitlight.utils.globalEnums.ChatStyleEnum;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;
import me.itangqi.waveloadingview.WaveLoadingView;
import rm.com.audiowave.AudioWaveView;

public class ConversationAdapter extends PagedListAdapter<ConversationModel, BaseViewHolder> implements ConversationListListener {

    private ConversationListListener conversationListListener;
    private ChatStyleEnum chatStyleEnum;
    private Context context; //for permission, storage management and intent initialization
    private ThinDownloadManager downloadManager; // one dl mgr for the whole list
    private static MediaPlayer mp; //Media Player to play voices and audios
    ViewConversationClientAudioBinding globalClientAudioBinding;
    ViewConversationServerAudioBinding globalServerAudioBinding;

    public ConversationAdapter(ConversationListListener conversationListListener, ChatStyleEnum chatStyleEnum) {
        super(new ConversationDiffCallback());
        this.setHasStableIds(true);
        this.chatStyleEnum = chatStyleEnum;
        this.conversationListListener = conversationListListener;
        this.downloadManager = new ThinDownloadManager();
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (chatStyleEnum) {
            case DEFAULT:
            case ARMAN_VARZESH:
                return AvViewHolderCreator(parent, inflater, viewType);
            case LAWONE:
                return LawoneViewHolderCreator(parent, inflater, viewType);
        }
        return null; //chatStyle is non-defined? impossible
    }

    @Override
    public long getItemId(int position) { //todo -> must be unique
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if (getCurrentList() == null || getCurrentList().snapshot().size() == 0 || getItem(position) == null)
            return;

        switch (chatStyleEnum) {
            case DEFAULT:
            case ARMAN_VARZESH:
                AvBinder(holder, position);
                break;
            case LAWONE:
                LawoneBinder(holder, position);
                break;
        }
    }

    private void LawoneBinder(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() >= 10000) {
            switch (ConversationType.get(holder.getItemViewType() - 10000)) {
                case CLIENT: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).lawoneClientAudioBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneClientAudioBinding.setConversationListListener(this);
                    break;
                }

                case SERVER: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).lawoneServerAudioBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerAudioBinding.setConversationListListener(this);
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
                    ((ConversationViewHolder) holder).lawoneClientImageBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneClientImageBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).lawoneClientImageBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);
                    break;
                }
                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneServerImageBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerImageBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).lawoneServerImageBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);
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
                    ((ConversationViewHolder) holder).lawoneClientFileBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneClientFileBinding.setConversationListListener(this);
                    break;
                }
                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneServerFileBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerFileBinding.setConversationListListener(this);
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
                    ((ConversationViewHolder) holder).lawoneClientTextBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneClientTextBinding.setConversationListListener(this);
                    break;
                }
                case SERVER: {
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneServerTextBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerTextBinding.setConversationListListener(this);
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

    private void AvBinder(BaseViewHolder holder, int position) {
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
                    if (model == null)
                        return;
                    ((ConversationViewHolder) holder).clientImageBinding.imageViewImage.setImageUrlCurve(model.getFileAddress(), 12);
//                    Glide.with(context)
//                            .load(model.getFileAddress())
//                            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(((int) AndroidUtils.convertDpToPixel(6, context)))))
//                            .placeholder(R.drawable.background_global_place_holder)
//                            .into(((ConversationViewHolder) holder).clientImageBinding.imageViewImage);
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
        if (getItem(position).getFileType() != null) {
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

    @Override
    public void onConversationItemClicked(Object object) {
        if (conversationListListener != null)
            conversationListListener.onConversationItemClicked(object);
    }

    private BaseViewHolder LawoneViewHolderCreator(ViewGroup parent, LayoutInflater inflater, int viewType) {
        if (viewType >= 10000) {
            switch (ConversationType.get(viewType - 10000)) {
                case CLIENT:
                    LawoneConversationClientAudioBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_client_audio, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    LawoneConversationServerAudioBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_server_audio, parent, false);
                    return new ConversationViewHolder(serverBinding);

                case EMPTY:
                    ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                    return new ConversationViewHolder(emptyBinding);

                default:
                    ViewConversationUnsupportedBinding unsupportedBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_unsupported, parent, false);
                    return new ConversationViewHolder(unsupportedBinding);
            }
        } else if (viewType >= 1000) {
            switch (ConversationType.get(viewType - 1000)) {
                case CLIENT:
                    LawoneConversationClientImageBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_client_image, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    LawoneConversationServerImageBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_server_image, parent, false);
                    return new ConversationViewHolder(serverBinding);

                case EMPTY:
                    ViewConversationEmptyBinding emptyBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_empty, parent, false);
                    return new ConversationViewHolder(emptyBinding);

                default:
                    ViewConversationUnsupportedBinding unsupportedBinding = DataBindingUtil.inflate(inflater, R.layout.view_conversation_unsupported, parent, false);
                    return new ConversationViewHolder(unsupportedBinding);
            }
        } else if (viewType >= 100) {
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
        } else if (viewType >= 10) {
            switch (ConversationType.get(viewType - 10)) {
                case CLIENT:
                    LawoneConversationClientFileBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_client_file, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    LawoneConversationServerFileBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_server_file, parent, false);
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
                    LawoneConversationClientBinding clientBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_client, parent, false);
                    return new ConversationViewHolder(clientBinding);

                case SERVER:
                    LawoneConversationServerBinding serverBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_server, parent, false);
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

    private BaseViewHolder AvViewHolderCreator(ViewGroup parent, LayoutInflater inflater, int viewType) {
        if (viewType >= 10000) {
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
        } else if (viewType >= 1000) {
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
        } else if (viewType >= 100) {
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
        } else if (viewType >= 10) {
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

    private class ConversationViewHolder extends BaseViewHolder {
        DownloadRequest downloadRequest = null; //download request for different file types

        //DEFAULT - AV
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

        //LAWONE
        private LawoneConversationClientBinding lawoneClientTextBinding;
        private LawoneConversationClientImageBinding lawoneClientImageBinding;
        private LawoneConversationClientFileBinding lawoneClientFileBinding;
        //        private ViewConversationClientVideoBinding clientVideoBinding;
        private LawoneConversationClientAudioBinding lawoneClientAudioBinding;

        private LawoneConversationServerBinding lawoneServerTextBinding;
        private LawoneConversationServerImageBinding lawoneServerImageBinding;
        private LawoneConversationServerFileBinding lawoneServerFileBinding;
        //        private ViewConversationServerVideoBinding serverVideoBinding;
        private LawoneConversationServerAudioBinding lawoneServerAudioBinding;

        @Override
        public void onBind(int position) {
            super.onBind(position);
            if (getItem(position) == null)
                return;
            if (lawoneClientFileBinding != null) { //client file
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(position).getFileAddress()))) {
                    lawoneClientFileBinding.imageViewCheckmark.setVisibility(View.VISIBLE);
                    downloadProgressDone(lawoneClientFileBinding.waveView);
                }
            }
            if (lawoneServerFileBinding != null) { //server file
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(position).getFileAddress()))) {
                    lawoneServerFileBinding.imageViewCheckmark.setVisibility(View.VISIBLE);
                    downloadProgressDone(lawoneServerFileBinding.waveView);
                }
            }
            if (lawoneClientAudioBinding != null) { //client audio
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(position).getFileAddress()))) {
                    lawoneClientAudioBinding.imageViewCheckmark.setVisibility(View.VISIBLE);
                    downloadProgressDone(lawoneClientAudioBinding.waveView);
                    lawoneClientAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getFileAddress()));
                }
            }
            if (lawoneServerAudioBinding != null) { //server audio
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(position).getFileAddress()))) {
                    lawoneServerAudioBinding.imageViewCheckmark.setVisibility(View.VISIBLE);
                    downloadProgressDone(lawoneServerAudioBinding.waveView);
                    lawoneServerAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getFileAddress()));
                }
            }
            if (clientAudioBinding != null) { //client audio
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(position).getFileAddress()))) {
                    clientAudioBinding.imageViewCheckmark.setVisibility(View.VISIBLE);
                    downloadProgressDone(clientAudioBinding.waveView);
                    clientAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getFileAddress()));
                }
            }
            if (serverAudioBinding != null) { //server audio
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(position).getFileAddress()))) {
                    serverAudioBinding.imageViewCheckmark.setVisibility(View.VISIBLE);
                    downloadProgressDone(serverAudioBinding.waveView);
                    serverAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getFileAddress()));
                }
            }
        }

        //HELPER FUNCTIONS
        private DownloadStatusListenerV1 downloadListenerCreator(AppCompatImageView imageViewCheckmark, WaveLoadingView waveView, AudioWaveView wave) {
            return new DownloadStatusListenerV1() {
                @Override
                public void onDownloadComplete(DownloadRequest downloadRequest) {
                    imageViewCheckmark.setVisibility(View.VISIBLE);
                    waveView.setWaveColor(context.getResources().getColor(R.color.green));
                    if (wave != null) {
                        wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getFileAddress()));
                        wave.performClick();
                    }
                }

                @Override
                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                    waveView.setCenterTitle(context.getString(R.string.download));
                }

                @Override
                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                    waveView.setCenterTitle("");
                    if (progress >= 0 && progress <= 100)
                        waveView.setProgressValue(progress);
                    waveView.startAnimation();
                }
            };
        }

        private void downloadProgressDone(WaveLoadingView waveView) {
            waveView.setCenterTitle("");
            waveView.setProgressValue(100);
            waveView.setWaveColor(context.getResources().getColor(R.color.green));
        }

        private CountDownTimer getAudioSeeker(AudioWaveView wave) {
            return new CountDownTimer(1000, 10) {
                @Override
                public void onTick(long millisUntilFinished) {
                    try {
                        if (mp != null && wave != null)
                            wave.setProgress(((int) ((((float) mp.getCurrentPosition()) / ((float) mp.getDuration())) * 100)));
                    } catch (Exception e){
                        wave.setProgress(0);
                    }
                }

                @Override
                public void onFinish() {
                    if (mp.isPlaying())
                        this.start();
                }
            };
        }

        // DEFAULT - AV CONSTRUCTORS
        public ConversationViewHolder(ViewConversationClientBinding clientTextBinding) {
            super(clientTextBinding.getRoot());
            this.clientTextBinding = clientTextBinding;
        }

        public ConversationViewHolder(ViewConversationServerBinding serverTextBinding) {
            super(serverTextBinding.getRoot());
            this.serverTextBinding = serverTextBinding;
        }

        public ConversationViewHolder(ViewConversationEmptyBinding emptyBinding) {
            super(emptyBinding.getRoot());
            this.emptyBinding = emptyBinding;
        }

        public ConversationViewHolder(ViewConversationUnsupportedBinding unsupportedBinding) {
            super(unsupportedBinding.getRoot());
            this.unsupportedBinding = unsupportedBinding;
        }

        public ConversationViewHolder(ViewConversationClientImageBinding clientImageBinding) {
            super(clientImageBinding.getRoot());
            this.clientImageBinding = clientImageBinding;
            this.clientImageBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
//                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
                conversationListListener.onImageClicked(getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(ViewConversationClientVideoBinding clientVideoBinding) {
            super(clientVideoBinding.getRoot());
            this.clientVideoBinding = clientVideoBinding;
            this.clientVideoBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
//                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
                conversationListListener.onVideoClicked(getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(ViewConversationClientAudioBinding clientAudioBinding) {
            super(clientAudioBinding.getRoot());
            this.clientAudioBinding = clientAudioBinding;
            this.clientAudioBinding.setIsPlaying(false);
            this.clientAudioBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(getBindingAdapterPosition()).getFileAddress()))) {
                    if (mp != null && mp.isPlaying()) {
                        mp.stop();
                    }
                    if (this.clientAudioBinding.getIsPlaying()) {
                        if (mp != null)
                            mp.pause();
                        this.clientAudioBinding.setIsPlaying(false);
                        return;
                    }
                    mp = new MediaPlayer();
                    try {
                        if(globalClientAudioBinding != null)
                            globalClientAudioBinding.setIsPlaying(false);
                        if(globalServerAudioBinding != null)
                            globalServerAudioBinding.setIsPlaying(false);

                        globalServerAudioBinding = null;
                        globalClientAudioBinding = this.clientAudioBinding;

                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {
                            mp.start();
                            getAudioSeeker(this.clientAudioBinding.wave).start();
                            this.clientAudioBinding.setIsPlaying(true);
                        });
                        mp.setOnCompletionListener(mp -> this.clientAudioBinding.setIsPlaying(false));
                        mp.setOnErrorListener((mp, what, extra) -> {
                            this.clientAudioBinding.setIsPlaying(false);
                            return false;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            downloadListenerCreator(clientAudioBinding.imageViewCheckmark, clientAudioBinding.waveView, clientAudioBinding.wave));
                    if (downloadRequest != null)
                        downloadManager.add(downloadRequest);
                }
            });
        }

        public ConversationViewHolder(ViewConversationClientFileBinding clientFileBinding) {
            super(clientFileBinding.getRoot());
            this.clientFileBinding = clientFileBinding;
            this.clientFileBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(ViewConversationServerImageBinding serverImageBinding) {
            super(serverImageBinding.getRoot());
            this.serverImageBinding = serverImageBinding;
            this.serverImageBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
//                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
                conversationListListener.onImageClicked(getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(ViewConversationServerVideoBinding serverVideoBinding) {
            super(serverVideoBinding.getRoot());
            this.serverVideoBinding = serverVideoBinding;
            this.serverVideoBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
//                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
                conversationListListener.onVideoClicked(getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(ViewConversationServerAudioBinding serverAudioBinding) {
            super(serverAudioBinding.getRoot());
            this.serverAudioBinding = serverAudioBinding;
            this.serverAudioBinding.setIsPlaying(false);
            this.serverAudioBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(getBindingAdapterPosition()).getFileAddress()))) {
                    if (mp != null && mp.isPlaying()) {
                        mp.stop();
                    }
                    if (this.serverAudioBinding.getIsPlaying()) {
                        if (mp != null)
                            mp.pause();
                        this.serverAudioBinding.setIsPlaying(false);
                        return;
                    }
                    mp = new MediaPlayer();
                    try {
                        if(globalClientAudioBinding != null)
                            globalClientAudioBinding.setIsPlaying(false);
                        if(globalServerAudioBinding != null)
                            globalServerAudioBinding.setIsPlaying(false);
                        globalServerAudioBinding = this.serverAudioBinding;
                        globalClientAudioBinding = null;

                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {
                            mp.start();
                            getAudioSeeker(serverAudioBinding.wave).start();
                            this.serverAudioBinding.setIsPlaying(true);
                        });
                        mp.setOnCompletionListener(mp -> this.serverAudioBinding.setIsPlaying(false));
                        mp.setOnErrorListener((mp, what, extra) -> {
                            this.serverAudioBinding.setIsPlaying(false);
                            return false;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            downloadListenerCreator(serverAudioBinding.imageViewCheckmark, serverAudioBinding.waveView, serverAudioBinding.wave));
                    if (downloadRequest != null)
                        downloadManager.add(downloadRequest);
                }
            });
        }

        public ConversationViewHolder(ViewConversationServerFileBinding serverFileBinding) {
            super(serverFileBinding.getRoot());
            this.serverFileBinding = serverFileBinding;
            this.serverFileBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        //LAWONE CONSTRUCTORS
        public ConversationViewHolder(LawoneConversationClientBinding clientTextBinding) {
            super(clientTextBinding.getRoot());
            this.lawoneClientTextBinding = clientTextBinding;
        }

        public ConversationViewHolder(LawoneConversationServerBinding serverTextBinding) {
            super(serverTextBinding.getRoot());
            this.lawoneServerTextBinding = serverTextBinding;
        }

        public ConversationViewHolder(LawoneConversationClientImageBinding clientImageBinding) {
            super(clientImageBinding.getRoot());
            this.lawoneClientImageBinding = clientImageBinding;
            this.lawoneClientImageBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(LawoneConversationClientFileBinding clientFileBinding) {
            super(clientFileBinding.getRoot());
            this.lawoneClientFileBinding = clientFileBinding;
            this.lawoneClientFileBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(getBindingAdapterPosition()).getFileAddress()))) {
                    FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            downloadListenerCreator(lawoneClientFileBinding.imageViewCheckmark, lawoneClientFileBinding.waveView, null));
                    if (downloadRequest != null)
                        downloadManager.add(downloadRequest);
                }
            });
        }

        public ConversationViewHolder(LawoneConversationServerImageBinding serverImageBinding) {
            super(serverImageBinding.getRoot());
            this.lawoneServerImageBinding = serverImageBinding;
            this.lawoneServerImageBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(LawoneConversationClientAudioBinding clientAudioBinding) {
            super(clientAudioBinding.getRoot());
            this.lawoneClientAudioBinding = clientAudioBinding;
            this.lawoneClientAudioBinding.setIsPlaying(false);
            this.lawoneClientAudioBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;

                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(getBindingAdapterPosition()).getFileAddress()))) {
                    if (lawoneClientAudioBinding.getIsPlaying()) {
                        mp.pause();
                        lawoneClientAudioBinding.setIsPlaying(false);
                        return;
                    }
                    mp = new MediaPlayer();
                    try {
                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {
                            mp.start();
                            getAudioSeeker(lawoneClientAudioBinding.wave).start();
                            lawoneClientAudioBinding.setIsPlaying(true);
                        });
                        mp.setOnCompletionListener(mp -> lawoneClientAudioBinding.setIsPlaying(false));
                        mp.setOnErrorListener((mp, what, extra) -> {
                            lawoneClientAudioBinding.setIsPlaying(false);
                            return false;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            downloadListenerCreator(lawoneClientAudioBinding.imageViewCheckmark, lawoneClientAudioBinding.waveView, lawoneClientAudioBinding.wave));
                    if (downloadRequest != null)
                        downloadManager.add(downloadRequest);
                }
            });
        }

        public ConversationViewHolder(LawoneConversationServerAudioBinding serverAudioBinding) {
            super(serverAudioBinding.getRoot());
            this.lawoneServerAudioBinding = serverAudioBinding;
            this.lawoneServerAudioBinding.setIsPlaying(false);
            this.lawoneServerAudioBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(getBindingAdapterPosition()).getFileAddress()))) {
                    if (lawoneServerAudioBinding.getIsPlaying()) {
                        mp.pause();
                        lawoneServerAudioBinding.setIsPlaying(false);
                        return;
                    }
                    mp = new MediaPlayer();
                    try {
                        lawoneServerAudioBinding.imageViewPlay.setImageDrawable(context.getDrawable(R.drawable.ic_pause));
                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {
                            mp.start();
                            getAudioSeeker(lawoneServerAudioBinding.wave).start();
                            lawoneServerAudioBinding.setIsPlaying(true);
                        });
                        mp.setOnCompletionListener(mp -> lawoneServerAudioBinding.setIsPlaying(false));
                        mp.setOnErrorListener((mp, what, extra) -> {
                            lawoneServerAudioBinding.setIsPlaying(false);
                            return false;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            downloadListenerCreator(lawoneServerAudioBinding.imageViewCheckmark, lawoneServerAudioBinding.waveView, lawoneServerAudioBinding.wave));
                    if (downloadRequest != null)
                        downloadManager.add(downloadRequest);
                }
            });
        }

        public ConversationViewHolder(LawoneConversationServerFileBinding serverFileBinding) {
            super(serverFileBinding.getRoot());
            this.lawoneServerFileBinding = serverFileBinding;
            this.lawoneServerFileBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, FileHelper.getFileName(getItem(getBindingAdapterPosition()).getFileAddress()))) {
                    FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress());
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            downloadListenerCreator(lawoneServerFileBinding.imageViewCheckmark, lawoneServerFileBinding.waveView, null));
                    if (downloadRequest != null)
                        downloadManager.add(downloadRequest);
                }
            });
        }
    }
}

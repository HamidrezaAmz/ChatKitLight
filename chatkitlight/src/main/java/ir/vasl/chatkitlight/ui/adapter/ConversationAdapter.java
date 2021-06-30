package ir.vasl.chatkitlight.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;

import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import ir.vasl.chatkitlight.R;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientAudioBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientFileBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationClientImageBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerAudioBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerFileBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationServerImageBinding;
import ir.vasl.chatkitlight.databinding.LawoneConversationSystemBinding;
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
import ir.vasl.chatkitlight.utils.FileHelper;
import ir.vasl.chatkitlight.utils.PermissionHelper;
import ir.vasl.chatkitlight.utils.TimeUtils;
import ir.vasl.chatkitlight.utils.globalEnums.ChatStyleEnum;
import ir.vasl.chatkitlight.utils.globalEnums.ConversationType;
import me.itangqi.waveloadingview.WaveLoadingView;
import rm.com.audiowave.AudioWaveView;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

@SuppressWarnings("rawtypes")
public class ConversationAdapter extends PagedListAdapter<ConversationModel, BaseViewHolder> implements ConversationListListener {

    private Context context; // for permission, storage management and intent initialization
    private ConversationListListener conversationListListener;
    private ChatStyleEnum chatStyleEnum;
    private ThinDownloadManager downloadManager; // one dl mgr for the whole list
    static MediaPlayer mp; //Media Player to play voices and audios
    private int lastPlayingPos = -1;

    public ConversationAdapter(ConversationListListener conversationListListener, ChatStyleEnum chatStyleEnum) {
        super(new DiffUtil.ItemCallback<ConversationModel>() {
            @Override
            public boolean areItemsTheSame(@NonNull ConversationModel oldItem, @NonNull ConversationModel newItem) {
                return oldItem.getConversationId().equals(newItem.getConversationId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull ConversationModel oldItem, @NonNull ConversationModel newItem) {
                return oldItem.equals(newItem);
            }
        });
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
        try {
            createDateGroupIfNeeded(holder, position);
        } catch (Exception e){
            Log.e("TAG", "Cannot create date grouping " + e.getMessage()  );
            e.printStackTrace();
        }
    }

    public static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
        return fmt.format(date1).equals(fmt.format(date2));
    }

    @SuppressLint("ResourceType")
    private void createDateGroupIfNeeded(BaseViewHolder holder, int position) {

//        Log.e("TAG", "createDateGroupIfNeeded: " + position + " " + holder.getCurrentPosition());

        if (position == getItemCount() - 1) {
            return;
        }
        ConversationModel lastModel = getItem(position);
        ConversationModel nextModel = getItem(position + 1);

        if (lastModel.getTime().contains(":") || nextModel.getTime().contains(":"))
            return;

        Date lastDate = new Date(Long.parseLong(lastModel.getTime()));
        Date nextDate = new Date(Long.parseLong(nextModel.getTime()));

        if (!isSameDay(lastDate, nextDate) &&
                ((ViewGroup) holder.itemView).findViewById(255) == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = vi.inflate(R.layout.item_date, null);
            v.setId(255);
            TextView textView = (TextView) v.findViewById(R.id.textView_date);

            PersianDate pdate = new PersianDate(Long.parseLong(lastModel.getTime()));
            PersianDateFormat pdformater = new PersianDateFormat("l j F Y");

            textView.setText(pdformater.format(pdate));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            params.bottomMargin = (int) AndroidUtils.convertDpToPixel(18f, context);
            v.setLayoutParams(params);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) holder.itemView.findViewById(R.id.linearLayout_bubble).getLayoutParams();
            params2.addRule(RelativeLayout.BELOW, 255);

            ViewGroup insertPoint = ((ViewGroup) holder.itemView.getRootView());
            insertPoint.addView(v);

            holder.itemView.findViewById(R.id.linearLayout_bubble).setLayoutParams(params2);
        } else {
            if (((ViewGroup) holder.itemView).findViewById(255) != null && isSameDay(lastDate, nextDate)) {
                ((ViewGroup) holder.itemView).removeView(((ViewGroup) holder.itemView).findViewById(255));
            }
        }
    }

    private void LawoneBinder(BaseViewHolder holder, int position) {
        if (holder.getItemViewType() >= 100000) {
            ConversationModel model = getItem(position);
            ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationModel(model);
            ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationListListener(this);
        } else if (holder.getItemViewType() >= 10000) {
            switch (ConversationType.get(holder.getItemViewType() - 10000)) {
                case CLIENT: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).lawoneClientAudioBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneClientAudioBinding.setIsPlaying(model.isPlaying());
//                    getAudioSeeker(((ConversationViewHolder) holder).lawoneClientAudioBinding.wave, position).cancel();
                    ((ConversationViewHolder) holder).lawoneClientAudioBinding.wave.setProgress(0);
                    ((ConversationViewHolder) holder).lawoneClientAudioBinding.setConversationListListener(this);
                    break;
                }

                case SERVER: {
                    ConversationModel model = getItem(position);
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).lawoneServerAudioBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerAudioBinding.setIsPlaying(model.isPlaying());
//                    getAudioSeeker(((ConversationViewHolder) holder).lawoneServerAudioBinding.wave, position).cancel();
                    ((ConversationViewHolder) holder).lawoneServerAudioBinding.wave.setProgress(0);
                    ((ConversationViewHolder) holder).lawoneServerAudioBinding.setConversationListListener(this);
                    break;
                }
                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;

                case SYSTEM:
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationListListener(this);
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
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).lawoneServerImageBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerImageBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).lawoneServerImageBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);
                    break;
                }
                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;

                case SYSTEM:
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationListListener(this);
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
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).serverVideoBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).serverVideoBinding.setConversationListListener(this);
                    ((ConversationViewHolder) holder).serverVideoBinding.imageViewImage.setImageUrlCurve(getItem(position).getFileAddress(), 12);

                    break;
                }
                case SYSTEM:
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationListListener(this);
                    break;
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
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).lawoneServerFileBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerFileBinding.setConversationListListener(this);
                    break;
                }
                case SYSTEM:
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationListListener(this);
                    break;
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
                    holder.onBind(position);
                    ((ConversationViewHolder) holder).lawoneServerTextBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneServerTextBinding.setConversationListListener(this);
                    break;
                }
                case EMPTY:
                    ((ConversationViewHolder) holder).emptyBinding.setConversationListListener(this);
                    break;
                case SYSTEM:
                    ConversationModel model = getItem(position);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationModel(model);
                    ((ConversationViewHolder) holder).lawoneConversationSystemBinding.setConversationListListener(this);
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
        super.submitList(pagedList, new Runnable() {
            @Override
            public void run() {
                listSubmitted();
            }
        });
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
                case TEXT_RATE:
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
        return super.getItemCount();
//        if (getCurrentList() == null || getCurrentList().snapshot().size() == 0)
//            return 1;
//
//        return getCurrentList().snapshot().size();
    }

    @Override
    public void onConversationItemClicked(Object object) {
        if (conversationListListener != null)
            conversationListListener.onConversationItemClicked(object);
    }

    @Override
    public void systemRateClicked() {
        if (conversationListListener != null)
            conversationListListener.systemRateClicked();
    }

    @Override
    public void systemSupportClicked() {
        if (conversationListListener != null)
            conversationListListener.systemSupportClicked();
    }

    private BaseViewHolder LawoneViewHolderCreator(ViewGroup parent, LayoutInflater inflater, int viewType) {
        if (viewType >= 100000) {
            LawoneConversationSystemBinding lawoneConversationSystemBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_system, parent, false);
            return new ConversationViewHolder(lawoneConversationSystemBinding);
        } else if (viewType >= 10000) {
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
                case SYSTEM:
                    LawoneConversationSystemBinding lawoneConversationSystemBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_system, parent, false);
                    return new ConversationViewHolder(lawoneConversationSystemBinding);
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

                case SYSTEM:
                    LawoneConversationSystemBinding lawoneConversationSystemBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_system, parent, false);
                    return new ConversationViewHolder(lawoneConversationSystemBinding);

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

                case SYSTEM:
                    LawoneConversationSystemBinding lawoneConversationSystemBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_system, parent, false);
                    return new ConversationViewHolder(lawoneConversationSystemBinding);

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

                case SYSTEM:
                    LawoneConversationSystemBinding lawoneConversationSystemBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_system, parent, false);
                    return new ConversationViewHolder(lawoneConversationSystemBinding);

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

                case SYSTEM:
                    LawoneConversationSystemBinding lawoneConversationSystemBinding = DataBindingUtil.inflate(inflater, R.layout.lawone_conversation_system, parent, false);
                    return new ConversationViewHolder(lawoneConversationSystemBinding);

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

    public CountDownTimer getAudioSeeker(AudioWaveView wave, int pos) {
        return new CountDownTimer(1000, 10) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (mp != null && wave != null)
                    try {
                        if (pos == lastPlayingPos)
                            wave.setProgress(((int) ((((float) mp.getCurrentPosition()) / ((float) mp.getDuration())) * 100)));
                        else wave.setProgress(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }

            @Override
            public void onFinish() {
                try {
                    if (mp.isPlaying())
                        this.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
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
        private LawoneConversationClientAudioBinding lawoneClientAudioBinding;

        private LawoneConversationServerBinding lawoneServerTextBinding;
        private LawoneConversationServerImageBinding lawoneServerImageBinding;
        private LawoneConversationServerFileBinding lawoneServerFileBinding;
        private LawoneConversationServerAudioBinding lawoneServerAudioBinding;

        //SYSTEM
        // private SystemConversationBinding systemConversationBinding;
        private LawoneConversationSystemBinding lawoneConversationSystemBinding;

        @Override
        public void onBind(int position) {
            super.onBind(position);
            if (getItem(position) == null)
                return;

            /*if (lawoneClientTextBinding != null) {
                try {
                    if (position != -1 && getItem(position) != null && getItem(position).getImageRes().length() > 0)
                        lawoneClientTextBinding.imageViewAvatar.setImageResource(getResId(getItem(position).getImageRes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            if (lawoneServerTextBinding != null) {
                try {
                    if (position != -1 && getItem(position) != null && getItem(position).getImageRes().length() > 0)
                        lawoneServerTextBinding.imageViewAvatar.setImageResource(getResId(getItem(position).getImageRes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (lawoneClientFileBinding != null) { //client file
                if (FileHelper.checkFileExistence(context, getItem(position).getTitle())) {
                    downloadProgressDone(lawoneClientFileBinding.waveView, lawoneClientFileBinding.imageViewCheckmark, true);
                }
                try {
                    if (position != -1 && getItem(position) != null && getItem(position).getImageRes().length() > 0)
                        lawoneClientFileBinding.imageViewAvatar.setImageResource(getResId(getItem(position).getImageRes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (lawoneServerFileBinding != null) { //server file
                if (FileHelper.checkFileExistence(context, getItem(position).getTitle())) {
                    downloadProgressDone(lawoneServerFileBinding.waveView, lawoneServerFileBinding.imageViewCheckmark, true);
                }
                try {
                    if (position != -1 && getItem(position) != null && getItem(position).getImageRes().length() > 0)
                        lawoneServerFileBinding.imageViewAvatar.setImageResource(getResId(getItem(position).getImageRes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (lawoneClientAudioBinding != null) { //client audio
                if (FileHelper.checkFileExistence(context, getItem(position).getTitle())) {
                    downloadProgressDone(lawoneClientAudioBinding.waveView, null, false);
                    lawoneClientAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getTitle()));
                }
                try {
                    if (position != -1 && getItem(position) != null && getItem(position).getImageRes().length() > 0)
                        lawoneClientAudioBinding.imageViewAvatar.setImageResource(getResId(getItem(position).getImageRes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (lawoneServerAudioBinding != null) { //server audio
                if (FileHelper.checkFileExistence(context, getItem(position).getTitle())) {
                    downloadProgressDone(lawoneServerAudioBinding.waveView, null, false);
                    lawoneServerAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getTitle()));
                }
                try {
                    if (position != -1 && getItem(position) != null && getItem(position).getImageRes().length() > 0)
                        lawoneServerAudioBinding.imageViewAvatar.setImageResource(getResId(getItem(position).getImageRes()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (clientAudioBinding != null) { //client audio
                if (FileHelper.checkFileExistence(context, getItem(position).getTitle())) {
                    downloadProgressDone(clientAudioBinding.waveView, null, false);
                    clientAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getTitle()));
                }
            }
            if (serverAudioBinding != null) { //server audio
                if (FileHelper.checkFileExistence(context, getItem(position).getTitle())) {
                    downloadProgressDone(serverAudioBinding.waveView, null, false);
                    serverAudioBinding.wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getTitle()));
                }
            }
        }

        //HELPER FUNCTIONS
        private DownloadStatusListenerV1 downloadListenerCreator(AppCompatImageView imageViewCheckmark, ProgressBar loadingProgress, AudioWaveView wave, WaveLoadingView waveView) {
            return new DownloadStatusListenerV1() {
                @Override
                public void onDownloadComplete(DownloadRequest downloadRequest) {
                    imageViewCheckmark.setVisibility(View.VISIBLE);
                    if (waveView != null)
                        waveView.setWaveColor(context.getResources().getColor(R.color.green));
                    if (loadingProgress != null)
                        loadingProgress.setVisibility(View.GONE);
                    if (wave != null) {
                        wave.setRawData(FileHelper.getFileBytes(context, getItem(getBindingAdapterPosition()).getTitle()));
                        wave.performClick();
                    }
                }

                @Override
                public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                    if (loadingProgress != null)
                        loadingProgress.setVisibility(View.GONE);
                    if (waveView != null)
                        waveView.setCenterTitle(context.getString(R.string.download));
                }

                @Override
                public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                    if (waveView != null) {
                        waveView.setCenterTitle("");
                        waveView.setProgressValue(progress);
                        waveView.startAnimation();
                    }
                    if (loadingProgress != null)
                        loadingProgress.setVisibility(View.VISIBLE);
                }
            };
        }

        private void downloadProgressDone(WaveLoadingView waveView, AppCompatImageView imageCheck, boolean showCheckMark) {
            if (waveView != null) {
                waveView.setCenterTitle("");
                waveView.setProgressValue(100);
                waveView.setWaveColor(context.getResources().getColor(R.color.green));
            }
            if (imageCheck != null && showCheckMark)
                imageCheck.setVisibility(View.VISIBLE);
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
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
            });
        }

        public ConversationViewHolder(ViewConversationClientVideoBinding clientVideoBinding) {
            super(clientVideoBinding.getRoot());
            this.clientVideoBinding = clientVideoBinding;
            this.clientVideoBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
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
                if (FileHelper.checkFileExistence(context, getItem(getBindingAdapterPosition()).getTitle())) {
                    if (this.clientAudioBinding.getIsPlaying()) {
                        mp.pause();
                        this.clientAudioBinding.setIsPlaying(false);
                        return;
                    }
                    mp = new MediaPlayer();
                    try {
                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {
                            mp.start();
                            this.clientAudioBinding.setIsPlaying(true);
                            getAudioSeeker(this.clientAudioBinding.wave, getCurrentPosition()).start();
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
                            getItem(getBindingAdapterPosition()).getTitle(),
                            downloadListenerCreator(clientAudioBinding.imageViewCheckmark, null, clientAudioBinding.wave, null));
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
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
            });
        }

        public ConversationViewHolder(ViewConversationServerImageBinding serverImageBinding) {
            super(serverImageBinding.getRoot());
            this.serverImageBinding = serverImageBinding;
            this.serverImageBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
            });
        }

        public ConversationViewHolder(ViewConversationServerVideoBinding serverVideoBinding) {
            super(serverVideoBinding.getRoot());
            this.serverVideoBinding = serverVideoBinding;
            this.serverVideoBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
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
                if (FileHelper.checkFileExistence(context, getItem(getBindingAdapterPosition()).getTitle())) {
                    if (this.serverAudioBinding.getIsPlaying()) {
                        mp.pause();
                        this.serverAudioBinding.setIsPlaying(false);
                        return;
                    }
                    mp = new MediaPlayer();
                    try {
                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {
                            mp.start();
                            this.serverAudioBinding.setIsPlaying(true);
                            getAudioSeeker(serverAudioBinding.wave, getCurrentPosition()).start();
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
                            getItem(getBindingAdapterPosition()).getTitle(),
                            downloadListenerCreator(serverAudioBinding.imageViewCheckmark, null, serverAudioBinding.wave, null));
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
                FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
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
                FileHelper.openUrl(context, getItem(getBindingAdapterPosition()).getFileAddress());
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
                if (FileHelper.checkFileExistence(context, getItem(getBindingAdapterPosition()).getTitle())) {
                    Uri fileUri = FileHelper.getFileUri(context, getItem(getBindingAdapterPosition()).getTitle());
                    String type = FileHelper.getMimeType(context, fileUri);
                    if (type.contains("/") && type.split("/")[1].equals("pdf"))
                        activatePdfInterface(fileUri);
                    else
                        FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
                } else {
                    try {
                        downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                                getItem(getBindingAdapterPosition()).getTitle(),
                                downloadListenerCreator(lawoneClientFileBinding.imageViewCheckmark, lawoneClientFileBinding.progressbarLoading, null, lawoneClientFileBinding.waveView));
                        if (downloadRequest != null)
                            downloadManager.add(downloadRequest);
                    } catch (Exception e) {
                        Log.e("tag", "ConversationViewHolder: " + e);
                    }
                }
            });
        }

        public ConversationViewHolder(LawoneConversationServerImageBinding serverImageBinding) {
            super(serverImageBinding.getRoot());
            this.lawoneServerImageBinding = serverImageBinding;
            this.lawoneServerImageBinding.imageViewImage.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                FileHelper.openUrl(context, getItem(getBindingAdapterPosition()).getFileAddress());
            });
        }

        public ConversationViewHolder(LawoneConversationClientAudioBinding clientAudioBinding) {
            super(clientAudioBinding.getRoot());
            this.lawoneClientAudioBinding = clientAudioBinding;
            this.lawoneClientAudioBinding.setIsPlaying(false);
            this.lawoneClientAudioBinding.frameLayoutFile.setOnClickListener(v -> {
                if (getBindingAdapterPosition() == -1 || getItem(getBindingAdapterPosition()) == null)
                    return;
                if (lastPlayingPos != -1) {
                    getCurrentList().snapshot().get(lastPlayingPos).setPlaying(false);
                    notifyItemChanged(lastPlayingPos);
                }
                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, getItem(getBindingAdapterPosition()).getTitle())) {
                    try {
                        if (mp != null && mp.isPlaying()) {
                            mp.stop();
                            mp.release();
                            if (lawoneClientAudioBinding.getIsPlaying()) {
                                lawoneClientAudioBinding.setIsPlaying(false);
                                lastPlayingPos = -1;
                                return;
                            }
                        }
                        mp = new MediaPlayer();
                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {

                            mp.start();
                            lawoneClientAudioBinding.setIsPlaying(true);
                            getAudioSeeker(lawoneClientAudioBinding.wave, getCurrentPosition()).start();
                            lastPlayingPos = getCurrentPosition();
                        });
                        mp.setOnCompletionListener(mp -> {
                            lawoneClientAudioBinding.setIsPlaying(false);
                            lastPlayingPos = -1;
                        });
                        mp.setOnErrorListener((mp, what, extra) -> {
                            lawoneClientAudioBinding.setIsPlaying(false);
                            lastPlayingPos = -1;
                            return false;
                        });
                    } catch (Exception e) {
                        mp = new MediaPlayer();
                        Log.e("TAG", "ConversationViewHolder: " + e.getCause());
                        e.printStackTrace();
                    }
                } else {
                    try {
                        downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                                getItem(getBindingAdapterPosition()).getTitle(),
                                downloadListenerCreator(lawoneClientAudioBinding.imageViewCheckmark, lawoneClientAudioBinding.progressbarLoading, lawoneClientAudioBinding.wave, null));
                        if (downloadRequest != null)
                            downloadManager.add(downloadRequest);
                    } catch (Exception e) {
                        Log.e("TAG", "ConversationViewHolder: " + e.getMessage());
                    }
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
                if (lastPlayingPos != -1) {
                    getCurrentList().snapshot().get(lastPlayingPos).setPlaying(false);
                    notifyItemChanged(lastPlayingPos);
                }
                if (!PermissionHelper.checkStoragePermission(context)) {
                    new PermissionDialog(context, () -> conversationListListener.requestStoragePermission()).show();
                    return;
                }
                if (FileHelper.checkFileExistence(context, getItem(getBindingAdapterPosition()).getTitle())) {
                    if (mp != null && mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                        if (lawoneClientAudioBinding.getIsPlaying()) {
                            lawoneClientAudioBinding.setIsPlaying(false);
                            lastPlayingPos = -1;
                            return;
                        }
                    }
                    mp = new MediaPlayer();
                    try {
                        lawoneServerAudioBinding.imageViewPlay.setImageDrawable(context.getDrawable(R.drawable.ic_pause));
                        mp.setDataSource(getItem(getBindingAdapterPosition()).getFileAddress());
                        mp.prepareAsync();
                        mp.setOnPreparedListener(mp -> {
                            if (lastPlayingPos != -1) {
                                getCurrentList().snapshot().get(lastPlayingPos).setPlaying(false);
                                notifyItemChanged(lastPlayingPos);
                            }
                            mp.start();
                            lawoneServerAudioBinding.setIsPlaying(true);
                            getAudioSeeker(lawoneServerAudioBinding.wave, getCurrentPosition()).start();
                            lastPlayingPos = getCurrentPosition();
                        });
                        mp.setOnCompletionListener(mp -> {
                            lawoneServerAudioBinding.setIsPlaying(false);
                            lastPlayingPos = -1;
                        });
                        mp.setOnErrorListener((mp, what, extra) -> {
                            lawoneServerAudioBinding.setIsPlaying(false);
                            lastPlayingPos = -1;
                            return false;
                        });
                    } catch (Exception e) {
                        mp = new MediaPlayer();
                        Log.e("tag", "ConversationViewHolder: " + e.getCause());
                        e.printStackTrace();
                    }
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            getItem(getBindingAdapterPosition()).getTitle(),
                            downloadListenerCreator(lawoneServerAudioBinding.imageViewCheckmark, lawoneServerAudioBinding.progressbarLoading, lawoneServerAudioBinding.wave, null));
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
                if (FileHelper.checkFileExistence(context, getItem(getBindingAdapterPosition()).getTitle())) {
                    Uri fileUri = FileHelper.getFileUri(context, getItem(getBindingAdapterPosition()).getTitle());
                    String type = FileHelper.getMimeType(context, fileUri);
                    if (type.contains("/") && type.split("/")[1].equals("pdf"))
                        activatePdfInterface(fileUri);
                    else
                        FileHelper.openFile(context, getItem(getBindingAdapterPosition()).getFileAddress(), getItem(getBindingAdapterPosition()).getTitle());
                } else {
                    downloadRequest = FileHelper.downloadFile(context, getItem(getBindingAdapterPosition()).getFileAddress(),
                            getItem(getBindingAdapterPosition()).getTitle(),
                            downloadListenerCreator(lawoneServerFileBinding.imageViewCheckmark, lawoneServerFileBinding.progressbarLoading, null, lawoneServerFileBinding.waveView));
                    if (downloadRequest != null)
                        downloadManager.add(downloadRequest);
                }
            });
        }

        public ConversationViewHolder(LawoneConversationSystemBinding lawoneConversationSystemBinding) {
            super(lawoneConversationSystemBinding.getRoot());
            this.lawoneConversationSystemBinding = lawoneConversationSystemBinding;
        }
    }

    private void activatePdfInterface(Uri fileUri) {
        conversationListListener.pdfFileClicked(fileUri);
    }

    public void stopMediaPlayer() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
            }
            mp.release();
            mp = null;
            lastPlayingPos = -1;
        }
    }

    public int getResId(String num) {
        int index = Integer.parseInt(num);
        switch (index) {
            case 0:
                return R.drawable.ic_avatar_0;
            case 1:
                return R.drawable.ic_avatar_1;
            case 2:
                return R.drawable.ic_avatar_2;
            case 3:
                return R.drawable.ic_avatar_3;
            case 4:
                return R.drawable.ic_avatar_4;
            case 5:
                return R.drawable.ic_avatar_5;
            default:
                return -1;
        }
    }
}

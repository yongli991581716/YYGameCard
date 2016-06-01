
package com.lordcard.ui.personal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lordcard.common.schedule.AutoTask;
import com.lordcard.common.schedule.ScheduledTask;
import com.lordcard.common.util.ActivityUtils;
import com.lordcard.common.util.AudioPlayUtils;
import com.lordcard.common.util.ImageUtil;
import com.lordcard.common.util.MultiScreenTool;
import com.lordcard.ui.base.IGameView;
import com.lordcard.ui.personal.logic.PlayAlone;
import com.ylly.playcard.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 比赛场游戏结束界面
 * 
 * @author Administrator
 */
@SuppressLint("HandlerLeak")
public class AloneGameEndDialog extends Dialog implements IGameView,
        android.view.View.OnClickListener {
    private static final int EXIT = 1212;
    private static final int GO_BACK = 1211;
    private static final int GO_AGAIN = 1213;
    private static final int SHOW_IQ_GRADE_MIN = 1214;
    public static final int GED_SHOW_IQ_GRADE_MAX = 1215;
    private static final int VISIBLE_VIEW = 121;

    // private Poker[] poker = null;
    private int nextPlay = -1;
    private String beishuNumber = null;
    // private Drawable icoWin;
    private MultiScreenTool mst = MultiScreenTool.singleTonHolizontal();
    // private int cardSpace = 0;
    private Context context;
    // private List<PlayAlone> list;
    private int order;
    private LinkedList<PlayAlone> users = null;
    private Handler mHandler;
    private Handler handler;
    private RelativeLayout mainLayout;
    private ImageView headIv1, headIv2, headIv3 = null;// 玩家头像
    private ImageView titlaSexIv;// 标题人物图片
    private TextView nameTv1, nameTv2, nameTv3 = null;// 玩家昵称
    private TextView zhiDouTv1, zhiDouTv2, zhiDouTv3 = null;// 金豆
            // private TextView zhiZuangTv1, zhiZuangTv2, zhiZuangTv3 = null;//钻石
    private TextView zhiLiTv1, zhiLiTv2, zhiLiTv3 = null;// 经验
    private TextView zongBeiShuTv, diShuTv, nowZhiDouTv, ZhiShangTv = null;// 炸弹，春天，加倍，房间倍数，叫分倍数，总倍数，底注，当前金豆，等级
    private Button againBtn, backBtn;// 关闭，再来一局，返回，微博分享
    private ListView mBeiShuList;// 左边倍数列表
    private List<BeiShu> beiShu;// 倍数数据
    private int mBaseScore;
    private AutoTask goOutTask, showUpgradeTask, showUpgradeTask2;

    public AloneGameEndDialog(Context context, List<PlayAlone> list, int order, Handler handler) {
        super(context, R.style.process_dialog);
        this.context = context;
        // this.list = list;
        this.order = order;
        this.users = new LinkedList<PlayAlone>(list);
        this.handler = handler;
    }

    public AloneGameEndDialog(Context context, List<PlayAlone> list, int order, Handler handler,
            int baseScore) {
        super(context, R.style.process_dialog);
        this.context = context;
        mBaseScore = baseScore;
        this.order = order;
        this.users = new LinkedList<PlayAlone>(list);
        this.handler = handler;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new BitmapDrawable());
        // setContentView(R.layout.doudizhu_end);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case EXIT:
                        handler.sendEmptyMessage(24);
                        dismiss();
                        release();
                        break;
                    case VISIBLE_VIEW:
                        handler.sendEmptyMessage(23);
                        break;
                    case GO_BACK:// 返回游戏大厅
                        release();
                        handler.sendEmptyMessage(25);
                        dismiss();
                        break;
                    case GO_AGAIN:// 再来一局
                        dismiss();
                        release();
                        handler.sendEmptyMessage(26);
                        break;
                    case SHOW_IQ_GRADE_MIN:// 显示IQ升级对话框(小级)
                       
                        break;
                    default:
                        break;
                }
            }
        };
        beiShu = new ArrayList<AloneGameEndDialog.BeiShu>();
        initView();
        refreshData();
        // 3分钟后自动离开
        goOutTask = new AutoTask() {
            public void run() {
                handler.sendEmptyMessage(25);
            }
        };
        ScheduledTask.addDelayTask(goOutTask, (1000 * 60 * 3));
    }

    public void initView() {
        mainLayout = (RelativeLayout) findViewById(R.id.dzed_top_layout);
        mst.adjustView(mainLayout);
        headIv1 = (ImageView) findViewById(R.id.dzed_head_1);
        ImageView dongjie = (ImageView) findViewById(R.id.dzed_img_freeze);
        dongjie.setVisibility(View.INVISIBLE);// 隐藏冻结
        headIv2 = (ImageView) findViewById(R.id.dzed_head_2);
        headIv3 = (ImageView) findViewById(R.id.dzed_head_3);
        titlaSexIv = (ImageView) findViewById(R.id.dzed_lose_win_head_iv);

        nameTv1 = (TextView) findViewById(R.id.dzed_nickname_1);
        nameTv2 = (TextView) findViewById(R.id.dzed_nickname_2);
        nameTv3 = (TextView) findViewById(R.id.dzed_nickname_3);

        zhiDouTv1 = (TextView) findViewById(R.id.dzed_zhidou_1);
        zhiDouTv2 = (TextView) findViewById(R.id.dzed_zhidou_2);
        zhiDouTv3 = (TextView) findViewById(R.id.dzed_zhidou_3);

        zhiLiTv1 = (TextView) findViewById(R.id.dzed_zhili_1);
        zhiLiTv2 = (TextView) findViewById(R.id.dzed_zhili_2);
        zhiLiTv3 = (TextView) findViewById(R.id.dzed_zhili_3);

        // zhaDanTv=(TextView) findViewById(R.id.dzed_zhadang);
        // chunTianTv=(TextView) findViewById(R.id.dzed_chuntian);
        // jiaBeiv=(TextView) findViewById(R.id.dzed_jiabei);
        zongBeiShuTv = (TextView) findViewById(R.id.dzed_zongbeishu);
        diShuTv = (TextView) findViewById(R.id.dzed_dishu);
        nowZhiDouTv = (TextView) findViewById(R.id.dzed_now_zhidou);
        ZhiShangTv = (TextView) findViewById(R.id.dzed_now_zhishang);

        againBtn = (Button) findViewById(R.id.dzed_again);
        backBtn = (Button) findViewById(R.id.dzed_back);
        mBeiShuList = (ListView) findViewById(R.id.beishu_list);
        againBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);

        mainLayout.findViewById(R.id.dzed_recharge_btn).setVisibility(View.INVISIBLE);

    }

    /**
     * 刷新结算数据
     */
    public void refreshData() {

        // 判断是否有人逃跑
        boolean hasEscape = false;
        for (PlayAlone end : users) {
            if (end.isEscape())
                hasEscape = true; // 有人逃跑
        }

        // 判断是否是地主赢
        boolean isdizhu = false;
        if (hasEscape) {
            for (PlayAlone end : users) {
                if (end.isEscape() && !end.isCall()) { // 有人逃跑，且不是地主
                    isdizhu = true;

                }
            }
        } else {
            for (PlayAlone end : users) {
                if (end.getCount() == 0 && end.isCall()) {
                    isdizhu = true;
                }
            }
        }
        boolean dizhuStove = false;
        boolean onlyDizhu = false;
        for (PlayAlone end : users) {
            // 如果是自己的顺序
            if (end.getOrder() == order) {
                Log.i("item", "第一项" + "isdizhu:" + isdizhu + "  dizhuStove:" + dizhuStove
                        + "  onlyDizhu:" + onlyDizhu);
                theFirstItem(isdizhu, dizhuStove, onlyDizhu, end);
            } else if (nextPlay == -1) {
                theSecondItem(isdizhu, dizhuStove, onlyDizhu, end);
                Log.i("item", "第二项" + "isdizhu:" + isdizhu + "  dizhuStove:" + dizhuStove
                        + "  onlyDizhu:" + onlyDizhu);
            } else {
                theThreeItem(isdizhu, dizhuStove, onlyDizhu, end);
                Log.i("item", "第三项" + "isdizhu:" + isdizhu + "  dizhuStove:" + dizhuStove
                        + "  onlyDizhu:" + onlyDizhu);
            }
        }
    }

    /**
     * 设置第一项的数据
     * 
     * @param isdizhu
     * @param dizhuStove
     * @param onlyDizhu
     * @param end
     */
    private void theFirstItem(boolean isdizhu, boolean dizhuStove, boolean onlyDizhu,
            final PlayAlone end) {
        // cardSpace = 15;
        zhiDouTv1.setText(String.valueOf(end.getPayment()));
        nameTv1.setText(end.getNickMap().get(end.getOrder()));
        AudioPlayUtils.isPlay = true;
        AudioPlayUtils.isGameEnd = true;
        nowZhiDouTv.setText("" + end.getBean());
        ZhiShangTv.setText("" + 0);

        beiShu.add(new BeiShu(R.drawable.game_end_dibei, "底倍", end.getBaseRatio()));
        beiShu.add(new BeiShu(R.drawable.game_end_zhadan, "炸弹", end.getBombRatio()));
        beiShu.add(new BeiShu(R.drawable.game_end_spring, "春天", end.getSpringRatio()));
        beiShu.add(new BeiShu(R.drawable.game_end_jiaofen, "叫分", end.getCallRatio()));

        beishuNumber = String.valueOf(end.getRatio());
        zongBeiShuTv.setText(beishuNumber);
        diShuTv.setText(String.valueOf(mBaseScore));
        mBeiShuList.setAdapter(new BeiShuAdapter());
        if (end.isUpgrade()) {// 是否升级
            showUpgradeTask = new AutoTask() {
                public void run() {
                    Message msg = new Message();
                    Bundle b = new Bundle();
                    b.putString("getCelebratedText", end.getCelebratedText());// 提示内容
                    b.putBoolean("isCall", end.isCall());// 是否是地主
                    msg.what = SHOW_IQ_GRADE_MIN;
                    msg.setData(b);
                    mHandler.sendMessage(msg);
                }
            };
            ScheduledTask.addDelayTask(showUpgradeTask, (700));
            int isTitle = end.getIsTitle() == null ? 0 : end.getIsTitle();
            if (1 == isTitle) {
                showUpgradeTask2 = new AutoTask() {
                    public void run() {
                        mHandler.sendEmptyMessage(GED_SHOW_IQ_GRADE_MAX);
                    }
                };
                ScheduledTask.addDelayTask(showUpgradeTask2, (200));
            }
        }

        zhiLiTv1.setText("0");
        if (end.isCall()) { // 自己是地主
            ActivityUtils.setHead(headIv1, "0", true);

            if (isdizhu) { // 地主赢
                findViewById(R.id.dzed_lose_win).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.game_end_dialog_win_text, true));
                findViewById(R.id.dzed_lose_win_top_iv).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.iq_grade_dialog_win_bg, true));
                AudioPlayUtils.getInstance().playMusic(false, R.raw.win); // 胜利
                setTitalSex("0", true, true);
            } else {
                findViewById(R.id.dzed_lose_win).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.game_end_dialog_lose_text, true));
                findViewById(R.id.dzed_lose_win_top_iv).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.iq_grade_dialog_lose_bg, true));
                AudioPlayUtils.getInstance().playMusic(false, R.raw.lose); // 失败
                setTitalSex("0", false, false);
            }
        } else { // 自己是农民
            ActivityUtils.setHead(headIv1, "0", false);
            if (isdizhu) { // 地主赢
                findViewById(R.id.dzed_lose_win).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.game_end_dialog_lose_text, true));
                findViewById(R.id.dzed_lose_win_top_iv).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.iq_grade_dialog_lose_bg, true));
                AudioPlayUtils.getInstance().playMusic(false, R.raw.lose); // 失败
                setTitalSex("0", true, false);
            } else {
                findViewById(R.id.dzed_lose_win).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.game_end_dialog_win_text, true));
                findViewById(R.id.dzed_lose_win_top_iv).setBackgroundDrawable(
                        ImageUtil.getResDrawable(R.drawable.iq_grade_dialog_win_bg, true));
                AudioPlayUtils.getInstance().playMusic(false, R.raw.win);
                setTitalSex("0", false, true);
            }
        }
        AudioPlayUtils.isPlay = false;
    }

    private void setParams(android.view.WindowManager.LayoutParams lay) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        Rect rect = new Rect();
        View view = getWindow().getDecorView();
        view.getWindowVisibleDisplayFrame(rect);
        lay.height = dm.heightPixels - rect.top;
        lay.width = dm.widthPixels;
    }

    /**
     * 设置标题人物图片
     * 
     * @param gender 性别
     * @param isDizhu 是否是地主
     * @param iswin 是否赢
     */
    private void setTitalSex(String gender, boolean isDizhu, boolean iswin) {
        if (isDizhu) {// 是否是地主
            if (gender.equals("1")) {// 性别 0保密/1女/2男
                if (iswin) {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_win, true));
                } else {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_lose, true));
                }
            } else {
                if (iswin) {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_win, true));
                } else {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_lose, true));
                }
            }
        } else {
            if (gender.equals("1")) {// 性别 0保密/1女/2男
                if (iswin) {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_win, true));
                } else {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_lose, true));
                }
            } else {
                if (iswin) {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_win, true));
                } else {
                    titlaSexIv.setImageDrawable(ImageUtil.getResDrawable(
                            R.drawable.game_end_dialog_man_lose, true));
                }
            }
        }
    }

    /**
     * 设置第三项的数据
     * 
     * @param isdizhu
     * @param dizhuStove
     * @param onlyDizhu
     * @param end
     */
    private void theThreeItem(boolean isdizhu, boolean dizhuStove, boolean onlyDizhu, PlayAlone end) {
        // cardSpace = 15;
        zhiDouTv3.setText(String.valueOf(end.getPayment()));
        nameTv3.setText(end.getNickMap().get(end.getOrder()));
        // String[] last = end.getCards().substring(1, end.getCards().length() - 1).split(",");
        zhiLiTv3.setText("0");
        if (end.isCall()) { // 自己是地主
            ActivityUtils.setHead(headIv3, "0", true);
        } else { // 自己是农民
            ActivityUtils.setHead(headIv3, "0", false);
        }
    }

    /**
     * 设置第二项的数据
     * 
     * @param isdizhu
     * @param dizhuStove
     * @param onlyDizhu
     * @param end
     */
    private void theSecondItem(boolean isdizhu, boolean dizhuStove, boolean onlyDizhu, PlayAlone end) {
        // cardSpace = 15;
        nextPlay = end.getOrder();
        zhiDouTv2.setText(String.valueOf(end.getPayment()));
        nameTv2.setText(end.getNickMap().get(end.getOrder()));
        zhiLiTv2.setText("0");
        if (end.isCall()) { // 自己是地主
            ActivityUtils.setHead(headIv2, "0", true);
        } else { // 自己是农民
            ActivityUtils.setHead(headIv2, "0", false);
        }
    }

    private void release() {
        AudioPlayUtils.isGameEnd = false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dzed_again:// 再来一局
                mHandler.sendEmptyMessage(GO_AGAIN);
                break;
            case R.id.dzed_back:// 返回大厅
                mHandler.sendEmptyMessage(GO_BACK);
                // 发消息关闭游戏界面
                break;
            default:
                break;
        }
    }

    class BeiShuAdapter extends BaseAdapter {
        private LayoutInflater layoutInflater = null;

        public BeiShuAdapter() {
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return beiShu.size();
        }

        @Override
        public Object getItem(int position) {
            return beiShu.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = layoutInflater.inflate(R.layout.game_end_dialog_left_list_item, null);
                holder = new ViewHolder();
                holder.iconIv = (ImageView) convertView.findViewById(R.id.gedlli_icon_iv);
                holder.nameTv = (TextView) convertView.findViewById(R.id.gedlli_name_tv);
                holder.numTv = (TextView) convertView.findViewById(R.id.gedlli_num_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.iconIv.setBackgroundResource(beiShu.get(position).getIcomId());
            holder.nameTv.setText(beiShu.get(position).getName());
            holder.numTv.setText("" + beiShu.get(position).getNum());
            return convertView;
        }

        public class ViewHolder {
            private TextView nameTv;// 名称
            private ImageView iconIv;// 图片
            private TextView numTv;// 数量
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (goOutTask != null) {
            goOutTask.stop(true);
            goOutTask = null;
        }
        if (showUpgradeTask != null) {
            showUpgradeTask.stop(true);
            showUpgradeTask = null;
        }
        if (showUpgradeTask2 != null) {
            showUpgradeTask2.stop(true);
            showUpgradeTask2 = null;
        }

    }

    class BeiShu {
        public BeiShu(int icomId, String name, int num) {
            this.num = num;
            this.name = name;
            this.icomId = icomId;
        }

        private int num;
        private int icomId;
        private String name;
        private String x = "X";

        public final int getNum() {
            return num;
        }

        public final void setNum(int num) {
            this.num = num;
        }

        public final int getIcomId() {
            return icomId;
        }

        public final void setIcomId(int icomId) {
            this.icomId = icomId;
        }

        public final String getName() {
            return name;
        }

        public final void setName(String name) {
            this.name = name;
        }

        public final String getX() {
            return x;
        }

        public final void setX(String x) {
            this.x = x;
        }

    }
}

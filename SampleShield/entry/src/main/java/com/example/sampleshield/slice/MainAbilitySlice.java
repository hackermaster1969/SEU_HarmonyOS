package com.example.sampleshield.slice;

import com.example.sampleshield.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;
import ohos.agp.utils.Color;
import ohos.event.notification.NotificationHelper;
import ohos.event.notification.NotificationRequest;
import ohos.event.notification.NotificationSlot;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.rpc.RemoteException;

public class MainAbilitySlice extends AbilitySlice {
    private int count=0;
    private boolean notificationBanned = false;
    private NotificationSlot slot;
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x00201, "MY_ERROR");


    private void initNotificationSlot()
    {
        slot = new NotificationSlot("slot_001","slot_default", NotificationSlot.LEVEL_DEFAULT); // 创建notificationSlot对象
        slot.setDescription("NotificationSlotDescription");
        slot.setEnableVibration(true);
        slot.setLockscreenVisibleness(NotificationRequest.VISIBLENESS_TYPE_PUBLIC);
        slot.setEnableLight(true);
        slot.setLedLightColor(Color.RED.getValue());
        try {
            NotificationHelper.addNotificationSlot(slot);
        } catch (RemoteException ex) {
            HiLog.warn(LABEL, "addNotificationSlot occur exception.");
        }

    }

    private void publishNotification(String s, int id)
    {
        NotificationRequest request = new NotificationRequest(id);
        request.setSlotId(slot.getId());
        NotificationRequest.NotificationNormalContent normalContent = new NotificationRequest.NotificationNormalContent();
        String title = "我的消息弹窗";
        normalContent.setTitle(title)
                .setText(s);
        NotificationRequest.NotificationContent notificationContent = new NotificationRequest.NotificationContent(normalContent);
        request.setContent(notificationContent);
        if(!notificationBanned) {
            try {
                NotificationHelper.publishNotification(request);

            } catch (RemoteException ex) {
                HiLog.warn(LABEL, "publishNotification occur exception.");
            }
        }

    }

    private void cancelNotification(int id)
    {

        try {
            NotificationHelper.cancelNotification(id);
        } catch (RemoteException ex) {
            HiLog.warn(LABEL, "cancelNotification occur exception.");
        }
    }
    @Override
    public void onStart(Intent intent)
    {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        Button button = (Button) findComponentById(ResourceTable.Id_button_sample);
        Button button_cancel = (Button) findComponentById(ResourceTable.Id_button_cancel);
        TextField textField = (TextField) findComponentById(ResourceTable.Id_text_in);
        Text text=(Text)findComponentById(ResourceTable.Id_text_count);
        Text text_ban= (Text) findComponentById(ResourceTable.Id_text_banned);
        if (button != null )
        {

            button.setClickedListener(component -> {
                String content = textField.getText();
                count++;
                text.setText("发送次数:" + count);
                initNotificationSlot();
                publishNotification(content, 1);
            });

        }
        if (button_cancel!=null)
        {
            button_cancel.setClickedListener(component -> {

                    cancelNotification(1);
                    notificationBanned=!notificationBanned;
                    text_ban.setText("屏蔽状态："+notificationBanned);

            });
        }
    }

    @Override
    public void onActive()
    {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

}

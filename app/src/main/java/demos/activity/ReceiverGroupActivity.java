package demos.activity;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import demos.view.ReceiversGroup;

public class ReceiverGroupActivity extends ToolbarActivity {

    @Override
    public int bindLayout() {
        return R.layout.activity_receiver_group;
    }

    @Override
    public void initView() {
        ReceiversGroup receiversGroup = $(R.id.receivers_group);
        List<ReceiversGroup.Contact> list = new ArrayList<>();
        list.add(new ReceiversGroup.Contact("11111111111", "11111111111"));
        list.add(new ReceiversGroup.Contact("22222222222", "22222222222"));
        list.add(new ReceiversGroup.Contact("33333333333", "张三"));
        list.add(new ReceiversGroup.Contact("44444444444", "李四"));
        list.add(new ReceiversGroup.Contact("55555555555", "王二麻子"));
        list.add(new ReceiversGroup.Contact("66666666666", "66666666666"));
        list.add(new ReceiversGroup.Contact("77777777777", "淘气"));
        list.add(new ReceiversGroup.Contact("88888888888", "88888888888"));
        receiversGroup.addContacts(list);
    }
}

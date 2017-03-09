package demos.activity;

import com.victor.androiddemos.R;

import java.util.ArrayList;
import java.util.List;

import demos.view.ShareSheetView;

public class ShareSheetActivity extends ToolbarActivity {
    private ShareSheetView sheetView;
    private int n = 1;

    @Override
    public int bindLayout() {
        return R.layout.activity_share_sheet;
    }

    @Override
    public void initView() {
        sheetView = (ShareSheetView) findViewById(R.id.share_sheet_view);
        initSSV();
    }


    private void initSSV() {
        final ShareSheetView.DataSource dataSource = new ShareSheetView.DataSource();
        ShareSheetView.TitleSource titleSource = new ShareSheetView.TitleSource();
        List<String> leftTitles = new ArrayList<>();
        leftTitles.add("左标题1");
        leftTitles.add("左标题2");
        List<String> rightTitles = new ArrayList<>();
        for (int i = 1; i < 11; i++) {
            rightTitles.add("右标题" + i);
        }
        titleSource.setLeft(leftTitles);
        titleSource.setRight(rightTitles);
        dataSource.setTitle(titleSource);
        List<ShareSheetView.ValueSource> valueSources = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            ShareSheetView.ValueSource valueSource = new ShareSheetView.ValueSource();
            List<String> leftValues = new ArrayList<>();
            leftValues.add("左项" + i + "-1");
            leftValues.add("左项" + i + "-2");
            valueSource.setLeft(leftValues);
            List<String> rightValues = new ArrayList<>();
            for (int j = 1; j < 11; j++) {
                rightValues.add("右项" + i + "-" + j);
            }
            valueSource.setRight(rightValues);
            valueSources.add(valueSource);
        }
        dataSource.setValue(valueSources);

        sheetView.initialize(dataSource);
        sheetView.setOnLoadListener(new ShareSheetView.OnLoadListener() {
            @Override
            public void onLoadMore() {
                sheetView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        n++;
                        sheetView.update(addDataSource(n), n == 9);
                    }
                }, 2000);
            }
        });
    }

    private List<ShareSheetView.ValueSource> addDataSource(int n) {
        List<ShareSheetView.ValueSource> newValueSources = new ArrayList<>();
        for (int i = 10 * n + 1; i < 10 * (n + 1) + 1; i++) {
            ShareSheetView.ValueSource valueSource = new ShareSheetView.ValueSource();
            List<String> leftValues = new ArrayList<>();
            leftValues.add("左项" + i + "-1");
            leftValues.add("左项" + i + "-2");
            valueSource.setLeft(leftValues);
            List<String> rightValues = new ArrayList<>();
            for (int j = 1; j < 11; j++) {
                rightValues.add("左项" + i + "-" + j);
            }
            valueSource.setRight(rightValues);
            newValueSources.add(valueSource);
        }
        return newValueSources;
    }
}

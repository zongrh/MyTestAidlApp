package cn.my.mytestapp;

/**
 * FileName: TestModel
 * Author: nanzong
 * Date: 2022/4/8 10:44 上午
 * Description:
 * History:
 */
public class TestModel {

    private boolean isShow = false;

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isReTestShow(){
        return !isShow;
    }


} 